package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.afrobaskets.App.adapter.OrderSummaryAdapter;
import com.afrobaskets.App.bean.CartBean;
import com.afrobaskets.App.bean.ViewAndTrackOrderBeans;
import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.constant.SavePref;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.OrdersummaryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP-PC on 11/20/2017.
 */

public class OrderSummeryActivity extends AppCompatActivity {
    private List<CartBean> foodList = new ArrayList<>();
    private OrderSummaryAdapter fAdapter;
    JSONObject jsonObject;
ProgressDialog progressDialog;
    String order_id;
    OrdersummaryBinding ordersummaryBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         ordersummaryBinding = DataBindingUtil.setContentView(this, R.layout.ordersummary);
        System.out.println("fffff"+getIntent().getStringExtra("data"));
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Gson gson = new Gson();
        Type type = new TypeToken<List<ViewAndTrackOrderBeans>>() {}.getType();
        List<ViewAndTrackOrderBeans> viewAndTrackOrderBeansList = gson.fromJson(getIntent().getStringExtra("data"), type);
        int position=Integer.parseInt(getIntent().getStringExtra("position"));
        ordersummaryBinding.orderId.setText(viewAndTrackOrderBeansList.get(position).getOrder_id());
        ordersummaryBinding.date.setText(viewAndTrackOrderBeansList.get(position).getUpdated_date());
        ordersummaryBinding.status.setText(viewAndTrackOrderBeansList.get(position).getOrder_status());
        ordersummaryBinding.ammount.setText("GHC "+viewAndTrackOrderBeansList.get(position).getPayable_amount());
       ordersummaryBinding.timeSlots.setText(viewAndTrackOrderBeansList.get(position).getDelivery_date());
        ordersummaryBinding.items.setText(viewAndTrackOrderBeansList.get(position).getViewAndTrackItemOrderBeanArrayList().size()+" Items");
        if(viewAndTrackOrderBeansList.get(position).getOrder_status().equalsIgnoreCase("order_placed"))
        {
            ordersummaryBinding.cancelAction.setVisibility(View.VISIBLE);
        }
        else {
            ordersummaryBinding.cancelAction.setVisibility(View.GONE);
        }
        order_id=viewAndTrackOrderBeansList.get(position).getOrder_id();
        ordersummaryBinding.shipping.setText("GHC "+viewAndTrackOrderBeansList.get(position).getShipping_charge());
        ordersummaryBinding.tax.setText("GHC "+viewAndTrackOrderBeansList.get(position).getTax_amount());
        ordersummaryBinding.otherTotal.setText("GHC "+Integer.parseInt(viewAndTrackOrderBeansList.get(position)
        .getPayable_amount()));
        ordersummaryBinding.promo.setText("GHC "+viewAndTrackOrderBeansList.get(position).getDiscount_amount());
        fAdapter = new OrderSummaryAdapter(OrderSummeryActivity.this, viewAndTrackOrderBeansList.get(position).getViewAndTrackItemOrderBeanArrayList(),viewAndTrackOrderBeansList.get(position).getImageRootPath());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ordersummaryBinding.recyclerView.setLayoutManager(mLayoutManager);
        ordersummaryBinding. recyclerView.setItemAnimator(new DefaultItemAnimator());
        ordersummaryBinding.recyclerView.setAdapter(fAdapter);
ordersummaryBinding.cancelAction.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Dialogs();
    }
});
     }
    void Dialogs()
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(OrderSummeryActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(OrderSummeryActivity.this);
        }
        builder.setTitle("Cancel Order")
                .setMessage("Do you want to cancel your order?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        cancelOrder();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    private void cancelOrder()
    {
         jsonObject=new JSONObject();
        try {
            jsonObject.put("method","updateOrderstatus");

            jsonObject.put("user_id", SavePref.getPref(OrderSummeryActivity.this,SavePref.User_id));
            jsonObject.put("order_id", order_id);
            jsonObject.put("order_status","cancelled");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(OrderSummeryActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer?", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("ProfileResopnse"+response);
try {


    JSONObject jsonObject = new JSONObject(response);
    if (jsonObject.getString("status").equals("success")) {
        Constants.showSnackBar(OrderSummeryActivity.this,"Order Cancelled Successfully");
        ordersummaryBinding.cancelAction.setVisibility(View.GONE);
    }
    else
    {
        Constants.showSnackBar(OrderSummeryActivity.this,"Server error");
    }
    progressDialog.dismiss();
}catch (Exception e)
{
    e.printStackTrace();
}

                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error" + error.getMessage());
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(OrderSummeryActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(OrderSummeryActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(OrderSummeryActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(OrderSummeryActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(OrderSummeryActivity.this,"Parse Error!");
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("parameters",jsonObject.toString());
                System.out.println("Parameters"+" "+params);
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+jsonObject.toString()));

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        sr.setShouldCache(false);
        requestQueue.add(sr);
    }

}