package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.webistrasoft.org.ecommerce.databinding.LoginActivityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP-PC on 11/13/2017.
 */




public class LoginActivity  extends AppCompatActivity {
LoginActivityBinding loginActivityBinding;
    String regId;

    JSONObject sendJson;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        loginActivityBinding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        loginActivityBinding.btnLogin.setOnClickListener(new View.OnClickListener()
        {
    @Override
    public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
           login();

        //startActivity(new Intent(LoginActivity.this, CityListActivity.class));

    }
});

        loginActivityBinding.skip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, CityListActivity.class));
            }
        });

        loginActivityBinding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
            }
        });

        loginActivityBinding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUp_Activity.class));
            }
        });
        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
           if (!SavePref.get_credential(LoginActivity.this,SavePref.Email).equalsIgnoreCase("0")) {
            loginActivityBinding.email.setText(SavePref.get_credential(LoginActivity.this,
                    SavePref.Email));
            loginActivityBinding.txtPassword.setText(SavePref.get_credential(LoginActivity.this,
                    SavePref.Password));
        }
    }

    private void login ()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
         String email = loginActivityBinding.email.getText().toString();
        if (!Constants.isEmpty(email)) {
            loginActivityBinding.email.setError("Invalid Email");
            return;
        }
        final String password = loginActivityBinding.txtPassword.getText().toString();
        if (!Constants.isEmpty(password)) {
            loginActivityBinding.txtPassword.setError("Invalid Password");
            return;
        }
        if(!Constants.isNetworkAvailable(this))
        {
            Snackbar snackbar = Snackbar
                    .make(loginActivityBinding.btnLogin,getString(R.string.connectio_error), Snackbar.LENGTH_LONG);
            snackbar.show();
            Constants.showSnackBar(LoginActivity.this,getString(R.string.connectio_error));
            return;
        }
        if (loginActivityBinding.checkBox.isChecked()) {
            SavePref.save_credential(LoginActivity.this,SavePref.Email,email);
            SavePref.save_credential(LoginActivity.this,SavePref.Password,password);

        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        try {
            sendJson = new JSONObject();
            sendJson.put("method", "login");
            sendJson.put("email",email);
            sendJson.put("password",password);
            sendJson.put("fcm_reg_id",regId);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObject= new JSONObject(response);
                            if(jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                JSONObject Object = jObject.getJSONObject("data");
                                Iterator<String> keys = Object.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                  //  JSONObject innerJObject = jObject.getJSONObject(key);
                                    JSONObject attributeObject= new JSONObject(Object.getString(key));
             SavePref.saveStringPref(LoginActivity.this, SavePref.User_id,attributeObject.getString ("id"));
             SavePref.save_credential(LoginActivity.this, SavePref.is_loogedin,"true");
             SavePref.saveStringPref(LoginActivity.this, SavePref.Name,attributeObject.getString  ("name"));
             SavePref.save_credential(LoginActivity.this, SavePref.Password,password);
                                    SavePref.save_credential(LoginActivity.this, SavePref.Email,attributeObject.getString  ("email"));
                                    SavePref.saveStringPref(LoginActivity.this, SavePref.Mobile,attributeObject.getString  ("mobile_number"));
                                }
                                Constants.updateCart(LoginActivity.this);
                                if(getIntent().hasExtra("type"))

                                {
                                    Intent intent=new Intent(LoginActivity.this,CategoriesActivity.class);
                                                startActivity(intent);
                                    }
                                finish();
                            }
                            else
                            {
                                pDialog.dismiss();
                                Constants.showSnackBar(LoginActivity.this,jObject.getString("msg"));
                            }
                        }
                        catch (JSONException e)
                        {
                            pDialog.dismiss();
                            Constants.showSnackBar(LoginActivity.this,"Login Error");
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(LoginActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(LoginActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(LoginActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(LoginActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(LoginActivity.this,"Parse Error!");
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


