package com.afrobaskets.App.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afrobaskets.App.adapter.ShopByCategoriesFragmentAdapter;
import com.afrobaskets.App.bean.CategoriesBean;
import com.afrobaskets.App.bean.ShopByCategoriesFragmentsBean;
import com.afrobaskets.App.bean.ShopByCategoriesFragmentsBeanImage;
import com.afrobaskets.App.constant.Constants;
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
import java.util.List;
import java.util.Map;

/**
 * Created by hh on 13-Nov-17.
 */

public class ShopByCategoriesFragments extends android.support.v4.app.Fragment {

    private RecyclerView new_arrival_recyclerView,hot_deal_recyclerView,recyclerView;

    private List<CategoriesBean> foodList = new ArrayList<>();
JSONObject manJson;
    ShopByCategoriesFragmentAdapter fAdapter;
    ProgressBar mprogressBar;

    ProgressDialog pDialog;
    ArrayList<ShopByCategoriesFragmentsBean>shopByCategoriesFragmentsBeanArrayList=new ArrayList<>();
    ArrayList<ShopByCategoriesFragmentsBeanImage>shopByCategoriesFragmentsBeanimageArrayList=new ArrayList<>();

    public ShopByCategoriesFragments() {
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
        View view=inflater.inflate(R.layout.shop_by_categories_fragments, container, false);
        recyclerView = (RecyclerView)view. findViewById(R.id.categories_recycler_view);
        mprogressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        getCategories();
       // cd();
        return  view;
    }


    private void getCategories()
    {

        if(!Constants.isNetworkAvailable(getActivity()))
        {
            Snackbar snackbar = Snackbar
                    .make(recyclerView, "Please Connect Internet", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
       /* pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);*/

        try {
            manJson = new JSONObject();
            manJson.put("method", "categoryList");
            manJson.put("parent_category_list", "1");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(""+response);
                        shopByCategoriesFragmentsBeanArrayList.clear();
                        try {
                            JSONObject jObject = new JSONObject(response).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            while (keys.hasNext()) {
                                ShopByCategoriesFragmentsBean shopByCategoriesFragmentsBean = new ShopByCategoriesFragmentsBean();
                                String key = keys.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                shopByCategoriesFragmentsBean.setId(innerJObject.getString("id"));
                                shopByCategoriesFragmentsBean.setCategory_name(innerJObject.getString("category_name"));
                                shopByCategoriesFragmentsBean.setParent_category_id(innerJObject.getString("parent_category_id"));
                                shopByCategoriesFragmentsBean.setCategory_des(innerJObject.getString("category_des"));
                                shopByCategoriesFragmentsBean.setImage_root_path(new JSONObject(response).getString("imageRootPath"));
                                try {
                                    JSONObject attributeObjectimage = new JSONObject(response).getJSONObject("images");

                                    //JSONObject attributeObjectimage= new JSONObject(innerJObject.getString("productImageData"));

                                    Iterator<String> imagekeys = attributeObjectimage.keys();
                                    while (imagekeys.hasNext()) {
                                        String imgkey = imagekeys.next();
                                        JSONArray imageinnerJObject = attributeObjectimage.getJSONArray(imgkey);
                                        if (imageinnerJObject.getJSONObject(0).getString("image_id").equalsIgnoreCase(innerJObject.getString("id"))) {
                                            // subCategoriesAdapterbean.setImage_id(imageinnerJObject.getString("30"));
                                            //subCategoriesAdapterbean.setImage_name(imageinnerJObject.getString("1_1511792144.png"));
                                            shopByCategoriesFragmentsBean.setImage_id(imageinnerJObject.getJSONObject(0).getString("type") + "/" + imageinnerJObject.getJSONObject(0).getString("image_id"));
                                            shopByCategoriesFragmentsBean.setImage_name(imageinnerJObject.getJSONObject(0).getString("image_name"));
                                        }

                                    }
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                shopByCategoriesFragmentsBeanArrayList.add(shopByCategoriesFragmentsBean);
                            }



                           /* JSONObject jObjects = new JSONObject(response).getJSONObject("images");
                            Iterator<String> key1 = jObjects.keys();
                            shopByCategoriesFragmentsBeanimageArrayList.clear();
                            while (key1.hasNext()) {
                                ShopByCategoriesFragmentsBeanImage shopByCategoriesFragmentsBeanImage = new                  ShopByCategoriesFragmentsBeanImage();
                                String key = key1.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                shopByCategoriesFragmentsBeanImage.setId(innerJObject.getString("image_id"));
                                shopByCategoriesFragmentsBeanImage.setImage_name(innerJObject.getString("image_name"));
                                shopByCategoriesFragmentsBeanimageArrayList.add(shopByCategoriesFragmentsBeanImage);
                            }*/
                            fAdapter = new ShopByCategoriesFragmentAdapter(getActivity(),shopByCategoriesFragmentsBeanArrayList, shopByCategoriesFragmentsBeanimageArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(fAdapter);
                            mprogressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        catch (JSONException e)
                        {   e.printStackTrace();    }
                      //  pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                params.put("parameters",manJson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+manJson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }
    }

