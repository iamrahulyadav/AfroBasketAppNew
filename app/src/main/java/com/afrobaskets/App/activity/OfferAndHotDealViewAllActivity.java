package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afrobaskets.App.adapter.OfferAndHotDealViewAllAcivityAdapter;
import com.afrobaskets.App.bean.CartListBean;
import com.afrobaskets.App.bean.SubCategoriesAdapterAttributesBean;
import com.afrobaskets.App.bean.SubCategoriesAdapterbean;
import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.constant.SavePref;
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
import com.webistrasoft.org.ecommerce.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HP-PC on 12/23/2017.
 */

public class OfferAndHotDealViewAllActivity extends AppCompatActivity implements CartCallback {
ProgressDialog pDialog;
    JSONObject sendJson;
ArrayList<SubCategoriesAdapterbean>subCategoriesAdapterbeanArrayList=new ArrayList<>();
    OfferAndHotDealViewAllAcivityAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView cart_count;
int pageCount=1;
    @Override
    protected void onRestart() {
        super.onRestart();
        cart_count.setText(""+Constants.cartListBeenArray.size());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccess(ArrayList<CartListBean> cartListBeanArrayList, int result) {

        cart_count.setText(""+result);

            }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offerandhotdealviewallactivity);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_count.setText(""+Constants.cartListBeenArray.size());
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
         progressBar = (ProgressBar)findViewById(R.id.progressBar);
        ImageView toolbar_back=(ImageView)findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        adapter = new OfferAndHotDealViewAllAcivityAdapter(OfferAndHotDealViewAllActivity.this,subCategoriesAdapterbeanArrayList);
        final GridLayoutManager layoutManager1
                = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getProducts(pageCount);
        FrameLayout cart = (FrameLayout) findViewById(R.id.cart_layout);
        cart.setVisibility(View.VISIBLE);
        cart_count = (TextView) findViewById(R.id.cart_count);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Constants.cartListBeenArray.size()>0) {

                    startActivity(new Intent(OfferAndHotDealViewAllActivity.this, CartListActivity.class));
                }
                else {
                    Constants.showSnackBar(OfferAndHotDealViewAllActivity.this,getString(R.string.cart_msg));
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = layoutManager1.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {



progressBar.setVisibility(View.VISIBLE);
                    getProducts(++pageCount);


                }
            }
        });

    }


    void  getProducts(final int pageCount)
    {
        try
        {


            sendJson = new JSONObject();
            sendJson.put("method", "productlist");
            sendJson.put("product_type", getIntent().getStringExtra("type"));
            sendJson.put("city_id", SavePref.get_credential(this,SavePref.city_id));
            sendJson.put("pagination","1");
            sendJson.put("page",pageCount);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if(!Constants.isNetworkAvailable(this))
        {
          /* Snackbar snackbar = Snackbar
                   .make, "Please Connect Internet", Snackbar.LENGTH_LONG);
           snackbar.show();*/
            return;
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        if(pageCount<=1) {
            pDialog.show();
        }
        pDialog.setCancelable(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/product?",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        String jsonString="";//your json string here
                        try
                        {
                            JSONObject jObject= new JSONObject(response).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            while(keys.hasNext())
                            {
                                SubCategoriesAdapterbean subCategoriesAdapterbean=new SubCategoriesAdapterbean();
                                String key = keys.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                subCategoriesAdapterbean.setProduct_id(innerJObject.getString("product_id"));
                                subCategoriesAdapterbean.setProduct_name(innerJObject.getString("product_name"));
                                subCategoriesAdapterbean.setProduct_desc(innerJObject.getString("product_desc"));
                                subCategoriesAdapterbean.setBrand_name(innerJObject.getString("brand_name"));

                                subCategoriesAdapterbean.setCategory_id(innerJObject.getString("category_id"));
                                LinkedHashMap<String,String> cuStringStringHashMap=new LinkedHashMap<>();
                                cuStringStringHashMap.put("About",innerJObject.getString("product_desc"));
                                if(innerJObject.optJSONObject("custom_info") != null)
                                {
                                    JSONObject custominfoobject=innerJObject.optJSONObject("custom_info");
                                    Iterator<String> custominfoobjectkey = custominfoobject.keys();
                                    while(custominfoobjectkey.hasNext()) {
                                        String innercustominfo = custominfoobjectkey.next();                                      cuStringStringHashMap.put(innercustominfo,custominfoobject.getString(innercustominfo));
                                    }
                                }
                                else if(innerJObject.optString("custom_info") != null)
                                {
                                    cuStringStringHashMap.put("Other info",innerJObject.getString("custom_info"));
                                    //Its string, do appropriate operation
                                }
                                subCategoriesAdapterbean.setDescription(cuStringStringHashMap);
                               /*String innerKkey = keys.next();
                               String value = innerJObject.getString(innerKkey);
                               System.out.println("ffffff"+value);
                               Iterator<String> innerKeys = innerJObject.keys();*/
                                ArrayList<SubCategoriesAdapterAttributesBean> subCategoriesAdapterAttributesBeanArrayList
                                        =new ArrayList<>();
                                JSONObject attributeObject= new JSONObject(innerJObject.getString("attribute"));
                                Iterator<String> attributekeys = attributeObject.keys();
                                while( attributekeys.hasNext() )
                                {
                                    SubCategoriesAdapterAttributesBean subCategoriesAdapterAttributesBean=new                                                           SubCategoriesAdapterAttributesBean();
                                    String attributeinnerkey = attributekeys.next();
                                    JSONObject attributeinnerJObject = attributeObject.getJSONObject(attributeinnerkey);
                                    subCategoriesAdapterAttributesBean.setProduct_id(attributeinnerJObject.getString("product_id"));
                                    subCategoriesAdapterAttributesBean.setQuantity(attributeinnerJObject.getString("quantity"));
                                    subCategoriesAdapterAttributesBean.setId(attributeinnerJObject.getString("id"));
                                    subCategoriesAdapterAttributesBean.setAttribute_id(attributeinnerJObject.getString("attribute_id"));
                                    subCategoriesAdapterAttributesBean.setStore_id(attributeinnerJObject.getString("store_id"));
                                    subCategoriesAdapterAttributesBean.setMerchant_id(attributeinnerJObject.getString("merchant_id"));
                                    subCategoriesAdapterAttributesBean.setPrice(attributeinnerJObject.getString("price"));
                                    subCategoriesAdapterAttributesBean.setStock(attributeinnerJObject.getString("stock"));
                                    subCategoriesAdapterAttributesBean.setDiscount_type(attributeinnerJObject.getString("discount_type"));
                                    subCategoriesAdapterAttributesBean.setDiscount_value(attributeinnerJObject.getString("discount_value"));
                                    subCategoriesAdapterAttributesBean.setUnit(attributeinnerJObject.getString("unit"));
                                    subCategoriesAdapterAttributesBean.setAttribute_name(attributeinnerJObject.getString("attribute_name"));
                                    subCategoriesAdapterAttributesBeanArrayList.add(subCategoriesAdapterAttributesBean);
                                }
                                subCategoriesAdapterbean.setImage_root_path(new JSONObject(response).getString("imageRootPath"));
try{
                                JSONObject attributeObjectimage= new JSONObject(response).getJSONObject("productImageData");

                                //JSONObject attributeObjectimage= new JSONObject(innerJObject.getString("productImageData"));

                                Iterator<String> imagekeys = attributeObjectimage.keys();
                                while(imagekeys.hasNext()) {
                                    String imgkey = imagekeys.next();
                                    JSONArray imageinnerJObject = attributeObjectimage.getJSONArray(imgkey);
                                    if (imageinnerJObject.getJSONObject(0).getString("image_id").equalsIgnoreCase(innerJObject.getString("product_id"))) {

                                        subCategoriesAdapterbean.setImage_id(imageinnerJObject.getJSONObject(0).getString("type")+"/"+imageinnerJObject.getJSONObject(0).getString("image_id"));
                                        subCategoriesAdapterbean.setImage_name(imageinnerJObject.getJSONObject(0).getString("image_name"));

                                    }
                                }



                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            try {
                                JSONObject nutritionObjectimage = new JSONObject(response).getJSONObject("nutritionImageData");
                                //JSONObject attributeObjectimage= new JSONObject(innerJObject.getString("productImageData"));
                                Iterator<String> nutritionImageimagekeys = nutritionObjectimage.keys();
                                while (nutritionImageimagekeys.hasNext())
                                {
                                    String imgkey = nutritionImageimagekeys.next();
                                    JSONArray imageinnerJObject = nutritionObjectimage.getJSONArray(imgkey);
                                    if (imgkey.equalsIgnoreCase(innerJObject.getString("product_id")))
                                    {
                                        subCategoriesAdapterbean.setNutrition_image(new JSONObject(response).getString("imageRootPath")+"/"+imageinnerJObject.getJSONObject(0).getString("type") + "/" +imageinnerJObject.getJSONObject(0).getString("image_id")+"/"+imageinnerJObject.getJSONObject(0).getString("image_name"));
                                        //  subCategoriesAdapterbean.setImage_name(imageinnerJObject.getJSONObject(0).getString("image_name"));
                                    }
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                                subCategoriesAdapterbean.setSubCategoriesAdapterAttributesBeanArrayList                     (subCategoriesAdapterAttributesBeanArrayList);
                                subCategoriesAdapterbeanArrayList.add(subCategoriesAdapterbean);
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                        adapter.notifyDataSetChanged();
                        pDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(OfferAndHotDealViewAllActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(OfferAndHotDealViewAllActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(OfferAndHotDealViewAllActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(OfferAndHotDealViewAllActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(OfferAndHotDealViewAllActivity.this,"Parse Error!");
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+sendJson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }
}