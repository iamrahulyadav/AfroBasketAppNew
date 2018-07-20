package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.webistrasoft.org.ecommerce.databinding.SignupActivityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hh on 08-Nov-17.
 */

public class SignUp_Activity extends AppCompatActivity {
SignupActivityBinding signupActivityBinding;
ProgressDialog pDialog;
JSONObject sendJson;

    public static EditText mobile;
    String regId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        signupActivityBinding = DataBindingUtil.setContentView(this, R.layout.signup_activity);
        signupActivityBinding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SignUp();
            }
        });
        ImageView toolbar_back=(ImageView)findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signupActivityBinding.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(SignUp_Activity.this,CategoriesActivity.class));
                SignUp();

            }
        });
        signupActivityBinding.password.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!isValidPassword(signupActivityBinding.password.getText().toString()))
                {
                    signupActivityBinding.password.setError("Password must contains minimum 8 characters at least one Alphabet, one Number and one Special Character");

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
       mobile=(EditText)findViewById(R.id.txt_mobile);
        mobile.setFocusable(false);
        signupActivityBinding.txtCity.setFocusable(false);

        signupActivityBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        signupActivityBinding.txtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp_Activity.this,CityListActivity.class);
                intent.putExtra("type","signup");
                startActivity(intent);

            }
        });

        signupActivityBinding.txtMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp_Activity.this,CustomerMobileActivity.class);
                startActivity(intent);

            }
        });

        if(getIntent().hasExtra("type"))
        {
            signupActivityBinding.btnLogin.setVisibility(View.GONE);
            signupActivityBinding.txtPassword.setVisibility(View.GONE);
            signupActivityBinding.password.setVisibility(View.GONE);
            signupActivityBinding.btnSignup.setText("Save");
            signupActivityBinding.txtEmail.setInputType(InputType.TYPE_NULL);
            signupActivityBinding.txtMobile.setText(SavePref.getPref(SignUp_Activity.this,SavePref.Mobile));
            signupActivityBinding.txtFname.setText(SavePref.getPref(SignUp_Activity.this,SavePref.Name));
            signupActivityBinding.txtCity.setText(SavePref.getPref(SignUp_Activity.this,SavePref.City_name));
            signupActivityBinding.txtPassword.setText(SavePref.get_credential(SignUp_Activity.this,SavePref.Password));
            signupActivityBinding.password.setText(SavePref.get_credential(SignUp_Activity.this,SavePref.Password));
            signupActivityBinding.txtEmail.setText(SavePref.get_credential(SignUp_Activity.this,SavePref.Email));
        }
        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(SavePref.getPref(SignUp_Activity.this, SavePref.City_name).toString().length()>2)
        {
            signupActivityBinding.txtCity.setText(SavePref.getPref(SignUp_Activity.this, SavePref.City_name));
        }

    }
    //*****************************************************************
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    private void SignUp(){

        String fname = signupActivityBinding.txtFname.getText().toString();
        String password = signupActivityBinding.password.getText().toString();

        String email = signupActivityBinding.txtEmail.getText().toString();

        String confirm_password = signupActivityBinding.txtPassword.getText().toString();
        String mobile = signupActivityBinding.txtMobile.getText().toString();


        if(!Constants.isNetworkAvailable(this))
        {
            Snackbar snackbar = Snackbar
                    .make(signupActivityBinding.btnLogin,getString(R.string.connectio_error), Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        String city = signupActivityBinding.txtCity.getText().toString();
        if (!Constants.isEmpty(fname)) {
            signupActivityBinding.txtFname.setError("Enter First Name");
            return;
        }

        if (!Constants.isEmpty(email)) {
            signupActivityBinding.txtEmail.setError("Invalid Email");
            return;
        }
        if (!Constants.isEmpty(password)) {
            signupActivityBinding.password.setError("Enter password");
            return;
        }
        if (!Constants.isEmpty(confirm_password)) {
            signupActivityBinding.txtPassword.setError("Enter Password");
            return;
        }

        if (!Constants.isEmpty(mobile)) {
            signupActivityBinding.txtCity.setError("Invalid Mobile");
            return;
        }

       /* if (!Constants.isEmpty(city)) {
            signupActivityBinding.txtCity.setError("Invalid City");
            return;
        }*/

        if(password.length()<8 &&!isValidPassword(password)){
            signupActivityBinding.txtPassword.setError("Password must contains minimum 8 characters at least one Alphabet, one Number and one Special Character");
            signupActivityBinding.txtPassword.setText("");
            signupActivityBinding.password.setText("");
            return;

        }
        if (!password.equals(confirm_password)) {
            signupActivityBinding.txtPassword.setError("Password not matched");
            signupActivityBinding.txtPassword.setText("");
            signupActivityBinding.password.setText("");
            return;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "addedituser");
            sendJson.put("city_id", SavePref.get_credential(SignUp_Activity.this,SavePref.city_id));
            sendJson.put("password",password);
            sendJson.put("name", fname);
            sendJson.put("email", email);
            sendJson.put("mobile_number", mobile);
            sendJson.put("id", SavePref.getPref(SignUp_Activity.this,SavePref.User_id));
            sendJson.put("fcm_reg_id",regId);

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jObject=null;
                        String jsonString="";//your json string here
                              try{
                             jObject= new JSONObject(response);
                            if(jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                JSONObject Object = jObject.getJSONObject("data");
                                Iterator<String> keys = Object.keys();
                                while (keys.hasNext()) {
                                  String key = keys.next();
                                    Toast.makeText(getApplicationContext(),"Register  Successfully",Toast.LENGTH_LONG).show();

                                    //  JSONObject innerJObject = jObject.getJSONObject(key);
                                    JSONObject attributeObject= new JSONObject(Object.getString(key));

                                 /*   SavePref.saveStringPref(SignUp_Activity.this, SavePref.User_id,attributeObject.getString ("id"));
                                    SavePref.save_credential(SignUp_Activity.this, SavePref.is_loogedin,"true");
                                    SavePref.saveStringPref(SignUp_Activity.this, SavePref.Name,attributeObject.getString  ("name"));
                                    SavePref.save_credential(SignUp_Activity.this, SavePref.Password,signupActivityBinding.password.getText().toString());
                                    SavePref.save_credential(SignUp_Activity.this, SavePref.Email,attributeObject.getString  ("email"));
                                    SavePref.saveStringPref(SignUp_Activity.this, SavePref.Mobile,attributeObject.getString  ("mobile_number"));*/
                                }
                               // Constants.updateCart(SignUp_Activity.this);
                                Intent intent=new Intent(SignUp_Activity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                pDialog.dismiss();
                                Constants.showSnackBar(SignUp_Activity.this,jObject.getString("msg"));
                            }
                        }
                        catch (JSONException e)
                        {
                            pDialog.dismiss();
                            Constants.showSnackBar(SignUp_Activity.this,"SignUp  Error");
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(SignUp_Activity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(SignUp_Activity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(SignUp_Activity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(SignUp_Activity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(SignUp_Activity.this,"Parse Error!");
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