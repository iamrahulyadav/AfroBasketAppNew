package com.afrobaskets.App.constant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afrobaskets.App.bean.AddressListBean;
import com.afrobaskets.App.bean.CartListBean;
import com.afrobaskets.App.bean.CartListProductDetailsBean;
import com.afrobaskets.App.bean.CartListProductImage;
import com.afrobaskets.App.bean.TimeSlotsBean;
import com.afrobaskets.App.interfaces.CartCallback;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HP-PC on 11/26/2017.
 */
public class Constants {

    //public static String BASE_URL="http://54.233.182.212/basketapi/";
    public static String BASE_URL="http://172.104.239.54/basketapi/";
    static JSONObject sendJson;
    static ProgressDialog pDialog;
    public static ArrayList<CartListBean> cartListBeenArray = new ArrayList<>();
    public static int cartcount = 0;
    public static String today_date =null;
    public static String tomorrow_date =null;
    public static String device_id;
    public static String salt="secure#api$__";
    public static ArrayList<TimeSlotsBean> todaySlotsBeanArrayList = new ArrayList<>();
    public static ArrayList<TimeSlotsBean> tomorrowSlotsBeanArrayList = new ArrayList<>();
//    public static ArrayList<TimeSlotsBean> timeSlotsBeanArrayList = new ArrayList<>();
    public static ArrayList<AddressListBean>addressListBeanArrayList=new ArrayList<>();
public static byte[] hash=null;


    public static String get_SHA_512_SecurePassword(String   salt){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(salt.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println(sb);
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(Constants.EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isEmpty(String etText) {
        if (etText.trim().length() > 0)
            return true;

        return false;
    }
    public static  void showSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void  getCartList(final Context context)
    {
        /*pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        pDialog.setCancelable(false);*/
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "getitemintocart");
            if(SavePref.get_credential(context,SavePref.is_loogedin).equalsIgnoreCase("true"))
            {

                sendJson.put("user_id", SavePref.getPref(context, SavePref.User_id));
            }
            else
            {
                sendJson.put("guest_user_id",device_id);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + "application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString = "";//your json string here
                        cartListBeenArray.clear();
                        cartcount=0;
                        try {

                            JSONObject Object = new JSONObject(response);
                            if (Object.getString("status").equalsIgnoreCase("success")) {
                                cartListBeenArray.clear();
                                JSONObject jObject = new JSONObject(response).getJSONObject("data");
                                Iterator<String> keys = jObject.keys();
                                while (keys.hasNext()) {
                                    CartListBean cartListBean = new CartListBean();
                                   String key = keys.next();
                                    JSONObject innerJObject = jObject.getJSONObject(key);
                                    cartListBean.setId(innerJObject.getString("id"));
                                    cartListBean.setUser_id(innerJObject.getString("user_id"));
                                    cartListBean.setGuest_user_id(innerJObject.getString("guest_user_id"));
                                    cartListBean.setMerchant_inventry_id(innerJObject.getString("merchant_inventry_id"));
                                    cartListBean.setItem_name(innerJObject.getString("item_name"));
                                    cartListBean.setNumber_of_item(innerJObject.getString("number_of_item"));
                                    cartcount++;
                                    cartListBean.setCreated_date(innerJObject.getString("created_date"));
                                    cartListBean.setCreated_date(innerJObject.getString("created_date"));

                                    JSONObject innerObject = new JSONObject(response).getJSONObject("productDetails");
                                    JSONObject jsondataObject = innerObject.getJSONObject("data");
                                    Iterator<String> productDatakeys = jsondataObject.keys();
                                    JSONObject datainnerJObject = null;
                                    ArrayList<CartListProductDetailsBean> cartListProductDetailsBeanArrayList = new ArrayList<>();

                                    while (productDatakeys.hasNext()) {
                                        CartListProductDetailsBean cartListProductDetailsBean = new CartListProductDetailsBean();

                                        String datakey = productDatakeys.next();
                                        if (datakey.equalsIgnoreCase(innerJObject.getString("merchant_inventry_id"))) {
                                            datainnerJObject = jsondataObject.getJSONObject(datakey);
                                            cartListProductDetailsBean.setId(datainnerJObject.getString("id"));
                                            cartListProductDetailsBean.setPrice(datainnerJObject.getString("price"));
                                            cartListProductDetailsBean.setProduct_id(datainnerJObject.getString("product_id"));
                                            cartListProductDetailsBean.setMerchant_id(datainnerJObject.getString("merchant_id"));
                                            cartListProductDetailsBean.setProduct_name(datainnerJObject.getString("product_name"));
                                            cartListProductDetailsBean.setProduct_desc(datainnerJObject.getString("product_desc"));
                                            cartListProductDetailsBean.setDefault_discount_value(datainnerJObject.getString("default_discount_value"));
                                            try
                                            {
                                                cartListProductDetailsBean.setImage_root_path(new JSONObject(response).getString("imageRootPath"));
                                            }catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }

                                            cartListProductDetailsBean.setCategory_id(datainnerJObject.getString("category_id"));
                                            cartListProductDetailsBean.setCustom_info(datainnerJObject.getString("custom_info"));
                                            cartListProductDetailsBean.setCommission_type(datainnerJObject.getString("commission_type"));

                                            cartListProductDetailsBean.setCommission_value(datainnerJObject.getString("commission_value"));

                                            cartListProductDetailsBean.setDiscount_type(datainnerJObject.getString("discount_type"));

                                            cartListProductDetailsBean.setDiscount_value(datainnerJObject.getString("discount_value"));
                                            cartListProductDetailsBeanArrayList.add(cartListProductDetailsBean);
                                            cartListBean.setCartListProductDetailsBeanArrayList(cartListProductDetailsBeanArrayList);
                                        }
                                    }
try
{
                                    JSONObject productImageData = innerObject.getJSONObject("productImageData");
                                    Iterator<String> productImageDatakey = productImageData.keys();
                                    ArrayList<CartListProductImage> cartListProductImageArrayList = new ArrayList<>();
                                    while (productImageDatakey.hasNext()) {
                                        String imagekey = productImageDatakey.next();
                                        if (imagekey.equalsIgnoreCase(datainnerJObject.getString("product_id"))) {
                                            JSONArray innerarray = productImageData.getJSONArray(imagekey);
                                            for (int i = 0; i < innerarray.length(); i++) {
                                                JSONObject innerobj = innerarray.getJSONObject(i);
                                                CartListProductImage cartListProductImage = new CartListProductImage();
                                                cartListProductImage.setId(innerobj.getString("id"));
                                                cartListProductImage.setType(innerobj.getString("type"));
                                                cartListProductImage.setImage_id(innerobj.getString("image_id"));
                                                cartListProductImage.setImage_name(innerobj.getString("image_name"));
                                                cartListProductImageArrayList.add(cartListProductImage);
                                            }
                                            cartListBean.setCartListProductImageArrayList(cartListProductImageArrayList);
                                        }
                                    }}catch (Exception e)
{
    e.printStackTrace();
}
                                    cartListBeenArray.add(cartListBean);

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
if(pDialog!=null)
{
    pDialog.dismiss();

}
                        //((CartListActivity)context).upsateCart("minus");

                        ((CartCallback) context).onSuccess(cartListBeenArray, cartcount); // This will make a callback to activity.

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ccc" + error);
             //   pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error!", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", sendJson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+sendJson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }


    public static void updateCart(final Context context)
    {
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "updatecart");
            sendJson.put("user_id", SavePref.getPref(context, SavePref.User_id));
            sendJson.put("guest_user_id",Constants.device_id);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(""+response);
                       /* try {
                            JSONObject Object = new JSONObject(response);
                            if (Object.getString("status").equalsIgnoreCase("success")) {
                                pDialog.dismiss();
                                finish();
                            }
                            else
                            {
                                Constants.showSnackBar(LoginActivity.this,"Cart Not Updated");

                            }
                        }catch (Exception e)
                        {
                            Constants.showSnackBar(LoginActivity.this,"Cart Not Updated");
                            pDialog.dismiss();
                            e.printStackTrace();
                        }*/

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
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
    public static void updateCart(final Context context, String action, String merchant_inventry_id, String item_name)
    {
        if(!Constants.isNetworkAvailable(context))
        {
            Toast.makeText(context,"Please connect internet",Toast.LENGTH_SHORT).show();
            return;
        }
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "addtocart");

            if(SavePref.get_credential(context,SavePref.is_loogedin).equalsIgnoreCase("true"))
            {

                sendJson.put("user_id", SavePref.getPref(context, SavePref.User_id));
            }
            else
            {
                sendJson.put("guest_user_id",device_id);

            }
            sendJson.put("number_of_item","1");
            sendJson.put("merchant_inventry_id", merchant_inventry_id);
            sendJson.put("action", action);
            sendJson.put("item_name", item_name);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application/customer",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(""+response);
                        try {
                            JSONObject Object = new JSONObject(response);
                            if (Object.getString("status").equalsIgnoreCase("success")) {
                                Constants.getCartList(context);
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
                    Toast.makeText(context, "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error!", Toast.LENGTH_SHORT).show();
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
