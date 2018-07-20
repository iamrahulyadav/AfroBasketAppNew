package com.afrobaskets.App.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrobaskets.App.constant.Constants;
import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.ThankuactivityBinding;


public class ThankuActivity extends AppCompatActivity {
ThankuactivityBinding thankuactivityBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thankuactivityBinding= DataBindingUtil.setContentView(this,R.layout.thankuactivity);
        ImageView back=(ImageView) findViewById(R.id.toolbar_back);
        Button home=(Button) findViewById(R.id.btn_home);
        TextView order_id=(TextView)findViewById(R.id.order_id);
        order_id.setText(""+getIntent().getStringExtra("order_id"));
        String address= Constants.addressListBeanArrayList.get(0).getContact_name()+",\n"+Constants.addressListBeanArrayList.get(0).getHouse_number()+","+Constants.addressListBeanArrayList.get(0).getStreet_detail()+","+Constants.addressListBeanArrayList.get(0).getCity_name();
        thankuactivityBinding.total.setText(""+getIntent().getStringExtra("amount"));
        thankuactivityBinding.address.setText(address);
        thankuactivityBinding.time.setText(getIntent().getStringExtra("time"));
        thankuactivityBinding.items.setText(""+Constants.cartListBeenArray.size());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ThankuActivity.this,CategoriesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ThankuActivity.this,CategoriesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ThankuActivity.this,CategoriesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
