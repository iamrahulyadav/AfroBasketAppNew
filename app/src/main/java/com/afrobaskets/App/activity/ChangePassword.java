package com.afrobaskets.App.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

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
import com.webistrasoft.org.ecommerce.databinding.ChangepasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP-PC on 11/19/2017.
 */

public class ChangePassword extends AppCompatActivity {

    ChangepasswordBinding changepasswordBinding;
ProgressDialog pDialog;
    JSONObject sendJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changepasswordBinding = DataBindingUtil.setContentView(this, R.layout.changepassword);
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changepasswordBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

    }

    private void changePassword()
    {

        String old_pass = changepasswordBinding.oldPass.getText().toString();
        String new_pass = changepasswordBinding.newPass.getText().toString();
        String confirm_pass = changepasswordBinding.confirmPass.getText().toString();

        if (!Constants.isEmpty(old_pass))
       {
            changepasswordBinding.oldPass.setError("Enter password");
            return;
        }

        if (!Constants.isEmpty(new_pass))
        {
            changepasswordBinding.newPass.setError("Enter password");
            return;
        }

        if (!Constants.isEmpty(confirm_pass)) {
            changepasswordBinding.confirmPass.setError("Enter password");
            return;
        }

        if(!new_pass.equals(confirm_pass))
       {
           changepasswordBinding.confirmPass.setError("confirm  Password not matched");
           return;

       }

       String pass=SavePref.get_credential(ChangePassword.this,SavePref.Password);

        if(!pass.equals(old_pass))
        {
            changepasswordBinding.oldPass.setError("Old password incorrect");
            return;
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        http://54.233.182.212/basketapi/application/customer?parameters=%7B%22method%22%3A%22changepassword%22%2C%22password%22%3A%2212345%22%2C%22new_password%22%3A%22123%22%2C%22user_id%22%3A1%7D

        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "changepassword");
            sendJson.put("password",changepasswordBinding.oldPass.getText().toString());
            sendJson.put("new_password", changepasswordBinding.newPass.getText().toString());
            sendJson.put("user_id", SavePref.getPref(ChangePassword.this,SavePref.User_id));
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
                      String jsonString="";//your json string here
                        try{
                            JSONObject jObject= new JSONObject(response);
                            if(jObject.getString("status").equalsIgnoreCase("success"))
                            {


                                SavePref.save_credential(ChangePassword.this,SavePref.is_loogedin,"false");                                SavePref.removePref(ChangePassword.this);


                                Intent intent=new Intent(ChangePassword.this,LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                    return;




                            }
                            else
                            {

                                Constants.showSnackBar(ChangePassword.this,jObject.getString("msg"));
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
                    Constants.showSnackBar(ChangePassword.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(ChangePassword.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(ChangePassword.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(ChangePassword.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(ChangePassword.this,"Parse Error!");
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