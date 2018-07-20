package com.afrobaskets.App.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.constant.SavePref;
import com.webistrasoft.org.ecommerce.R;


public class ProfileActivity extends AppCompatActivity {
RelativeLayout changepassword,logout;
    Button btn_ads;
    ImageView btn_profile;
    TextView name,email,mobile,address;

    @Override
    protected void onResume() {
        super.onResume();
        name.setText(SavePref.getPref(ProfileActivity.this,SavePref.Name));
        mobile.setText(SavePref.getPref(ProfileActivity.this,SavePref.Mobile));
        email.setText(SavePref.get_credential(ProfileActivity.this,SavePref.Email));

        if(Constants.addressListBeanArrayList.size()>0) {
            String addres = Constants.addressListBeanArrayList.get(0).getContact_name() + ",\n" + Constants.addressListBeanArrayList.get(0).getHouse_number() + "," + Constants.addressListBeanArrayList.get(0).getStreet_detail() + "," + Constants.addressListBeanArrayList.get(0).getCity_name();
            address.setText(addres);
            return;
        }
       CardView card_view1=(CardView)findViewById(R.id.card_view1);
        card_view1.setVisibility(View.GONE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        ImageView btn_profile = (ImageView) findViewById(R.id.edit_profile);
         Button btn_ads = (Button) findViewById(R.id.button_ads);
         name=(TextView)findViewById(R.id.name);
         address=(TextView)findViewById(R.id.location);
         changepassword=(RelativeLayout)findViewById(R.id.change_password);
         logout=(RelativeLayout)findViewById(R.id.logout);
         email=(TextView)findViewById(R.id.email);
         mobile=(TextView)findViewById(R.id.mobile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SignUp_Activity.class);
                intent.putExtra("type","edit");
                startActivity(intent);
            }
        });

        btn_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,AddressList.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,ChangePassword.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePref.save_credential(ProfileActivity.this,SavePref.is_loogedin,"false");
                SavePref.removePref(ProfileActivity.this);
                Intent intent=new Intent(ProfileActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

    }
}
