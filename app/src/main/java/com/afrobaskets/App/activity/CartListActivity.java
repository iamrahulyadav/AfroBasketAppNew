package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrobaskets.App.adapter.CartListAdapter;
import com.afrobaskets.App.bean.CartBean;
import com.afrobaskets.App.bean.CartListBean;
import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.constant.SavePref;
import com.afrobaskets.App.interfaces.CartCallback;
import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.CartActivityBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartListActivity extends AppCompatActivity implements CartCallback {
    private List<CartBean> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CartListAdapter fAdapter;
    ProgressDialog pDialog;
    CartActivityBinding cartActivityBinding;
    ArrayList<CartListBean> cartListBeanArrayList = new ArrayList<>();
    JSONObject sendJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartActivityBinding = DataBindingUtil.setContentView(this, R.layout.cart_activity);
        // Constants.getCartList(CartListActivity.this);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_count.setText("" + Constants.cartListBeenArray.size());
        cartActivityBinding.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!SavePref.get_credential(CartListActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true"))
                {
                    Intent intent=new Intent(CartListActivity.this, LoginActivity.class);
                    intent.putExtra("cart_list","cartlist");
                    startActivity(intent);
                }
                else {
                  Intent intent = new Intent(CartListActivity.this, AddressList.class);
                  intent.putExtra("checkout", "checkout");
                  startActivity(intent);
              }
            }
        });
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cart_count = (TextView) findViewById(R.id.cart_count);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.cart_layout);
        frameLayout.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        cartActivityBinding.recyclerViewSubcat.setLayoutManager(mLayoutManager);
        cartActivityBinding.recyclerViewSubcat.setItemAnimator(new DefaultItemAnimator());
        pDialog = new ProgressDialog(CartListActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        Constants.getCartList(CartListActivity.this);
    }

    TextView cart_count;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onSuccess(ArrayList<CartListBean> cartListBeanArrayList, int result) {
        if(pDialog!=null)
        {
            pDialog.dismiss();
        }

        cart_count.setText("" + Constants.cartListBeenArray.size());
        fAdapter = new CartListAdapter(CartListActivity.this, cartListBeanArrayList);
        cartActivityBinding.recyclerViewSubcat.setAdapter(fAdapter);
        if(cartListBeanArrayList.size()<=0)
        {
            finish();
        }
    }
}


