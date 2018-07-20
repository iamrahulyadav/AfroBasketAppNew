package com.afrobaskets.App.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afrobaskets.App.activity.OfferAndHotDealViewAllActivity;
import com.afrobaskets.App.adapter.HotDealFragmentAdapter;
import com.afrobaskets.App.bean.CategoriesBean;
import com.afrobaskets.App.bean.SubCategoriesAdapterAttributesBean;
import com.afrobaskets.App.bean.SubCategoriesAdapterbean;
import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.constant.SavePref;
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
import java.util.List;
import java.util.Map;

/**
 * Created by hh on 13-Nov-17.
 */

public class OffersAndHotDealFragment extends android.support.v4.app.Fragment {
    private RecyclerView new_arrival_recyclerView,hot_deal_recyclerView,offer_recyclerView;
    private List<CategoriesBean> foodList = new ArrayList<>();
    int hotDealpageCount=1;
    int offerpageCount=1;
    ProgressDialog pDialog;
    JSONObject sendJson=null,sendJsonObjects=null;
    ArrayList<SubCategoriesAdapterbean>HotDealArrayList=new ArrayList<>();
    ArrayList<SubCategoriesAdapterbean>OfferArrayList=new ArrayList<>();
    HotDealFragmentAdapter hotDealFragmentAdapter;
    HotDealFragmentAdapter offerFragmentAdapter;
    public OffersAndHotDealFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view=inflater.inflate(R.layout.offerandhotdealactivity, container, false);

        offer_recyclerView = (RecyclerView)view. findViewById(R.id.offer_recycler_view);
        hot_deal_recyclerView = (RecyclerView)view. findViewById(R.id.hot_deal_recycler_view);
        TextView hot_deal_View = (TextView)view. findViewById(R.id.hotdeal_viewall);
        TextView offer_View = (TextView)view. findViewById(R.id.offer_viewall);
hot_deal_View.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(), OfferAndHotDealViewAllActivity.class);
        intent.putExtra("type","hotdeals");
        startActivity(intent);
    }
});

        offer_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), OfferAndHotDealViewAllActivity.class);
                intent.putExtra("type","new_offer");
                startActivity(intent);

            }
        });
        hotDealFragmentAdapter = new HotDealFragmentAdapter(getActivity(),HotDealArrayList);
        offerFragmentAdapter = new HotDealFragmentAdapter(getActivity(),OfferArrayList);
        final LinearLayoutManager layoutManager1
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        hot_deal_recyclerView.setLayoutManager(layoutManager1);
        hot_deal_recyclerView.setItemAnimator(new DefaultItemAnimator());
        hot_deal_recyclerView.setAdapter(hotDealFragmentAdapter);
        final LinearLayoutManager layoutManager2
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        offer_recyclerView.setLayoutManager(layoutManager2);
        offer_recyclerView.setItemAnimator(new DefaultItemAnimator());
        offer_recyclerView.setAdapter(offerFragmentAdapter);
        getHotDealsProducts(hotDealpageCount);
        getOfferProducts(offerpageCount);
       /* hot_deal_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = layoutManager1.findLastVisibleItemPosition();

                if (lastvisibleitemposition == hotDealFragmentAdapter.getItemCount() - 1) {




                    getHotDealsProducts(hotDealpageCount++);


                }
            }
        });
        offer_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = layoutManager2.findLastVisibleItemPosition();

                if (lastvisibleitemposition == offerFragmentAdapter.getItemCount() - 1) {




                    getOfferProducts(offerpageCount++);


                }
            }
        });*/
        return  view;
    }
    public void refress()
    {
        hotDealFragmentAdapter.notifyDataSetChanged();
offerFragmentAdapter.notifyDataSetChanged();
    }
    void  getHotDealsProducts(final int pageCount)
    {
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "productlist");
            sendJson.put("product_type", "hotdeals");
            sendJson.put("city_id", SavePref.get_credential(getActivity(),SavePref.city_id));
            sendJson.put("pagination","1");
            sendJson.put("page",pageCount);
        }catch (Exception e)
        {
            //{"method":"productlist","product_type":"hotdeals","city_id":"3","pagination":"1"}
            //{"method":"productlist","product_type":"hotdeals","city_id":"3","pagination":"1"}
            e.printStackTrace();
        }

        if(!Constants.isNetworkAvailable(getActivity()))
        {
          /* Snackbar snackbar = Snackbar
                   .make, "Please Connect Internet", Snackbar.LENGTH_LONG);
           snackbar.show();*/
            return;
        }

       /* pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
*/
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                            HotDealArrayList.clear();
                            while(keys.hasNext())
                            {
                                SubCategoriesAdapterbean subCategoriesAdapterbean=new SubCategoriesAdapterbean();
                                String key = keys.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                subCategoriesAdapterbean.setProduct_id(innerJObject.getString("product_id"));
                                subCategoriesAdapterbean.setProduct_name(innerJObject.getString("product_name"));
                                subCategoriesAdapterbean.setBrand_name(innerJObject.getString("brand_name"));
                                subCategoriesAdapterbean.setProduct_desc(innerJObject.getString("product_desc"));
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
                                ArrayList<SubCategoriesAdapterAttributesBean>subCategoriesAdapterAttributesBeanArrayList
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
 subCategoriesAdapterbean.setSubCategoriesAdapterAttributesBeanArrayList                     (subCategoriesAdapterAttributesBeanArrayList);
                                HotDealArrayList.add(subCategoriesAdapterbean);
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                       // pDialog.dismiss();


                                                   hotDealFragmentAdapter.notifyDataSetChanged();


                       // pDialog.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(getActivity(),"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(getActivity(), "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(getActivity(),"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(getActivity(), "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(getActivity(),"Parse Error!");
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





    void  getOfferProducts(final int pageCount)
    {
        try {
            sendJsonObjects = new JSONObject();
            sendJsonObjects.put("method", "productlist");
            sendJsonObjects.put("product_type", "new_offer");
            sendJsonObjects.put("city_id", SavePref.get_credential(getActivity(),SavePref.city_id));
            sendJsonObjects.put("pagination","1");
            sendJsonObjects.put("page",pageCount);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if(!Constants.isNetworkAvailable(getActivity()))
        {
          /* Snackbar snackbar = Snackbar
                   .make, "Please Connect Internet", Snackbar.LENGTH_LONG);
           snackbar.show();*/
            return;
        }

       /* pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
*/
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                                subCategoriesAdapterbean.setBrand_name(innerJObject.getString("brand_name"));
                                subCategoriesAdapterbean.setProduct_desc(innerJObject.getString("product_desc"));
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
                                ArrayList<SubCategoriesAdapterAttributesBean>subCategoriesAdapterAttributesBeanArrayList
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
try {
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
}

catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                try
                                {
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
                                subCategoriesAdapterbean.setSubCategoriesAdapterAttributesBeanArrayList                     (subCategoriesAdapterAttributesBeanArrayList);
                                OfferArrayList.add(subCategoriesAdapterbean);
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        // pDialog.dismiss();


                        offerFragmentAdapter.notifyDataSetChanged();
                        // pDialog.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(getActivity(),"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(getActivity(), "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(getActivity(),"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(getActivity(), "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(getActivity(),"Parse Error!");
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJsonObjects.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+sendJson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }
}
