package com.afrobaskets.App.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrobaskets.App.bean.CartBean;
import com.afrobaskets.App.bean.CartListBean;
import com.afrobaskets.App.constant.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webistrasoft.org.ecommerce.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asdfgh on 10/13/2017.
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
    private List<CartBean> foodList;
    Context context;
    private  ProgressDialog pDialog;
    JSONObject sendJson;
    ArrayList<CartListBean>cartListBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;
       public TextView items;
        public TextView price,item_count;
        Button pluse, minus;
        int count = 0;
        Button button_minus, button_plus;
        ImageView product_image;
        public MyViewHolder(View view)
        {

            super(view);
            items=(TextView)  view.findViewById(R.id.items);

            product_image=(ImageView)  view.findViewById(R.id.product_image);
            name=(TextView)  view.findViewById(R.id.txt_p_name);

            price=(TextView)  view.findViewById(R.id.price);
            button_minus = (Button) view.findViewById(R.id.button_minus);

            button_plus = (Button) view.findViewById(R.id.button_pluse);
            item_count = (TextView) view.findViewById(R.id.textView1);

        }
    }

    public CartListAdapter(Context context, ArrayList<CartListBean> cartListBeanArrayList)
    {

        this.cartListBeanArrayList = cartListBeanArrayList;
        this.context=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position)
    {
        holder.name.setText(cartListBeanArrayList.get(position).getItem_name());
        holder.button_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int count = Integer.parseInt(holder.item_count.getText().toString());
                if (count <= 0)
                {
                    return;
                }
       Constants.updateCart(context,"add", cartListBeanArrayList.get(position).getCartListProductDetailsBeanArrayList().get(0).getId(), cartListBeanArrayList.get(position).getItem_name());
                //holder.item_count.setText("" + (Integer.parseInt(holder.item_count.getText().toString()) + 1));
            }
        });
        try {
           Glide.with(context).load(Constants.BASE_URL+"public/images/product/"+ cartListBeanArrayList.get(position).getCartListProductImageArrayList().get(0).getImage_id() + "/" + cartListBeanArrayList.get(position).getCartListProductImageArrayList().get(0).getImage_name())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.product_image);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        holder.button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.item_count.getText().toString());

                Constants.updateCart(context,"delete",cartListBeanArrayList.get(position).getCartListProductDetailsBeanArrayList().get(0).getId() ,cartListBeanArrayList.get(position).getItem_name());

               /* holder.item_count.setText(""+(Integer.parseInt(holder.item_count.getText().toString())-1));
                if(count==1)
                {
                    cartListBeanArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,cartListBeanArrayList.size());
                }*/

            }
        });

        holder.item_count.setText(cartListBeanArrayList.get(position).getNumber_of_item());

         if(cartListBeanArrayList.get(position).getCartListProductDetailsBeanArrayList()!=null) {
            holder.price.setText("GHC "+cartListBeanArrayList.get(position).getCartListProductDetailsBeanArrayList().get(0).getPrice());

                }
    }

    @Override
    public int getItemCount() {
        return cartListBeanArrayList.size();
    }

}
