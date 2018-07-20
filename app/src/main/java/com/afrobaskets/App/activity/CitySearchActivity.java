package com.afrobaskets.App.activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.afrobaskets.App.adapter.CitySearchAdapter;
import com.afrobaskets.App.bean.CityListBeans;
import com.afrobaskets.App.constant.Constants;
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
import java.util.List;
import java.util.Map;
/**
 * Created by HP-PC on 11/25/2017.
 */


public class CitySearchActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    private RecyclerView mRecyclerView;
    public EditText search;
    ArrayList<CityListBeans> cityArrayList=new ArrayList<>();
    private List<String> list = new ArrayList<String>();
    public CitySearchAdapter mAdapter;
    JSONObject manJson;

   @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citysearchactivity);

        ImageView back = (ImageView) findViewById(R.id.toolbar_back);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        search = (EditText) findViewById(R.id.search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       // countryList();  // in this method, Create a list of items.
        // call the adapter with argument list of items and context.
        addTextListener();
        getCity();
        //get();
   }

   private void getCity()
    {
        if(!Constants.isNetworkAvailable(this))
        {
            Snackbar snackbar = Snackbar
                    .make(search, "Please Connect Internet", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        try {
            manJson = new JSONObject();
            manJson.put("method", "cityList");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject Object= new JSONObject(response);
                            if(Object.getString("status").equalsIgnoreCase("success")) {
                                JSONObject jObject = new JSONObject(response).getJSONObject("data");
                                Iterator<String> keys = jObject.keys();
                                while (keys.hasNext()) {
                                    CityListBeans cityListBeans = new CityListBeans();
                                    String key = keys.next();
                                    JSONObject innerJObject = jObject.getJSONObject(key);
                                    cityListBeans.setId(innerJObject.getString("id"));
                                    cityListBeans.setCity_name(innerJObject.getString("city_name"));
                                    cityListBeans.setCountry_id(innerJObject.getString("country_id"));
                                    cityArrayList.add(cityListBeans);
                                }

                                mAdapter = new CitySearchAdapter(CitySearchActivity.this, cityArrayList);
                                mRecyclerView.setAdapter(mAdapter);
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
                    Constants.showSnackBar(CitySearchActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(CitySearchActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(CitySearchActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(CitySearchActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(CitySearchActivity.this,"Parse Error!");
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
              params.put("parameters",manJson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+manJson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }
    public void addTextListener(){

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                 ArrayList<CityListBeans> filteredList = new ArrayList<>();

                for (int i = 0; i < cityArrayList.size(); i++) {

                    final String text = cityArrayList.get(i).getCity_name().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(cityArrayList.get(i));
                    }
                }
                mRecyclerView.setLayoutManager(new LinearLayoutManager(CitySearchActivity.this));
                mAdapter = new CitySearchAdapter(CitySearchActivity.this,filteredList);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}