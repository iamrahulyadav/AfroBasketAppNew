package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobaskets.App.adapter.SearchAdapter;
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
import java.util.Locale;
import java.util.Map;

/**
 * Created by HP-PC on 12/11/2017.
 */

public class SearchActivity extends AppCompatActivity implements CartCallback{
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    JSONObject sendJson=null;
    ArrayList<SubCategoriesAdapterbean>subCategoriesAdapterbeanArrayList=new ArrayList<>();
    SearchAdapter adapter;
    TextView cart_count;
    LinearLayoutManager linearLayoutManager;
    SearchView search;
    int pageCount=1;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    ProgressBar mprogressBar,bottomProgress;
    String searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

        setContentView(R.layout.searchactivity);
        //Constants.getCartList(SearchActivity.this);
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_count.setText(""+Constants.cartListBeenArray.size());
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.cart_layout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
         linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SearchAdapter(SearchActivity.this,subCategoriesAdapterbeanArrayList);
        recyclerView.setAdapter(adapter);
        ImageView voice = (ImageView) findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
        search = (SearchView) findViewById(R.id.searchView);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        bottomProgress=(ProgressBar)findViewById(R.id.progressBarButtoms);
        adapter = new SearchAdapter(SearchActivity.this,subCategoriesAdapterbeanArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {


                    bottomProgress.setVisibility(View.VISIBLE);

                    getProducts(++pageCount,searchText);


                }
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText=newText;
                if (newText.length() > 2) {
                    mprogressBar.setVisibility(View.VISIBLE);
                    if(pageCount==1 && newText.length()>3)
                    {
                        getProducts(++pageCount,searchText);
                      return false;
                    }
pageCount=1;
                    subCategoriesAdapterbeanArrayList.clear();

                    getProducts(pageCount,searchText);
                } else {
                    subCategoriesAdapterbeanArrayList.clear();
                    recyclerView.setVisibility(View.GONE);
                }
                return false;
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recyclerView.setVisibility(View.GONE);
                subCategoriesAdapterbeanArrayList.clear();
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        cart_count = (TextView) findViewById(R.id.cart_count);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.cartListBeenArray.size()>0) {

                    startActivity(new Intent(SearchActivity.this, CartListActivity.class));
                }
                else {
                    Constants.showSnackBar(SearchActivity.this,getString(R.string.cart_msg));
                }
            }
        });
        if (getIntent().getStringExtra("type").equalsIgnoreCase("voice")) {
            startVoiceInput();
        } else {
            search.requestFocus();
        }
    }

    @Override
    public void onSuccess(ArrayList<CartListBean> cartListBeanArrayList, int result) {
        cart_count.setText(""+Constants.cartListBeenArray.size());

    }

    void  getProducts(final int pageCount, String textsearch)
    {
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "productlist");

                sendJson.put("category_name", textsearch);

            sendJson.put("product_name", textsearch);
            sendJson.put("pagination","1");
            sendJson.put("page",pageCount);
            sendJson.put("city_id", SavePref.get_credential(this,SavePref.city_id));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if(!Constants.isNetworkAvailable(this))
        {

            return;
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
       // pDialog.show();
        pDialog.setCancelable(false);
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/product?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObject= new JSONObject(response).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            //subCategoriesAdapterbeanArrayList.clear();
                            if(pageCount<=1)
                            {
                                subCategoriesAdapterbeanArrayList.clear();
                            }
                            while(keys.hasNext())
                            {
                                SubCategoriesAdapterbean subCategoriesAdapterbean=new SubCategoriesAdapterbean();
                                String key = keys.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                subCategoriesAdapterbean.setProduct_id(innerJObject.getString("product_id"));
                                subCategoriesAdapterbean.setProduct_name(innerJObject.getString("product_name"));


                                if(!innerJObject.isNull("bullet_desc"))
                                {

                                    JSONArray jsonArray=innerJObject.getJSONArray("bullet_desc");
                                    String desc="";
                                    for(int i=0;i<jsonArray.length();i++)
                                    {
                                        if(i==0)
                                        {
                                            desc=jsonArray.getString(i);

                                        }
                                        else {
                                            desc=desc+"#"+jsonArray.getString(i);

                                        }
                                    }
                                    subCategoriesAdapterbean.setBullet_desc(desc);                               }


                                subCategoriesAdapterbean.setBrand_name(innerJObject.getString("brand_name"));
                                subCategoriesAdapterbean.setProduct_desc(innerJObject.getString("product_desc"));
                                subCategoriesAdapterbean.setNutrition_name(innerJObject.getString("nutrition"));
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
                                try {


                                    subCategoriesAdapterbean.setImage_root_path(new JSONObject(response).getString("imageRootPath"));

                                    JSONObject attributeObjectimage = new JSONObject(response).getJSONObject("productImageData");

                                    //JSONObject attributeObjectimage= new JSONObject(innerJObject.getString("productImageData"));

                                    Iterator<String> imagekeys = attributeObjectimage.keys();
                                    while (imagekeys.hasNext()) {
                                        String imgkey = imagekeys.next();
                                        JSONArray imageinnerJObject = attributeObjectimage.getJSONArray(imgkey);
                                        if (imageinnerJObject.getJSONObject(0).getString("image_id").equalsIgnoreCase(innerJObject.getString("product_id"))) {

                                            subCategoriesAdapterbean.setImage_id(imageinnerJObject.getJSONObject(0).getString("type") + "/" + imageinnerJObject.getJSONObject(0).getString("image_id"));
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
                                    while (nutritionImageimagekeys.hasNext()) {
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
                                subCategoriesAdapterbean.setSubCategoriesAdapterAttributesBeanArrayList                              (subCategoriesAdapterAttributesBeanArrayList);

                                subCategoriesAdapterbeanArrayList.add(subCategoriesAdapterbean);

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if(subCategoriesAdapterbeanArrayList.size()<=0) {
                                Toast.makeText(getApplicationContext(), "Item Not Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(subCategoriesAdapterbeanArrayList.size()>0 && searchText.length()>2) {
                            adapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                            pDialog.dismiss();
                        mprogressBar.setVisibility(View.GONE);
                        bottomProgress.setVisibility(View.GONE);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
if(subCategoriesAdapterbeanArrayList.size()<=0) {
    Toast.makeText(getApplicationContext(), "Item Not Found", Toast.LENGTH_SHORT).show();
}
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(SearchActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(SearchActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(SearchActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(SearchActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(SearchActivity.this,"Parse Error!");
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

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
               pageCount=1;
                    searchText=result.get(0);

                   getProducts(pageCount,searchText);
                }
                break;
            }

        }
    }
}



