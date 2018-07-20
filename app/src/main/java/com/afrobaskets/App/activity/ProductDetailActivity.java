package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.afrobaskets.App.adapter.ProductDetailPagerAdapter;
import com.afrobaskets.App.bean.CartListBean;
import com.afrobaskets.App.bean.SubCategoriesAdapterbean;
import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.interfaces.CartCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.ProductdetailactivityBinding;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP-PC on 12/1/2017.
 */

public class ProductDetailActivity extends AppCompatActivity implements CartCallback {
    ViewPager viewPager;
    TabLayout tabLayout;
    private ProgressDialog pDialog;
    JSONObject sendJson;
    int position;
    TextView cart_count;
    ProductdetailactivityBinding productdetailactivityBinding;
    List<SubCategoriesAdapterbean> subCategoriesAdapterbeanList;
    String url;
    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cart_count.setText(""+Constants.cartListBeenArray.size());
        try {
            for (int i = 0; i < Constants.cartListBeenArray.size(); i++) {
                if (subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getId().equalsIgnoreCase(Constants.cartListBeenArray.get(i).getCartListProductDetailsBeanArrayList().get(0).getId())) {
                    productdetailactivityBinding.itemCount.setText(Constants.cartListBeenArray.get(i).getNumber_of_item());
                    productdetailactivityBinding.buttonAdd.setVisibility(View.GONE);
                    productdetailactivityBinding.button.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

              }

    void setData() {
        final ImageView product_image = (ImageView) findViewById(R.id.product_image);
        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductDetailActivity.this,ImageShow.class);


                intent.putExtra("image",url);

              startActivity(intent);
            }
        });
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        cart_count = (TextView) findViewById(R.id.cart_count);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.cart_layout);
        frameLayout.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.cartListBeenArray.size()>0) {

                    startActivity(new Intent(ProductDetailActivity.this, CartListActivity.class));
                }
                else {
                    Constants.showSnackBar(ProductDetailActivity.this,getString(R.string.cart_msg));
                }            }
        });

        String ss = getIntent().getStringExtra("data");
        Gson gson = new Gson();
        Type type = new TypeToken<List<SubCategoriesAdapterbean>>() {
        }.getType();
        subCategoriesAdapterbeanList = gson.fromJson(ss, type);
        position = Integer.parseInt(getIntent().getStringExtra("position"));

         TextView brand_image = (TextView) findViewById(R.id.brand_name);

         if(!subCategoriesAdapterbeanList.get(position).getBrand_name().isEmpty())
         {
            brand_image.setVisibility(View.VISIBLE);
            brand_image.setText(subCategoriesAdapterbeanList.get(position).getBrand_name());
        }
        else {
            brand_image.setVisibility(View.GONE);
        }

        productdetailactivityBinding.productName.setText(subCategoriesAdapterbeanList.get(position).getProduct_name());
        //productdetailactivityBinding.productName.setText(subCategoriesAdapterbeanList.get(position).getProduct_name());
        productdetailactivityBinding.price.setText("GHC " + subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getPrice());
        cart_count = (TextView) findViewById(R.id.cart_count);

         url = subCategoriesAdapterbeanList.get(position).getImage_root_path() + "/" + subCategoriesAdapterbeanList.get(position).getImage_id() + "/" + subCategoriesAdapterbeanList.get(position).getImage_name();
        Glide.with(this).load(url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(product_image);
        //hashMap = (LinkedHashMap<Strng, String>) getIntent().getSerializableExtra("map");
        setUpTabs();
        setVarients(productdetailactivityBinding.varientSpinner, position);
        productdetailactivityBinding.varientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                productdetailactivityBinding.price.setText("GHC " + subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(productdetailactivityBinding.varientSpinner.getSelectedItemPosition()).getPrice());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub.3

            }
        });

        productdetailactivityBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productdetailactivityBinding.buttonAdd.setVisibility(View.GONE);
                productdetailactivityBinding.button.setVisibility(View.VISIBLE);
                int count = Integer.parseInt(productdetailactivityBinding.itemCount.getText().toString());
                if (count < 0) {
                    return;
                }
                Constants.updateCart(ProductDetailActivity.this,"add", subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getId(), subCategoriesAdapterbeanList.get(position).getProduct_name() + "(" + subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getAttribute_name() + ")");
                productdetailactivityBinding.itemCount.setText("" + (Integer.parseInt(productdetailactivityBinding.itemCount.getText().toString()) + 1));
            }
        });

        productdetailactivityBinding.buttonPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(productdetailactivityBinding.itemCount.getText().toString());
                if (count < 0) {
                    return;
                }
                Constants.updateCart(ProductDetailActivity.this,"add", subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getId(), subCategoriesAdapterbeanList.get(position).getProduct_name() + "(" + subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getAttribute_name() + ")");
                productdetailactivityBinding.itemCount.setText("" + (Integer.parseInt(productdetailactivityBinding.itemCount.getText().toString()) + 1));
            }
        });
        productdetailactivityBinding.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(productdetailactivityBinding.itemCount.getText().toString());
                if (count <= 1) {
                    productdetailactivityBinding.button.setVisibility(View.GONE);
                    productdetailactivityBinding.buttonAdd.setVisibility(View.VISIBLE);

                }
                if (count <= 0) {
                    productdetailactivityBinding.buttonAdd.setVisibility(View.VISIBLE);
                    productdetailactivityBinding.button.setVisibility(View.GONE);
                    return;
                }
                Constants.updateCart(ProductDetailActivity.this,"delete", subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getId(), subCategoriesAdapterbeanList.get(position).getProduct_name() + "(" + subCategoriesAdapterbeanList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getAttribute_name() + ")");

                productdetailactivityBinding.itemCount.setText("" + (Integer.parseInt(productdetailactivityBinding.itemCount.getText().toString()) - 1));
            }
        });
    }

    void setVarients(Spinner spinner, int position) {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < subCategoriesAdapterbeanList.get(position)
                .getSubCategoriesAdapterAttributesBeanArrayList().size(); i++) {
            list.add(subCategoriesAdapterbeanList.get(position)
                    .getSubCategoriesAdapterAttributesBeanArrayList().get(i).getQuantity()
                    + " " + subCategoriesAdapterbeanList.get(position)
                    .getSubCategoriesAdapterAttributesBeanArrayList().get(i).getUnit());
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productdetailactivityBinding = DataBindingUtil.setContentView(this, R.layout.productdetailactivity);
        setData();
        }


    private void createTabIcons()
    {
        for(int i=0;i<=subCategoriesAdapterbeanList.get(position).getDescription().size();i++)
        {
            try
            {
            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            if(i>=subCategoriesAdapterbeanList.get(position).getDescription().size())
            {

                if(subCategoriesAdapterbeanList.get(position).getNutrition_image()!=null)
                {
                    tabOne.setText("nutrition");     }


   }
            else {
                tabOne.setText("" + subCategoriesAdapterbeanList.get(position).getDescription().keySet().toArray()[i]);
            }
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories_select, 0, 0);
            tabLayout.getTabAt(i).setCustomView(tabOne);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    void setUpTabs()
    {

        CharSequence Titles[]=new CharSequence[subCategoriesAdapterbeanList.get(position).getNutrition_image()!=null
            ?subCategoriesAdapterbeanList.get(position).getDescription().size()+1 : subCategoriesAdapterbeanList.get(position).getDescription().size()];
        int j=0;
        for (String key : subCategoriesAdapterbeanList.get(position).getDescription().keySet()) {
            Titles[j]=key;
            j++;
        }
        if(subCategoriesAdapterbeanList.get(position).getNutrition_image()!=null)
        {

            Titles[j]="nutrition";
    }
    ProductDetailPagerAdapter adapter =  new ProductDetailPagerAdapter
                (this.getSupportFragmentManager(),Titles,Titles.length,subCategoriesAdapterbeanList.get(position).getDescription(),subCategoriesAdapterbeanList.get(position).getNutrition_image(),subCategoriesAdapterbeanList.get(position).getBullet_desc());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
        for(int i=0; i < tabLayout.getTabCount()-1; i++)
        {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 3, 0);
            tab.requestLayout();
        }
    }

    @Override
    public void onSuccess(ArrayList<CartListBean> cartListBeanArrayList, int result) {
        cart_count.setText(""+result);
            }
}