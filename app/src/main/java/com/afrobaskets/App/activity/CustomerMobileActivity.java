package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.afrobaskets.App.adapter.CountryCodeAdapter;
import com.afrobaskets.App.bean.CountryCodeModel;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomerMobileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText edt_mobile;
    String str1;
    ProgressDialog pDialog;
    static CustomerMobileActivity customerMobileActivity;
    JSONObject sendJson;

    Spinner spin;
    String country_code;
    ArrayList<CountryCodeModel>countryCodeModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutomer_mobile_activity);
        edt_mobile=(EditText)findViewById(R.id.edt_mobile);
        ImageView toolbar_back=(ImageView)findViewById(R.id.toolbar_back);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
         spin = (Spinner) findViewById(R.id.simpleSpinner);
        spin.setOnItemSelectedListener(this);
        setCountryCode();

        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btn_submit=(Button)findViewById(R.id.btn_submit);
 customerMobileActivity=this;
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                str1=edt_mobile.getText().toString().trim();
                if (str1.length()<5){
                    edt_mobile.setError("Invalid Mobile Number!");
                    edt_mobile.setFocusable(true);
                }
                else
                    {

      setMobile();

                }
            }
        });
    }

    //Performing action onItemSelected and onNothing selected

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        country_code=countryCodeModels.get(position).getCountryCode();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void setMobile (){

     /*   if(!Constants.isNetworkAvailable(this))
        {
            Snackbar snackbar = Snackbar
                    .make(loginActivityBinding.btnLogin,getString(R.string.connectio_error), Snackbar.LENGTH_LONG);
            snackbar.show();
            Constants.showSnackBar(LoginActivity.this,getString(R.string.connectio_error));
            return;
        }*/

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "generateotp");
            sendJson.put("user_id",SavePref.getPref(CustomerMobileActivity.this,SavePref.User_id));
            sendJson.put("otp_type","register");
            sendJson.put("country_code",country_code);
            sendJson.put("mobile_number",edt_mobile.getText().toString());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObject= new JSONObject(response);
                            if(jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                Intent intent=new Intent(CustomerMobileActivity.this,OtpActivity.class);
                                intent.putExtra(
                                        "mobile",edt_mobile.getText().toString());
                                intent.putExtra(
                                        "country_code",country_code);
                                startActivity(intent);
                                  }
                            else
                            {

                                Constants.showSnackBar(CustomerMobileActivity.this,jObject.getString("msg"));
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(CustomerMobileActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(CustomerMobileActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(CustomerMobileActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {

                    Constants.showSnackBar(CustomerMobileActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(CustomerMobileActivity.this,"Parse Error!");
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
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }



    private void setCountryCode() {
         /*   if(!Constants.isNetworkAvailable(this))
        {
            Snackbar snackbar = Snackbar
                    .make(loginActivityBinding.btnLogin,getString(R.string.connectio_error), Snackbar.LENGTH_LONG);
            snackbar.show();
            Constants.showSnackBar(LoginActivity.this,getString(R.string.connectio_error));
            return;
        }*/

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "countryList");
              }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObjects= new JSONObject(response);
                            if(jObjects.getString("status").equalsIgnoreCase("success")) {
                                JSONObject jObject = new JSONObject(response).getJSONObject("data");
                                Iterator<String> keys = jObject.keys();

                                while (keys.hasNext()) {
                                    SubCategoriesAdapterbean subCategoriesAdapterbean = new SubCategoriesAdapterbean();
                                    String key = keys.next();
                                    JSONObject innerJObject = jObject.getJSONObject(key);
                                    CountryCodeModel countryCodeModel=new CountryCodeModel();
                                    countryCodeModel.setCountryCode(innerJObject.getString("country_phone_code"));
                                    countryCodeModels.add(countryCodeModel);
                                }

                                CountryCodeAdapter customAdapter=new CountryCodeAdapter(getApplicationContext(),countryCodeModels);
                                spin.setAdapter(customAdapter);
                                for (int i=0;i<countryCodeModels.size();i++){
                                    if (countryCodeModels.get(i).getCountryCode().toString().equalsIgnoreCase("+233")){
                                        spin.setSelection(i);
                                    }
                                }
                            }
                            else
                            {

                                Constants.showSnackBar(CustomerMobileActivity.this,"Server  Error");
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(CustomerMobileActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(CustomerMobileActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(CustomerMobileActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(CustomerMobileActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(CustomerMobileActivity.this,"Parse Error!");
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
        /*

        RequestQueue queue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        final String url = "https://restcountries.eu/rest/v2/all";


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            // jsonString is a string variable that holds the JSON
                            JSONArray itemArray=new JSONArray(response);
                            for (int i = 0; i < itemArray.length(); i++) {
                                JSONObject jsonObject=itemArray.getJSONObject(i);
                                CountryCodeModel countryCodeModel=new CountryCodeModel();
                                countryCodeModel.setUrl(jsonObject.getString("flag"));
                                JSONArray jsonArray=jsonObject.getJSONArray("callingCodes");
                                countryCodeModel.setCountryCode(jsonArray.getString(0));
countryCodeModels.add(countryCodeModel);
                            }
pDialog.dismiss();
                            CountryCodeAdapter customAdapter=new CountryCodeAdapter(getApplicationContext(),countryCodeModels);
                            spin.setAdapter(customAdapter);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(),"dd",Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }*/
    }
}


