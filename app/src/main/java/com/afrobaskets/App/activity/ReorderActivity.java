package com.afrobaskets.App.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.afrobaskets.App.adapter.ReorderAdapter;
import com.afrobaskets.App.bean.CartBean;
import com.webistrasoft.org.ecommerce.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP-PC on 11/20/2017.
 */

public class ReorderActivity extends AppCompatActivity {
    private List<CartBean> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReorderAdapter fAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reorderactivity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fAdapter = new ReorderAdapter(ReorderActivity.this, foodList);
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
