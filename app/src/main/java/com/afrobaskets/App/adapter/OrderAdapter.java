package com.afrobaskets.App.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webistrasoft.org.ecommerce.R;
import com.afrobaskets.App.bean.OrderBean;

import java.util.List;

/**
 * Created by asdfgh on 10/17/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<OrderBean> orderList;
    Context context;



    @Override
    public void onBindViewHolder(OrderAdapter.MyViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView order_id;
        public TextView order_date;
        public TextView order_address;
        public TextView delivery_date;
        public MyViewHolder(View itemView) {
            super(itemView);

            order_id=(TextView)  itemView.findViewById(R.id.text_order_id);
            order_date=(TextView)  itemView.findViewById(R.id.text_order_date);
            order_address=(TextView)  itemView.findViewById(R.id.text_address);


        }
    }

    public OrderAdapter(Context context, List<OrderBean> orderList) {
        this.orderList = orderList;
        this.context=context;
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_items, parent, false);
        return new MyViewHolder(itemView);
    }
}
