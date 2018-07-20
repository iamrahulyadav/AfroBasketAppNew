package com.afrobaskets.App.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.SignupActivityBinding;

/**
 * Created by HP-PC on 11/27/2017.
 */

public class AboutUsActivity extends AppCompatActivity {

    SignupActivityBinding signupActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        TextView textView=(TextView)findViewById(R.id.dummy_txt);
        textView.setText(Html.fromHtml("<h2>Online grocery Ghana</h2> <p>Afro Baskets ltd. is the first comprehensive online grocery store in Ghana. With over 10,000+ products and 700+ brands in our grocery list you will find everything you are looking for. Right from Staples, Beverages, Baby Products and seasonings to packaged bread, bakery and pet food and other branded foods online - we have it all. Save time and money, shop at Afrobaskets.com- an all-encompassing online groceries store of Ghana.</p> <h2>Ghana’s Best Online Supermarket </h2> <p>Shopping with Afrobaskets.com, the best online grocery and Branded food shopping store in Ghana. It is very simple and easy. No more standing in lines, carrying heavy bags and looking for parking! Get started with online grocery shopping at Ghana’s favourite online grocery store. Choose from a wide range of options in every groceries list, exclusively handpicked to help you find the best quality available at the lowest prices. Select a time slot for home delivery and your order will be delivered at your doorstep, anywhere in Accra and Tema. In future we are expanding to other cities in Ghana. You can pay by cash, card, Mobile Money. We guarantee on time delivery and the best quality! Happy Shopping!</p> <h2>Online Grocery Supermarket Ghana</h2> <p> We, at Afrobaskets.com, are excited about making life simpler and grocery shopping a breeze! Now you can buy Breakfast food & grocery online at your leisure and from the comfort of your home - no more standing in the long queues at ration shops, provision stores & supermarkets. Online grocery shopping in Accra and Tema is made easy & simple when you are shopping groceries at Afrobaskets.com."));

        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
