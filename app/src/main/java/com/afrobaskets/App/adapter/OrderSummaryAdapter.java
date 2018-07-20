package com.afrobaskets.App.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrobaskets.App.bean.CartBean;
import com.afrobaskets.App.bean.ViewAndTrackItemOrderBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webistrasoft.org.ecommerce.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asdfgh on 10/13/2017.
 */

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.MyViewHolder> {
    private List<CartBean> foodList;
    Context context;
    String image_url;
    ArrayList<ViewAndTrackItemOrderBean>viewAndTrackItemOrderBeanList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,qty;
       public TextView attribute;
        public TextView price;
        public TextView textView;
        ImageView image;

                public MyViewHolder(View view) {
            super(view);
            name=(TextView)  view.findViewById(R.id.name);
            attribute=(TextView)  view.findViewById(R.id.attribute);
            qty=(TextView)  view.findViewById(R.id.qty);
            price=(TextView)  view.findViewById(R.id.price);
                    image=(ImageView)  view.findViewById(R.id.product_image);



                }

    }
    public OrderSummaryAdapter(Context context, ArrayList<ViewAndTrackItemOrderBean> viewAndTrackItemOrderBeanList,String image_url) {
        this.viewAndTrackItemOrderBeanList = viewAndTrackItemOrderBeanList;
        this.context=context;
        this.image_url=image_url;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderlistitemrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(viewAndTrackItemOrderBeanList.get(position).getProduct_name());
        holder.attribute.setText(viewAndTrackItemOrderBeanList.get(position).getQuantity()+viewAndTrackItemOrderBeanList.get(position).getUnit());
        holder.price.setText("GHC "+viewAndTrackItemOrderBeanList.get(position).getAmount());
        holder.qty.setText(viewAndTrackItemOrderBeanList.get(position).getNumber_of_item()+"Items");
        Glide.with(context).load(image_url+"/"+viewAndTrackItemOrderBeanList.get(position).getImage_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return viewAndTrackItemOrderBeanList.size();
    }

}
