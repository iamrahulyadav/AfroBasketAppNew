package com.afrobaskets.App.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrobaskets.App.bean.CategoriesBean;
import com.webistrasoft.org.ecommerce.R;

import java.util.List;

/**
 * Created by asdfgh on 10/6/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    private List<CategoriesBean> foodList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            /*image=(ImageView)  view.findViewById(R.id.image_category);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,SubCategoryActivity.class);
                    context.startActivity(intent);
                }
            });*/
        }

    }
    public CategoriesAdapter(Context context, List<CategoriesBean> foodList) {
        this.foodList = foodList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.merchantfragmentsadapter, parent, false);
       /* itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SubCategoryActivity.class);
                context.startActivity(intent);
            }
        });*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       /*final CategoriesBean food=foodList.get(position);
       holder.image.setImageResource(food.getImage());*/
        /*holder.setClickListener(new ItemClickListener() {
            @Override public void onClickItem(int pos) {
                Toast.makeText(context, "CLICK : " + student.getName(), Toast.LENGTH_SHORT).show();
            }*/

    }

    @Override
    public int getItemCount() {
        return 10;
    }

}
