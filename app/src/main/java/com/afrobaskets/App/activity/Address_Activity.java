package com.afrobaskets.App.activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.afrobaskets.App.bean.AddressListBean;
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
import com.webistrasoft.org.ecommerce.databinding.SignupActivityBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hh on 08-Nov-17.
 */

public class Address_Activity extends AppCompatActivity
{
    ProgressDialog progressDialog;
    SignupActivityBinding signupActivityBinding;
    EditText txt_name , txt_city_name, txt_house_number , txt_area ,address_nickname, street_detail , landmark , zipcode ;
    JSONObject jsonObject;
    ArrayList<AddressListBean> addressListBeanArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
       {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaddrerssdelivery);
       // setContentView(R.layout.addaddrerssdelivery);
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
         txt_name = (EditText) findViewById(R.id.txt_name);
         txt_city_name = (EditText) findViewById(R.id.txt_city_name);
          // txt_city_name.setText(SavePref.get_credential(Address_Activity.this,SavePref.City_name));
           txt_house_number = (EditText) findViewById(R.id.txt_house_number);
         txt_area = (EditText) findViewById(R.id.txt_area);
           txt_city_name.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(Address_Activity.this, CityListActivity.class);
                 startActivity(intent);
             }
         });

         address_nickname = (EditText) findViewById(R.id.address_nickname);
         street_detail = (EditText) findViewById(R.id.street_detail);
         landmark = (EditText) findViewById(R.id.landmark);
         zipcode = (EditText) findViewById(R.id.zipcode);
         Button btn_save=(Button)findViewById(R.id.btn_save);
         progressDialog=new ProgressDialog(this);
         if(getIntent().hasExtra("type"))
         {
    String ss = getIntent().getStringExtra("data");
    Gson gson = new Gson();
    Type type = new TypeToken<List<AddressListBean>>() {
    }.getType();
    addressListBeanArrayList=new ArrayList<>();
    addressListBeanArrayList = gson.fromJson(ss, type);
    int position = Integer.parseInt(getIntent().getStringExtra("position"));
    txt_name.setText(""+addressListBeanArrayList.get(position).getContact_name());
  //  txt_city_name.setText(addressListBeanArrayList.get(position).getCity_name());
    txt_house_number.setText(addressListBeanArrayList.get(position).getHouse_number());
    txt_area.setText(addressListBeanArrayList.get(position).getArea());
    address_nickname.setText(addressListBeanArrayList.get(position).getAddress_nickname());
    street_detail.setText(addressListBeanArrayList.get(position).getStreet_detail());
    landmark.setText(addressListBeanArrayList.get(position).getLandmark());
    zipcode.setText(addressListBeanArrayList.get(position).getZipcode());
}
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Constants.isEmpty(txt_name.getText().toString())) {
                    txt_name.setError("Enter  Name");
                    return;
                }

               if (!Constants.isEmpty(txt_city_name.getText().toString())) {
                    txt_city_name.setError("Enter city name");
                    return;
                }
                if (!Constants.isEmpty(txt_house_number.getText().toString())) {
                    txt_house_number.setError("Enter house number");
                    return;
                }
               /* if (!Constants.isEmpty(txt_area.getText().toString())) {
                    txt_area.setError("Enter area");
                    return;
                }*/

                if (!Constants.isEmpty(address_nickname.getText().toString())) {
                    address_nickname.setError("Enter address Nick Name");
                    return;
                }
                if (!Constants.isEmpty(street_detail.getText().toString())) {
                    street_detail.setError("Enter streat");
                    return;
                }
                if (!Constants.isEmpty(landmark.getText().toString()))
                {
                    landmark.setError("Enter landmark");
                    return;
                }
                /*if (!Constants.isEmpty(zipcode.getText().toString())) {
                    zipcode.setError("Enter zip");
                    return;
                }*/
                jsonObject=new JSONObject();
                try
                {
                    jsonObject.put("method","addeditdeliveryaddress");
                    jsonObject.put("contact_name",txt_name.getText().toString());
                    jsonObject.put("city_id", SavePref.get_credential(Address_Activity.this,SavePref.city_id));
                    jsonObject.put("city_name",txt_city_name.getText().toString());
                    jsonObject.put("house_number",txt_house_number.getText().toString());
                    jsonObject.put("area","");
                    jsonObject.put("user_id",SavePref.getPref(Address_Activity.this,SavePref.User_id));
                    jsonObject.put("address_nickname",address_nickname.getText().toString());
                    jsonObject.put("street_detail",street_detail.getText().toString());
                    jsonObject.put("landmark",landmark.getText().toString());
                    jsonObject.put("zipcode",zipcode.getText().toString());

                    if(getIntent().hasExtra("type"))
                     {
    jsonObject.put("id",addressListBeanArrayList.get(Integer.parseInt(getIntent().getStringExtra("position"))).getId());
                }
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                addAddreess();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        txt_city_name.setText(SavePref.get_credential
                (Address_Activity.this, SavePref.city_locality));
    }

    private void addAddreess()
    {
        progressDialog.show();
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(Address_Activity.this);
        StringRequest sr = new StringRequest(Request.Method.POST,
                Constants.BASE_URL+"application/customer?", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                System.out.println("ProfileResopnse"+response);
                try
                {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("success"))
                    {
                        Intent intent=new Intent(Address_Activity.this, AddressList.class);
                        if(getIntent().hasExtra("checkout"))
                        {
                            intent.putExtra("checkout","checkout");
                        }
                        startActivity(intent);
                        AddressList.addressList.finish();
                        finish();
                    }
                    else
                    {
                        Constants.showSnackBar(Address_Activity.this,jsonObject.getString("status"));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener()
                {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error" + error.getMessage());
                progressDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(Address_Activity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(Address_Activity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(Address_Activity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(Address_Activity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(Address_Activity.this,"Parse Error!");
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