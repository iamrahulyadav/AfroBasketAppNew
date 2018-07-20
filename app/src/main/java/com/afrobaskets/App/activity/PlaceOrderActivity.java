package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobaskets.App.bean.TimeSlotsBean;
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
import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.PlaceorderactivityBinding;
import com.webistrasoft.org.ecommerce.databinding.SignupActivityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP-PC on 12/8/2017.
 */

public class PlaceOrderActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    SignupActivityBinding signupActivityBinding;
    JSONObject jsonObject;
    PlaceorderactivityBinding placeorderactivityBinding;
    String timeslote_id,timeslote,Booking_Date;

     Spinner today_spinner;
int spinner_poosition;
     Spinner tomorrow_spinner;
    void setVarients(Spinner spinner, ArrayList<TimeSlotsBean>arrayList,String day)
    {
        List<String> list = new ArrayList<String>();
        list.add(day);
        for(int i=0;i<arrayList.size();i++)
        {
        list.add(arrayList.get(i)
                    .getStart_time_slot()+":00 to "+arrayList.get(i)
                    .getEnd_time_slot()+":00");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PlaceOrderActivity.this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeorderactivityBinding= DataBindingUtil.setContentView(this, R.layout.placeorderactivity);
        placeorderactivityBinding.cash.setChecked(true);
        String address=Constants.addressListBeanArrayList.get(0).getAddress_nickname()+",\n"+Constants.addressListBeanArrayList.get(0).getHouse_number()+","+Constants.addressListBeanArrayList.get(0).getStreet_detail()+","+Constants.addressListBeanArrayList.get(0).getCity_name();
        placeorderactivityBinding.address.setText(address);
        placeorderactivityBinding.placeorder.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PlaceOrder();
    }
});

        placeorderactivityBinding.cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeorderactivityBinding.cash.setChecked(true);
                placeorderactivityBinding.ezzypay.setChecked(false);
            }
        });

        placeorderactivityBinding.ezzypay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    placeorderactivityBinding.cash.setChecked(false);

                placeorderactivityBinding.ezzypay.setChecked(true);

            }
        });

        placeorderactivityBinding.editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlaceOrderActivity.this,AddressList.class);
                intent.putExtra("checkout","checkout");

                startActivity(intent);
            }
        });
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog=new ProgressDialog(this);
                ReviewOrder();

today_spinner=(Spinner)findViewById(R.id.today_spinner);

tomorrow_spinner=(Spinner)findViewById(R.id.tomorrow_spinner);
if(Constants.todaySlotsBeanArrayList.size()>0)
{
    setVarients(today_spinner,Constants.todaySlotsBeanArrayList,"Today");
}
else
{
    today_spinner.setVisibility(View.GONE);
    TextView view=(TextView) findViewById(R.id.view);
    view.setVisibility(View.GONE);

}

        if(Constants.tomorrowSlotsBeanArrayList.size()>0)
        {
            setVarients(tomorrow_spinner,Constants.tomorrowSlotsBeanArrayList,"Tomorrow");
        }
        else
        {
            tomorrow_spinner.setVisibility(View.GONE);
            TextView view=(TextView) findViewById(R.id.view);
            view.setVisibility(View.GONE);

        }
        today_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated metho
                // d stub
                if(arg2<=0)
                    return;
                timeslote_id=Constants.todaySlotsBeanArrayList.get(arg2-1).getId();
                timeslote=Constants.todaySlotsBeanArrayList.get(arg2-1).getStart_time_slot()+":00 to "+Constants.todaySlotsBeanArrayList.get(arg2-1).getEnd_time_slot()+":00"
                ;
                //Booking_Date=new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault()).format(new Date());

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                Booking_Date = df.format(c);

                try {
                   tomorrow_spinner.setSelection(0);
               }catch (Exception e)
               {
                   e.printStackTrace();
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    tomorrow_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1,
        int arg2, long arg3) {
            // TODO Auto-generated metho
            // d stub
if(arg2<=0)
    return;
            timeslote_id=Constants.tomorrowSlotsBeanArrayList.get(arg2-1).getId();
            timeslote=Constants.tomorrowSlotsBeanArrayList.get(arg2-1).getStart_time_slot()+":00 to "+Constants.tomorrowSlotsBeanArrayList.get(arg2-1).getEnd_time_slot()+":00"
            ;
            /*Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            Booking_Date=new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault()).format(tomorrow);
         */ Date today = new Date();
            Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            Booking_Date = df.format(tomorrow);
            try {
                today_spinner.setSelection(0);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    });
}

    private void PlaceOrder()
    {
        if(!(placeorderactivityBinding.cash.isChecked() || placeorderactivityBinding.ezzypay.isChecked()))
        {
            Toast.makeText(getApplicationContext(),"Please select Payment Option",Toast.LENGTH_SHORT).show();
            return;
        }

        if (today_spinner.getSelectedItemPosition()==0 && tomorrow_spinner.getSelectedItemPosition()==0)
        {

                Toast.makeText(getApplicationContext(), "Please select Delivery Time", Toast.LENGTH_SHORT).show();

        }
        if ((today_spinner.getVisibility() == View.VISIBLE && tomorrow_spinner.getVisibility() != View.VISIBLE )&& today_spinner.getSelectedItemPosition() == 0)
        {
                    Toast.makeText(getApplicationContext(), "Please select Delivery Time", Toast.LENGTH_SHORT).show();
                    return;
        }

        if ((tomorrow_spinner.getVisibility() == View.VISIBLE && today_spinner.getVisibility() != View.VISIBLE )&& tomorrow_spinner.getSelectedItemPosition() == 0)
        {
            Toast.makeText(getApplicationContext(), "Please select Delivery Time", Toast.LENGTH_SHORT).show();
                    return;
        }
        jsonObject=new JSONObject();
        try {
            jsonObject.put("method","placeorder");
            jsonObject.put("user_id", SavePref.getPref(PlaceOrderActivity.this,SavePref.User_id));
            jsonObject.put("shipping_address_id",Constants.addressListBeanArrayList.get(0).getId());
            jsonObject.put("time_slot_id",timeslote_id);
            if (today_spinner.getSelectedItemPosition() != 0)
            {
                jsonObject.put("delivery_date",Constants.today_date);
            }
            else {
                jsonObject.put("delivery_date",Constants.tomorrow_date);
            }
                if(placeorderactivityBinding.cash.isChecked())
            {
                jsonObject.put("payment_type","cash");
            }
            else
            {
                jsonObject.put("payment_type","ezeepay");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(PlaceOrderActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer?", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("ProfileResopnse"+response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){

                        JSONObject Object = jsonObject.getJSONObject("data");
                        if(placeorderactivityBinding.ezzypay.isChecked())
                        {
 // https://endpoint/mobile/checkout?token=5e838160-76b0-435b-9a28-20431e7af8e1&returnurl=[ Return URL] for
/*
                            status: "success",
                                    msg: "order placed successfully.",
                                data: {
                            order_id: "order_m3_132",
                                    tokenResponse: {
                                StatusCode: "200",
                                        TokenId: "219fceca-92f3-4665-9041-f7acda789a57",
                                        Message: "SUCCESS"*/
                    JSONObject innerJsonObject1=Object.getJSONObject("tokenResponse");
                            Intent intent=new Intent(PlaceOrderActivity.this,PaymentActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("order_id",Object.getString("order_id"));
                            intent.putExtra("token_id",innerJsonObject1.getString("TokenId"));
                            intent.putExtra("amount",placeorderactivityBinding.payableAmount.getText().toString());
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            Intent intent=new Intent(PlaceOrderActivity.this,ThankuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("order_id",Object.getString("order_id"));
                            intent.putExtra("amount",placeorderactivityBinding.payableAmount.getText().toString());
                            intent.putExtra("time",timeslote+"\n("+Booking_Date+")");
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error" + error.getMessage());
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(PlaceOrderActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(PlaceOrderActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(PlaceOrderActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(PlaceOrderActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(PlaceOrderActivity.this,"Parse Error!");
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

    private void ReviewOrder() {
        jsonObject=new JSONObject();
        try {
            jsonObject.put("method","checkout");
            jsonObject.put("user_id",SavePref.getPref(PlaceOrderActivity.this,SavePref.User_id));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.show();
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(PlaceOrderActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer?", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("ProfileResopnse"+response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){
                        JSONObject jObject = jsonObject.getJSONObject("data");
                        JSONObject innerObject = jObject.getJSONObject("totalOrderDetails");
                        placeorderactivityBinding.total.setText("GHC "+innerObject.getString("amount"));
                        placeorderactivityBinding.discountAmount.setText("GHC "+innerObject.getString("discount_amount"));
                        placeorderactivityBinding.commissionAmount.setText("GHC "+innerObject.getString("commission_amount"));
                        try {
                            placeorderactivityBinding.shippingAmount.setText("GHC " + innerObject.getString("shipping_charge"));
                        }catch (Exception e)
                        {
                            placeorderactivityBinding.shippingAmount.setText("GHC 0");
                        }
                        placeorderactivityBinding.taxAmount.setText("GHC "+innerObject.getString("tax_amount"));
                        placeorderactivityBinding.payableAmount.setText("GHC "+innerObject.getString("payable_amount"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error" + error.getMessage());
                progressDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(PlaceOrderActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(PlaceOrderActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(PlaceOrderActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(PlaceOrderActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(PlaceOrderActivity.this,"Parse Error!");
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
