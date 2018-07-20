package com.afrobaskets.App.drawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.webistrasoft.org.ecommerce.R;
import com.afrobaskets.App.adapter.OrderAdapter;
import com.afrobaskets.App.bean.OrderBean;

import java.util.ArrayList;
import java.util.List;


public class Order extends AppCompatActivity {
    private List<OrderBean> orderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_order);

        orderAdapter = new OrderAdapter(Order.this,orderList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderAdapter);

    }
}
