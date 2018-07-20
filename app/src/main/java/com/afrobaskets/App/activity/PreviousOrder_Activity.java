package com.afrobaskets.App.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.afrobaskets.App.adapter.PreviousOrderAdapter;
import com.afrobaskets.App.bean.CartBean;
import com.webistrasoft.org.ecommerce.R;

import java.util.ArrayList;
import java.util.List;

public class PreviousOrder_Activity extends AppCompatActivity {
        private List<CartBean> foodList = new ArrayList<>();
        private RecyclerView recyclerView;
        private PreviousOrderAdapter fAdapter;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView( R.layout.previousorderactivity);
            RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view_subcat);
            fAdapter = new PreviousOrderAdapter(PreviousOrder_Activity.this,foodList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fAdapter);
            ImageView back = (ImageView) findViewById(R.id.toolbar_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
}
