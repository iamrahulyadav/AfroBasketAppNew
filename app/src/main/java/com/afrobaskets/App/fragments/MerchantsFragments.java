package com.afrobaskets.App.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afrobaskets.App.adapter.MerchantsFragmentsAdapter;
import com.afrobaskets.App.bean.MerchantsFragmentBean;
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
import java.util.Map;

/**
 * Created by hh on 13-Nov-17.
 */

public class MerchantsFragments extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;
    MerchantsFragmentsAdapter fAdapter;
    ArrayList<MerchantsFragmentBean>merchantsFragmentBeanArrayList=new ArrayList<>();
ProgressDialog pDialog;
    JSONObject sendJson;
    public MerchantsFragments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.merchantsfragments, container, false);
        recyclerView = (RecyclerView)view. findViewById(R.id.offer_recycler_view);
        getMerchent();
        return  view;
    }

    void getMerchent()
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
            sendJson = new JSONObject();
            sendJson.put("method", "getMarchantList");
            sendJson.put("city_id", SavePref.get_credential(getActivity(),SavePref.city_id));
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
                        merchantsFragmentBeanArrayList.clear();
                        try {
                            JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                MerchantsFragmentBean merchantsFragmentBean=new MerchantsFragmentBean();
                                merchantsFragmentBean.setId(jsonObject.getString("id"));
                                merchantsFragmentBean.setFirst_name(jsonObject.getString("first_name"));
                                merchantsFragmentBean.setIc_number(jsonObject.getString("ic_number"));
                                merchantsFragmentBean.setUsername(jsonObject.getString("username"));
                                merchantsFragmentBean.setEmail(jsonObject.getString("email"));
                                merchantsFragmentBean.setPhone_number(jsonObject.getString("phone_number"));
                                merchantsFragmentBean.setAddress(jsonObject.getString("address"));
                                merchantsFragmentBean.setBank_name(jsonObject.getString("bank_name"));
                                merchantsFragmentBean.setBank_account_number(jsonObject.getString("bank_account_number"));
                                merchantsFragmentBean.setStatus(jsonObject.getString("status"));
                                merchantsFragmentBeanArrayList.add(merchantsFragmentBean);
                            }
                        }
                        catch (JSONException e)
                        {   e.printStackTrace();    }
                        fAdapter = new MerchantsFragmentsAdapter(getActivity(),merchantsFragmentBeanArrayList);
                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(fAdapter);
                       // pDialog.dismiss();
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
                params.put("parameters",sendJson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+sendJson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }
    }



