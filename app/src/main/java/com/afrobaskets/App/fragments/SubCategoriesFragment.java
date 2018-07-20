package com.afrobaskets.App.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afrobaskets.App.adapter.SubCategoryAdapter;
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
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by asdfgh on 10/9/2017.
 */

public class SubCategoriesFragment extends Fragment {
    ProgressDialog pDialog;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    JSONObject sendJson=null;
int pageCount=1;
    SubCategoryAdapter adapter;
    ArrayList<SubCategoriesAdapterbean>subCategoriesAdapterbeanArrayList=new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.subcategoriesfragment, null);

        setUpView(root);
        return root;
    }

public void refress()
{
    adapter.notifyDataSetChanged();
}
    void setUpView(ViewGroup root){
        ButterKnife.bind(this, root);
        adapter = new SubCategoryAdapter(getActivity(),subCategoriesAdapterbeanArrayList);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        setUPList();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {


progressBar.setVisibility(View.VISIBLE);

                    getProducts(++pageCount);


                }
            }
        });

    }


    void setUPList() {


                getProducts(pageCount);

    }


   void  getProducts(int pageCount)
   {

      /* http://54.233.182.212/basketapi/application/product?parameters={%22method%22:%22productlist%22,%22city_id%22:%221%22,%22pagination%22:%221%22,%22page%22:%221%22,%22category_id%22:%222%22}*/
       try {
             sendJson = new JSONObject();
           String string = null;
           try {
           if (getArguments().getString("type").equalsIgnoreCase("banner")) {
               sendJson.put("city_id", getArguments().getString("city_id"));
               sendJson.put("method", getArguments().getString("method"));
               sendJson.put("category_id", getArguments().getString("id"));
               sendJson.put("pagination","1");
               sendJson.put("page",pageCount);
           }
           } catch (Exception e) {
               e.printStackTrace();
           }

           try {
               if (getArguments().getString("type").equalsIgnoreCase("merchant")) {
                   sendJson.put("merchant_id", getArguments().getString("id"));
                   sendJson.put("city_id", SavePref.get_credential(getActivity(),SavePref.city_id));
                   sendJson.put("method", "productlist");
                   sendJson.put("pagination","1");
                   sendJson.put("page",pageCount);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }


           try {
               if (getArguments().getString("type").equalsIgnoreCase("category")) {
                   sendJson.put("category_id", getArguments().getString("id"));
                   sendJson.put("city_id", SavePref.get_credential(getActivity(),SavePref.city_id));
                   sendJson.put("method", "productlist");
                   sendJson.put("pagination","1");
                   sendJson.put("page",pageCount);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }catch (Exception e)
       {
           e.printStackTrace();
       }
      /* pDialog = new ProgressDialog(getActivity());
       pDialog.setMessage("Loading...");
       pDialog.show();
       pDialog.setCancelable(false);*/
       RequestQueue queue = Volley.newRequestQueue(getActivity());
       StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/product?",
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
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
                               subCategoriesAdapterbean.setNutrition_name(innerJObject.getString("nutrition"));
                               subCategoriesAdapterbean.setProduct_desc(innerJObject.getString("product_desc"));
                               subCategoriesAdapterbean.setCategory_id(innerJObject.getString("category_id"));
                               LinkedHashMap<String,String>cuStringStringHashMap=new LinkedHashMap<>();
                               cuStringStringHashMap.put("About",innerJObject.getString("product_desc"));
                               if(innerJObject.optJSONObject("custom_info") != null)
                               {
                                   JSONObject custominfoobject=innerJObject.optJSONObject("custom_info");
                                   Iterator<String> custominfoobjectkey = custominfoobject.keys();

                                   while(custominfoobjectkey.hasNext())
                                   {
                                       String innercustominfo = custominfoobjectkey.next();
                     cuStringStringHashMap.put(innercustominfo,custominfoobject.getString(innercustominfo));
                                   }
                               }
                              /* else if(innerJObject.optString("custom_info") != null)
                               {
                                   cuStringStringHashMap.put("Other info",innerJObject.getString("custom_info"));
                                   //Its string, do appropriate operation
                               }*/
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
                               JSONObject attributeObjectimage= new JSONObject(response).getJSONObject("productImageData");

                               //JSONObject attributeObjectimage= new JSONObject(innerJObject.getString("productImageData"));

                               Iterator<String> imagekeys = attributeObjectimage.keys();
                               while(imagekeys.hasNext())
                               {
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

    subCategoriesAdapterbean.setSubCategoriesAdapterAttributesBeanArrayList (subCategoriesAdapterAttributesBeanArrayList);
                               subCategoriesAdapterbeanArrayList.add(subCategoriesAdapterbean);
                           }
                          adapter.notifyDataSetChanged();
                          // pDialog.dismiss();
                       }

                       catch (JSONException e)
                       {
                           //pDialog.dismiss();
                           if(subCategoriesAdapterbeanArrayList.size()==0)
                           Constants.showSnackBar(getActivity(),"Product Not Found!");
                           e.printStackTrace();

                       }
progressBar.setVisibility(View.GONE);

                   }

               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               pDialog.dismiss();
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
   }