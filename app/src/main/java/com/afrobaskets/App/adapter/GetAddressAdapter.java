package com.afrobaskets.App.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afrobaskets.App.activity.AddressList;
import com.afrobaskets.App.activity.Address_Activity;
import com.afrobaskets.App.activity.PlaceOrderActivity;
import com.afrobaskets.App.bean.AddressListBean;
import com.afrobaskets.App.constant.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by asdfgh on 10/17/2017.
 */

public class GetAddressAdapter extends RecyclerView.Adapter<GetAddressAdapter.MyViewHolder> {
    ArrayList<AddressListBean>addressListBeanArrayList;
    Context context;
String flag;


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(addressListBeanArrayList.get(position).getAddress_nickname());
        if(flag.equalsIgnoreCase("checkout"))
        {
            holder.checkbox.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkbox.setVisibility(View.GONE);

        }
        holder.text_address.setText(addressListBeanArrayList.get(position).getHouse_number() + "," +
                addressListBeanArrayList.get(position).getArea() + "\n" +
                addressListBeanArrayList.get(position).getLandmark() + "," +
                addressListBeanArrayList.get(position).getCity_name());
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(context,PlaceOrderActivity.class);
                Constants.addressListBeanArrayList.add(0,addressListBeanArrayList.get(position));
                context.startActivity(intent);
                ( (AddressList)context).finish();

            }
        });
        holder.edit_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Address_Activity.class);
                Gson gson = new Gson();
                Type type = new TypeToken<List<AddressListBean>>() {}.getType();
                String json = gson.toJson(addressListBeanArrayList, type);
                intent.putExtra("data",json);
                intent.putExtra("position",""+position);
                intent.putExtra("type","edit");
                if(flag.equalsIgnoreCase("checkout"))
                {
                    intent.putExtra("checkout","checkout");
                }
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return addressListBeanArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,text_address,items;
        Button button_minus, button_plus;
        Spinner item_spinner;
        @Bind(R.id.text)
        TextView text;
        ImageView edit_ads;
        CheckBox checkbox;
        public MyViewHolder(View itemView) {
            super(itemView);
            edit_ads=(ImageView)itemView.findViewById(R.id.edit_ads);

            name = (TextView) itemView.findViewById(R.id.name);
            text_address = (TextView) itemView.findViewById(R.id.text_address);
            checkbox = (CheckBox) itemView.findViewById(R.id.check_box);
        }
    }

    public GetAddressAdapter(Context context,ArrayList<AddressListBean> addressListBeanArrayList,String flag) {
        this.addressListBeanArrayList = addressListBeanArrayList;
        this.context=context;
        this.flag=flag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.getaddress_list_adapter, parent, false);
        return new MyViewHolder(itemView);

    }
}
