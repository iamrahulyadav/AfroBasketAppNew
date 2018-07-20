package com.afrobaskets.App.activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.afrobaskets.App.adapter.ViewAndTrackOrderAdapter;
import com.afrobaskets.App.bean.CartBean;
import com.afrobaskets.App.bean.ViewAndTrackItemOrderBean;
import com.afrobaskets.App.bean.ViewAndTrackOrderBeans;
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
import java.util.List;
import java.util.Map;

/**
 * Created by HP-PC on 11/19/2017.
 */

public class ViewAndTrackOrderActivity extends AppCompatActivity {
    private List<CartBean> foodList = new ArrayList<>();
    private ViewAndTrackOrderAdapter fAdapter;
ProgressDialog pDialog;
    ArrayList<ViewAndTrackItemOrderBean>viewAndTrackItemOrderBeanArrayList;
    ArrayList<ViewAndTrackOrderBeans>viewAndTrackOrderBeansArrayList=new ArrayList<>();
    RecyclerView recyclerView;
    JSONObject sendJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.viewandtrackorderactivity);
         recyclerView=(RecyclerView)findViewById(R.id.recycler_view_subcat);
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getOrder();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    void  getOrder()
    {
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "orderlist");
            sendJson.put("user_id", SavePref.getPref(ViewAndTrackOrderActivity.this,SavePref.User_id));
            sendJson.put("order_status", "");
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
        pDialog.show();
        pDialog.setCancelable(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try {

                            JSONObject jObject = new JSONObject(response).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            while (keys.hasNext()) {
                                viewAndTrackItemOrderBeanArrayList =
                                        new ArrayList<>();
                                viewAndTrackItemOrderBeanArrayList.clear();
                                 String key = keys.next();
                                 JSONObject innerJObject = jObject.getJSONObject(key);
                                    ViewAndTrackOrderBeans viewAndTrackOrderBeans = new ViewAndTrackOrderBeans();
                                    JSONObject order_detailsJObject = innerJObject.getJSONObject("order_details");
                                    viewAndTrackOrderBeans.setId(order_detailsJObject.getString("id"));

                                    viewAndTrackOrderBeans.setUser_id(order_detailsJObject.getString("user_id"));
                                    viewAndTrackOrderBeans.setOrder_id(order_detailsJObject.getString("order_id"));
                                    viewAndTrackOrderBeans.setParent_order_id(order_detailsJObject.getString("parent_order_id"));
                                viewAndTrackOrderBeans.setImageRootPath(new JSONObject(response).getString("imageRootPath"));
                                /*
                                    viewAndTrackOrderBeans.setMerchant_id(order_detailsJObject.getString("merchant_id"));
*/
                                    viewAndTrackOrderBeans.setShipping_address_id(order_detailsJObject.getString("shipping_address_id"));
                                    viewAndTrackOrderBeans.setAmount(order_detailsJObject.getString("amount"));
                                    viewAndTrackOrderBeans.setPayable_amount(order_detailsJObject.getString("payable_amount"));
                                    viewAndTrackOrderBeans.setDiscount_amount(order_detailsJObject.getString("discount_amount"));
                                    viewAndTrackOrderBeans.setCommission_amount(order_detailsJObject.getString("commission_amount"));
                                try
                                {
                                    viewAndTrackOrderBeans.setShipping_charge(order_detailsJObject.getString("shipping_charge"));
                                }catch (Exception e)
                                {
                                    viewAndTrackOrderBeans.setShipping_charge("0");
                                }
                                    viewAndTrackOrderBeans.setTax_amount(order_detailsJObject.getString("tax_amount"));                                     viewAndTrackOrderBeans.setPayment_status(order_detailsJObject.getString("payment_status"));
                                    viewAndTrackOrderBeans.setTime_slots(order_detailsJObject.getString("time_slot_id"));

                                    viewAndTrackOrderBeans.setOrder_status(order_detailsJObject.getString("order_status"));
                                viewAndTrackOrderBeans.setDelivery_date(order_detailsJObject.getString("delivery_date"));


                                    viewAndTrackOrderBeans.setCreated_date(order_detailsJObject.getString("created_date"));
                                    viewAndTrackOrderBeans.setUpdated_date(order_detailsJObject.getString("updated_date"));

                                    JSONObject orderitem_JObject = innerJObject.getJSONObject("orderitem");
                                    Iterator<String> orderitem_keys = orderitem_JObject.keys();

                                while (orderitem_keys.hasNext()) {
                                        String orderitem_key = orderitem_keys.next();
                                        JSONObject orderitemJObject = orderitem_JObject.getJSONObject(orderitem_key);
                                        ViewAndTrackItemOrderBean viewAndTrackItemOrderBean = new ViewAndTrackItemOrderBean();
                                        viewAndTrackItemOrderBean.setId(orderitemJObject.getString("id"));
                                        viewAndTrackItemOrderBean.setOrder_id(orderitemJObject.getString("order_id"));
                                        viewAndTrackItemOrderBean.setMerchant_product_id(orderitemJObject.getString("merchant_product_id"));
                                        viewAndTrackItemOrderBean.setNumber_of_item(orderitemJObject.getString("number_of_item"));

                                        viewAndTrackItemOrderBean.setNumber_of_item(orderitemJObject.getString("number_of_item"));
                                        viewAndTrackItemOrderBean.setAmount(orderitemJObject.getString("amount"));
                                        viewAndTrackItemOrderBean.setCommission_amount(orderitemJObject.getString("commission_amount"));

                                        viewAndTrackItemOrderBean.setDiscount_amount(orderitemJObject.getString("discount_amount"));
                                        viewAndTrackItemOrderBean.setTax_amount(orderitemJObject.getString("tax_amount"));
                                        viewAndTrackItemOrderBean.setStatus(orderitemJObject.getString("status"));
                                        viewAndTrackItemOrderBean.setCreated_by(orderitemJObject.getString("created_by"));
                                        viewAndTrackItemOrderBean.setUpdated_by(orderitemJObject.getString("updated_by"));
                                        JSONObject productdumpJObject = orderitemJObject.getJSONObject("product_dump");
                                        JSONObject productJObject = productdumpJObject.getJSONObject("product_details");
                                        viewAndTrackItemOrderBean.setProduct_name(productJObject.getString("product_name"));
                                        viewAndTrackItemOrderBean.setQuantity(productJObject.getString("quantity"));
                                        viewAndTrackOrderBeans.setMerchant_id(productJObject.getString("merchant_id"));
                                        viewAndTrackItemOrderBean.setUnit(productJObject.getString("unit"));
try {
    JSONArray product_image_Array = productdumpJObject.getJSONArray("product_image_data");
    JSONObject product_image_JObject = product_image_Array.getJSONObject(0);
    viewAndTrackItemOrderBean.setImage_url(product_image_JObject.getString("type") + "/" + product_image_JObject.getString("image_id") + "/" + product_image_JObject.getString("image_name"));
}catch (Exception e)
{
    e.printStackTrace();
}

                                        viewAndTrackItemOrderBeanArrayList.add(viewAndTrackItemOrderBean);
                                    }
                                    viewAndTrackOrderBeans.setViewAndTrackItemOrderBeanArrayList(viewAndTrackItemOrderBeanArrayList);
                                    viewAndTrackOrderBeansArrayList.add(viewAndTrackOrderBeans);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                        fAdapter = new ViewAndTrackOrderAdapter(ViewAndTrackOrderActivity.this,
                        viewAndTrackOrderBeansArrayList,recyclerView);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(fAdapter);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(ViewAndTrackOrderActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(ViewAndTrackOrderActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(ViewAndTrackOrderActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(ViewAndTrackOrderActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(ViewAndTrackOrderActivity.this,"Parse Error!");
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