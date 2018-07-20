package com.afrobaskets.App.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.constant.SavePref;
import com.webistrasoft.org.ecommerce.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.splash_screen_activity);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);

                    if (!SavePref.get_credential(SplashScreen.this,SavePref.is_loogedin).equalsIgnoreCase("true"))
                    {
                        Constants.device_id = Settings.Secure.getString(getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                    }
               String ss=SavePref.get_credential(SplashScreen.this,SavePref.is_City_Selected);
                     if (SavePref.get_credential(SplashScreen.this,SavePref.is_City_Selected).equalsIgnoreCase("true"))
                    {
                        startActivity(new Intent(SplashScreen.this, CategoriesActivity.class));

                    }
                    else
                     {
                         Intent intent=new Intent(SplashScreen.this, CityListActivity.class);
                         intent.putExtra("type","splash");
                         startActivity(intent);
                     }
finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timerThread.start();
    }
}

