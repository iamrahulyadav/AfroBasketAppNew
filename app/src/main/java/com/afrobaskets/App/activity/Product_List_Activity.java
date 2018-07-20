package com.afrobaskets.App.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afrobaskets.App.adapter.CartListAdapter;
import com.afrobaskets.App.bean.CartBean;
import com.afrobaskets.App.bean.CartListBean;
import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.fragments.SubCategoriesFragment;
import com.afrobaskets.App.interfaces.CartCallback;
import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.ProductlistactivityBinding;

import java.util.ArrayList;
import java.util.List;

public class Product_List_Activity extends AppCompatActivity implements CartCallback {
        private List<CartBean> foodList = new ArrayList<>();
        private RecyclerView recyclerView;
        private CartListAdapter fAdapter;
    TextView cart_count;
    ProductlistactivityBinding  cartActivityBinding;
    SubCategoriesFragment subCategoriesFragment;
    @Override
    protected void onRestart() {
        super.onRestart();
        cart_count.setText("" + Constants.cartListBeenArray.size());
        subCategoriesFragment.refress();
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
             cartActivityBinding = DataBindingUtil.setContentView(this, R.layout.productlistactivity);
       // Constants.getCartList(Product_List_Activity.this);
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_count.setText("" + Constants.cartListBeenArray.size());

        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.cart_layout);
        frameLayout.setVisibility(View.VISIBLE);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.cartListBeenArray.size()>0) {

                    startActivity(new Intent(Product_List_Activity.this, CartListActivity.class));
                }
                else {
                    Constants.showSnackBar(Product_List_Activity.this,getString(R.string.cart_msg));
                }            }
        });
        cartActivityBinding.checkout.setVisibility(View.GONE);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        subCategoriesFragment = new SubCategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",getIntent().getStringExtra("type"));

        bundle.putString("id",getIntent().getStringExtra("id"));
        if(getIntent().getStringExtra("type").equalsIgnoreCase("banner"))
        {
            bundle.putString("city_id",getIntent().getStringExtra("city_id"));
            bundle.putString("method",getIntent().getStringExtra("method"));

        }
        subCategoriesFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, subCategoriesFragment, "HELLO");
        fragmentTransaction.commit();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            back.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_back) );
        } else {
            back.setBackground( getResources().getDrawable(R.drawable.ic_back));
        }
        ImageView voice = (ImageView) findViewById(R.id.voice);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rel1);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_List_Activity.this, SearchActivity.class);
                intent.putExtra(
                        "type", "search");
                startActivity(intent);

            }
        });


        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_List_Activity.this, SearchActivity.class);
                intent.putExtra(
                        "type", "search");
                startActivity(intent);

            }
        });

        }

    @Override
    public void onSuccess(ArrayList<CartListBean> cartListBeanArrayList,int result) {
        cart_count.setText("" + result);
    }
}
