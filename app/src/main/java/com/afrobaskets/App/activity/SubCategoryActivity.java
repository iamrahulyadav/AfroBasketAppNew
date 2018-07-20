package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobaskets.App.adapter.SubCategoryPagerAdapter;
import com.afrobaskets.App.bean.CartListBean;
import com.afrobaskets.App.bean.CategoryListBeans;
import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.fragments.SubCategoriesFragment;
import com.afrobaskets.App.interfaces.CartCallback;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubCategoryActivity extends AppCompatActivity implements CartCallback
{
    @Bind(R.id.tabs)        TabLayout tabs;
    @Bind(R.id.pager)       ViewPager pager;
    ProgressDialog pDialog;
    SubCategoryPagerAdapter adapter;
   // CharSequence Titles[]={"Coffee","Fruit","Drink"};
    public static  int check_position;
    public static  String main_id;
    JSONObject sendjson;
    TextView cart_count;
    ArrayList<CategoryListBeans> categoryListBeansArrayList=new ArrayList<>();
    TextView tabOne;

    private void getCategory(final String ids, final String type) {

        if(!Constants.isNetworkAvailable(this))
        {
            Toast.makeText(getApplicationContext(),"Internet not connected",Toast.LENGTH_SHORT).show();
            return;
        }


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        try {
            sendjson = new JSONObject();
            sendjson.put("method", "categoryList");
            sendjson.put("parent_category_id", ids);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObject= new JSONObject(response).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            categoryListBeansArrayList=new ArrayList<>();
                            while( keys.hasNext() )
                            {
                                CategoryListBeans categoryListBeans=new CategoryListBeans();
                                String key = keys.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                categoryListBeans.setId(innerJObject.getString("id"));
                                categoryListBeans.setCategory_name(innerJObject.getString("category_name"));
                                categoryListBeans.setParent_category_id(innerJObject.getString("parent_category_id"));
                                categoryListBeans.setCategory_des(innerJObject.getString("category_des"));
                                categoryListBeansArrayList.add(categoryListBeans);
                          }
                           }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Intent intent=new Intent(SubCategoryActivity.this,Product_List_Activity.class);
                            intent.putExtra("id",ids);
                            intent.putExtra("type",type);
                            if(type.equalsIgnoreCase("banner"))
                            {
                                intent.putExtra("city_id",getIntent().getStringExtra("city_id"));
                                intent.putExtra("method",getIntent().getStringExtra("method"));

                            }
                            startActivity(intent);
                            finish();
                            return;
                        }

                        setUpTabs();

                        /*
pagination=1;
page=1;
page=2;
*/
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(SubCategoryActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(SubCategoryActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(SubCategoryActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(SubCategoryActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(SubCategoryActivity.this,"Parse Error!");
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendjson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+sendjson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void createTabIcons()
    {
for(int i=0;i<categoryListBeansArrayList.size();i++)
{
    TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
    tabOne.setText(categoryListBeansArrayList.get(i).getCategory_name());
    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories_select, 0, 0);
    tabOne.setTextColor(Color.WHITE);
    tabs.getTabAt(i).setCustomView(tabOne);
}
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        cart_count.setText(""+Constants.cartcount);
         SubCategoriesFragment frag1 = (SubCategoriesFragment)pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
        frag1.refress();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sub_category_activity);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_count.setText("" + Constants.cartListBeenArray.size());
        setData();
//Constants.getCartList(SubCategoryActivity.this);

    }


    void setUpTabs()
    {
    CharSequence Titles[]=new CharSequence[categoryListBeansArrayList.size()];

    for(int i=0;i<categoryListBeansArrayList.size();i++)
{
    Titles[i]=categoryListBeansArrayList.get(i).getCategory_name();
}

      adapter =  new SubCategoryPagerAdapter(SubCategoryActivity.this,SubCategoryActivity.this.getSupportFragmentManager(),Titles,Titles.length,categoryListBeansArrayList,getIntent().getStringExtra("type"));
      pager.setAdapter(adapter);
      tabs.setupWithViewPager(pager);
      createTabIcons();
      for(int i=0; i < tabs.getTabCount()-1; i++)
        {
            View tab = ((ViewGroup) tabs.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 3, 0);
            tab.requestLayout();
        }
        pager.setOffscreenPageLimit(1);
pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        SubCategoriesFragment frag1 = (SubCategoriesFragment)pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
        frag1.refress();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
});

    }

    void setData()
    {
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            back.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_back) );
        } else {
            back.setBackground(getResources().getDrawable(R.drawable.ic_back));
        }
        main_id=getIntent().getStringExtra("id");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cart_count = (TextView) findViewById(R.id.cart_count);
        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.cart_layout);
        ImageView header=(ImageView)findViewById(R.id.header);
        TextView cat_name=(TextView)findViewById(R.id.cat_name);
        cat_name.setText(getIntent().getStringExtra("name"));
        Glide.with(this).load(getIntent().getStringExtra("url"))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(header);

        frameLayout.setVisibility(View.VISIBLE);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.cartListBeenArray.size()>0) {

                    startActivity(new Intent(SubCategoryActivity.this, CartListActivity.class));
                }
                else {
                    Constants.showSnackBar(SubCategoryActivity.this,getString(R.string.cart_msg));
                }
            }
        });

        ImageView voice = (ImageView) findViewById(R.id.voice);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rel1);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, SearchActivity.class);
                intent.putExtra(
                        "type", "search");
                startActivity(intent);

            }
        });


        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, SearchActivity.class);
                intent.putExtra(
                        "type", "search");
                startActivity(intent);

            }
        });
        ButterKnife.bind(this);

        //getCategory(getIntent().getStringExtra("id"),getIntent().getStringExtra("type"));

        Gson gson = new Gson();
        Type type = new TypeToken<List<CategoryListBeans>>() {}.getType();
        List<CategoryListBeans> categoryListBeanses = gson.fromJson(getIntent().getStringExtra("data"), type);
categoryListBeansArrayList=new ArrayList<CategoryListBeans>(categoryListBeanses);
        setUpTabs();

    }

    @Override
    public void onSuccess(ArrayList<CartListBean> cartListBeanArrayList, int result) {
        cart_count.setText("" + result);

         }

}
