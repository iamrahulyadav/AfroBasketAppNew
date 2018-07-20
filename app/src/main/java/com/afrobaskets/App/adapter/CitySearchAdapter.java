package com.afrobaskets.App.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobaskets.App.activity.CategoriesActivity;
import com.afrobaskets.App.activity.CityListActivity;
import com.afrobaskets.App.bean.CityListBeans;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP-PC on 11/25/2017.
 */

public class CitySearchAdapter  extends
        RecyclerView.Adapter<CitySearchAdapter.MyViewHolder> {

    public Context mcontext;
    ArrayList<CityListBeans> cityArrayList;
ProgressDialog pDialog;
    JSONObject sendJson;
    public CitySearchAdapter(Context context,ArrayList<CityListBeans> cityArrayList) {

        this.cityArrayList=cityArrayList;
        mcontext = context;

    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public CitySearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.citysearchadapter, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position ) {

        viewHolder.city_name.setText(cityArrayList.get(position).getCity_name());

        viewHolder.city_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                     if(((Activity) mcontext).getIntent().getStringExtra("type").equalsIgnoreCase("splash"))
                {
                    SavePref.save_credential(mcontext,SavePref.city_id,cityArrayList.get(position).getId());
                    SavePref.save_credential(mcontext,SavePref.City_name,cityArrayList.get(position).getCity_name());
                    SavePref.save_credential(mcontext,SavePref.is_City_Selected,"true");
                    mcontext.startActivity(new Intent(mcontext,CategoriesActivity.class));
                    ((Activity) mcontext).finish();
                    CityListActivity.citylistActivity.finish();

                }
               else {
                         Dialogs(position);
                     }

                         }
        });
    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView city_name;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            city_name= (TextView) itemLayoutView.findViewById(R.id.city_name);

        }
    }
    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return cityArrayList.size();
    }
    void Dialogs(final int position)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mcontext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mcontext);
        }
        builder.setTitle("Change City")
                .setMessage("Change city will delete your added items?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                       updateCart(position);

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



    private void updateCart(final int position)
    {
        pDialog = new ProgressDialog(mcontext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "addtocart");

            if(SavePref.get_credential(mcontext,SavePref.is_loogedin).equalsIgnoreCase("true"))
            {

                sendJson.put("user_id", SavePref.getPref(mcontext, SavePref.User_id));
            }
            else
            {
                sendJson.put("guest_user_id",Constants.device_id);

            }
            sendJson.put("number_of_item","1");
            sendJson.put("merchant_inventry_id", "");
            sendJson.put("action", "clearcart");
            sendJson.put("item_name", "");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(mcontext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(""+response);
                        try {
                            JSONObject Object = new JSONObject(response);
                            if (Object.getString("status").equalsIgnoreCase("success")) {
                                SavePref.save_credential(mcontext,SavePref.city_id,cityArrayList.get(position).getId());

                                SavePref.save_credential(mcontext,SavePref.City_name,cityArrayList.get(position).getCity_name());
                                SavePref.save_credential(mcontext,SavePref.is_City_Selected,"true");

                                Intent i =new Intent(mcontext,CategoriesActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mcontext.startActivity(i);

                            }
                        }catch (Exception e)
                        {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(mcontext, "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(mcontext, "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(mcontext, "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(mcontext, "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(mcontext, "Parse Error!", Toast.LENGTH_SHORT).show();
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