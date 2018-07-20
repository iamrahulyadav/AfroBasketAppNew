package com.afrobaskets.App.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrobaskets.App.activity.OrderSummeryActivity;
import com.afrobaskets.App.bean.ViewAndTrackOrderBeans;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by asdfgh on 10/13/2017.
 */

public class ViewAndTrackOrderAdapter extends RecyclerView.Adapter<ViewAndTrackOrderAdapter.MyViewHolder> {
    private List<ViewAndTrackOrderBeans> viewAndTrackOrderBeansArrayList;
    Context context;
    RecyclerView recyclerView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date,items;
       public TextView status;
        public TextView price,order_id;
        ImageView circle1,getCircle2,circle3,circle4;

        public MyViewHolder(View view) {
            super(view);
            date=(TextView)view.findViewById(R.id.create_date);
            status=(TextView)view.findViewById(R.id.status);
            items=(TextView)view.findViewById(R.id.items);
            price = (TextView)view.findViewById(R.id.price);
            order_id = (TextView)view.findViewById(R.id.order_id);
            circle1 = (ImageView) view.findViewById(R.id.circle1);
            circle4 = (ImageView) view.findViewById(R.id.circle4);
            getCircle2 = (ImageView) view.findViewById(R.id.circle2);
            circle3 = (ImageView) view.findViewById(R.id.circle3);
        }
    }
    public ViewAndTrackOrderAdapter(Context context, List<ViewAndTrackOrderBeans> viewAndTrackOrderBeansArrayList, RecyclerView recyclerView) {
        this.viewAndTrackOrderBeansArrayList = viewAndTrackOrderBeansArrayList;
        this.context=context;
        this.recyclerView=recyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewandtrackorderadapter, parent, false);
                itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context,OrderSummeryActivity.class);
                Gson gson = new Gson();
                Type type = new TypeToken<List<ViewAndTrackOrderBeans>>() {}.getType();
                String json = gson.toJson(viewAndTrackOrderBeansArrayList, type);
                intent.putExtra("data",json);
                intent.putExtra("position",""+recyclerView.getChildAdapterPosition(itemView));
                //intent.putExtra("",recyclerView.getChildAdapterPosition(itemView));
                context.startActivity(intent);
            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.date.setText(viewAndTrackOrderBeansArrayList.get(position).getCreated_date());
        holder.status.setText(viewAndTrackOrderBeansArrayList.get(position).getOrder_status());
        holder.items.setText(viewAndTrackOrderBeansArrayList.get(position).getViewAndTrackItemOrderBeanArrayList().size()+" Items");
        holder.price.setText("GHC "+viewAndTrackOrderBeansArrayList.get(position).getPayable_amount());
        holder.order_id.setText("Order Id: "+viewAndTrackOrderBeansArrayList.get(position).getOrder_id());

        if(viewAndTrackOrderBeansArrayList.get(position).getOrder_status().equalsIgnoreCase("order_placed"
        ) || viewAndTrackOrderBeansArrayList.get(position).getOrder_status().equalsIgnoreCase("assigned_to_rider"
        ))
        {
            holder.circle1.setImageResource(R.drawable.greencircle);
            holder.getCircle2.setImageResource(R.drawable.yellowcircle);
        }
        if(viewAndTrackOrderBeansArrayList.get(position).getOrder_status().equalsIgnoreCase("dispatched"
        ) || viewAndTrackOrderBeansArrayList.get(position).getOrder_status().equalsIgnoreCase("ready_to_dispatch"
        ))
        {
            holder.circle1.setImageResource(R.drawable.greencircle);
            holder.getCircle2.setImageResource(R.drawable.greencircle);
            holder.circle3.setImageResource(R.drawable.yellowcircle);
        }
        if(viewAndTrackOrderBeansArrayList.get(position).getOrder_status().equalsIgnoreCase("sample_submited_to_lab"
        ) )
        {
            holder.circle1.setImageResource(R.drawable.greencircle);
            holder.getCircle2.setImageResource(R.drawable.greencircle);
            holder.circle3.setImageResource(R.drawable.greencircle);
            holder.circle4.setImageResource(R.drawable.yellowcircle);
        }

        if(viewAndTrackOrderBeansArrayList.get(position).getOrder_status().equalsIgnoreCase("completed"
        ))
        {
            holder.circle1.setImageResource(R.drawable.greencircle);
            holder.getCircle2.setImageResource(R.drawable.greencircle);
            holder.circle3.setImageResource(R.drawable.greencircle);
            holder.circle4.setImageResource(R.drawable.greencircle);

        }

          }
    @Override
    public int getItemCount() {
        return viewAndTrackOrderBeansArrayList.size();
    }

}
