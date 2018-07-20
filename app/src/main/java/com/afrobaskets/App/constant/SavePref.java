package com.afrobaskets.App.constant;

import android.content.Context;
import android.content.SharedPreferences;


public class SavePref {

    public static final String is_loogedin = "is_login";
    public static final String is_City_Selected = "is_city_selected";
    public static final String Email="email";
    public static final String Password="password";
    public static final String Mobile="mobile";
    public static final String Name="name";
    public static final String User_id="user_id";
    public static final String City_name="city_name";
    public static final String city_id="city_id";
    public static final String city_locality="city_locality";
    public static final String current_lat="lats";
    public static final String current_lon="lons";
    public static void saveStringPref(Context context, String key, String value) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void save_credential(Context context, String key, String value) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_credential", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public  static String get_credential(Context context, String key) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_credential", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "1");
    }

    public static String getPref(Context context, String key) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "0");
    }

    public  static boolean removePref(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        return editor.commit();
    }
}