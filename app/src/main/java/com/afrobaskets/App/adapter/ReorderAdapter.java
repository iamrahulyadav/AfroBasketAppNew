package com.afrobaskets.App.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afrobaskets.App.bean.CartBean;
import com.webistrasoft.org.ecommerce.R;

import java.util.List;

/**
 * Created by asdfgh on 10/13/2017.
 */

public class ReorderAdapter extends RecyclerView.Adapter<ReorderAdapter.MyViewHolder> {
    private List<CartBean> foodList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
       public TextView weght;
        public TextView price;
        public TextView textView;
        Button pluse, minus;
        int count = 0;

        public MyViewHolder(View view) {
            super(view);
            /*name=(TextView)  view.findViewById(R.id.txt_p_name);
            weght=(TextView)  view.findViewById(R.id.txt2_p_weight);
            price=(TextView)  view.findViewById(R.id.txt_p_price);
            minus = (Button) view.findViewById(R.id.button_minus);
            pluse = (Button) view.findViewById(R.id.button_pluse);
            textView = (TextView) view.findViewById(R.id.textView1);

            pluse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    textView.setText(String.valueOf(count));

                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(count>=1)
                    {
                        count=count-1;
                    }
                    textView.setText(String.valueOf(count));

                }
            });
*/
        }

    }
    public ReorderAdapter(Context context, List<CartBean> foodList) {
        this.foodList = foodList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reorderlistitemrow, parent, false);

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
