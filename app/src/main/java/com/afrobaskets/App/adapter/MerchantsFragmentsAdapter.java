package com.afrobaskets.App.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afrobaskets.App.activity.Product_List_Activity;
import com.afrobaskets.App.bean.MerchantsFragmentBean;
import com.webistrasoft.org.ecommerce.R;

import java.util.List;

/**
 * Created by hh on 13-Nov-17.
 */

public class MerchantsFragmentsAdapter  extends  RecyclerView.Adapter<MerchantsFragmentsAdapter.MyViewHolder> {
private List<MerchantsFragmentBean> merchantsFragmentBeanList;
        Context context;

    public MerchantsFragmentsAdapter(Context context, List<MerchantsFragmentBean> merchantsFragmentBeanList) {
        this.merchantsFragmentBeanList = merchantsFragmentBeanList;
        this.context=context;
    }

    @Override
    public MerchantsFragmentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.merchantfragmentsadapter, parent, false);

        return new MerchantsFragmentsAdapter.MyViewHolder(itemView);
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MerchantsFragmentsAdapter.MyViewHolder viewHolder, final int position ) {

        viewHolder.merchent_name.setText(merchantsFragmentBeanList.get(position).getFirst_name());

        viewHolder.btn_shop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
             /*   Intent intent=new Intent(context,SubCategoryActivity.class);
                intent.putExtra("id",merchantsFragmentBeanList.get(position).getId());
                intent.putExtra("type","merchant");
                intent.putExtra("url","xcdccdcds");
                intent.putExtra("name",merchantsFragmentBeanList.get(position).getFirst_name());
                context.startActivity(intent);*/
                Intent intent=new Intent(context,Product_List_Activity.class);
                intent.putExtra("id",merchantsFragmentBeanList.get(position).getId());
                intent.putExtra("type","merchant");
                context.startActivity(intent);

               // SavePref.saveStringPref(mcontext,SavePref.city_id,cityArrayList.get(position).getId());

            }
        });
    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView merchent_name;
Button btn_shop;
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            merchent_name= (TextView) itemLayoutView.findViewById(R.id.merchent_name);
            btn_shop= (Button) itemLayoutView.findViewById(R.id.button_shop);

        }
    }
    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return merchantsFragmentBeanList.size();
    }
}