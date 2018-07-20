package com.afrobaskets.App.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afrobaskets.App.activity.ProductDetailActivity;
import com.afrobaskets.App.bean.SubCategoriesAdapterbean;
import com.afrobaskets.App.constant.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP-PC on 11/17/2017.
 */

public class OfferAndHotDealViewAllAcivityAdapter extends RecyclerView.Adapter<OfferAndHotDealViewAllAcivityAdapter.MyViewHolder> {
     private  ProgressDialog pDialog;
    JSONObject sendJson;
    String url;
    private List<String> itemsData;
    ArrayList<SubCategoriesAdapterbean> subCategoriesAdapterbeanArrayList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_offers,txt_p_name,txt_price,txt_offprice,item_count;
        ImageView img_products;
        Button button_minus,button_pluse,button_add;
        Spinner item_spinner;
        LinearLayout btn_linear1;

        public MyViewHolder(View view) {
            super(view);
            btn_linear1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
            button_add = (Button) view.findViewById(R.id.button_add);

            txt_p_name = (TextView) view.findViewById(R.id.txt_p_name);
            txt_price = (TextView) view.findViewById(R.id.txt_price);
            img_products = (ImageView) view.findViewById(R.id.img_products);
            txt_offprice = (TextView) view.findViewById(R.id.txt_offprice);
            button_minus = (Button) view.findViewById(R.id.button_minus);
            button_pluse = (Button) view.findViewById(R.id.button_pluse);
           item_count = (TextView) view.findViewById(R.id.txt_zero);
            item_spinner = (Spinner) view.findViewById(R.id.spinner);
            txt_offers = (TextView) view.findViewById(R.id.txt_offers);

            txt_price = (TextView) view.findViewById(R.id.txt_price);
        }

    }
    public OfferAndHotDealViewAllAcivityAdapter(Context context, ArrayList<SubCategoriesAdapterbean> subCategoriesAdapterbeanArrayList) {
        this.subCategoriesAdapterbeanArrayList = subCategoriesAdapterbeanArrayList;
        this.context=context;
    }

    @Override
    public OfferAndHotDealViewAllAcivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offerandhotdealviewallactivityadapter, parent, false);
        return new OfferAndHotDealViewAllAcivityAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OfferAndHotDealViewAllAcivityAdapter.MyViewHolder viewHolder, final int position) {
        viewHolder.txt_price.setText("GHC "+subCategoriesAdapterbeanArrayList.get(position).
                getSubCategoriesAdapterAttributesBeanArrayList().get(0).getPrice());
        viewHolder.txt_price.setText("GHC "+subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getPrice());
        viewHolder.item_count.setText("0");
        Float off_price=0.0f;
        try {

             off_price = Float.parseFloat(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getPrice()) - (Float.parseFloat(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getDiscount_value()));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        viewHolder.txt_offprice.setText("GHC "+String.valueOf(off_price));

       // viewHolder.txt_p_name.setText(subCategoriesAdapterbeanArrayList.get(position).getProduct_name());
        if(!subCategoriesAdapterbeanArrayList.get(position).getBrand_name().isEmpty()) {
            viewHolder.txt_p_name.setText(subCategoriesAdapterbeanArrayList.get(position).getProduct_name() + "(" + subCategoriesAdapterbeanArrayList.get(position).getBrand_name() + ")");
        }
        else {
            viewHolder.txt_p_name.setText(subCategoriesAdapterbeanArrayList.get(position).getProduct_name());
        }

        url=subCategoriesAdapterbeanArrayList.get(position).getImage_root_path()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_id()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_name();
        Glide.with(context).load(subCategoriesAdapterbeanArrayList.get(position).getImage_root_path()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_id()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_name())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img_products);
        setVarients(viewHolder.item_spinner,position);


        viewHolder.item_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                viewHolder.txt_price.setText("GHC "+subCategoriesAdapterbeanArrayList.get(position).
                        getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getPrice());
                viewHolder.txt_price.setText("GHC "+subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getPrice());
                Float off_price=0.0f;
                try {
                    if(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getDiscount_type().equalsIgnoreCase("percent"))
                    {
                        float discount_value=Float.parseFloat(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getPrice())*Float.parseFloat(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getPrice())/100;
                        off_price = Float.parseFloat(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getPrice()) - discount_value;
                    }
                    else {
                        off_price = Float.parseFloat(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getPrice()) - (Float.parseFloat(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getDiscount_value()));

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                if(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getDiscount_type().equalsIgnoreCase("percent"))
                {
                    viewHolder.txt_offers.setText(subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getDiscount_value()+" % Off");
                }
                else
                {
                    viewHolder.txt_offers.setText(("CHG "+subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getDiscount_value())+" Off");
                }

                viewHolder.txt_offprice.setText("GHC "+String.valueOf(off_price));
                if(!subCategoriesAdapterbeanArrayList.get(position).getBrand_name().isEmpty()) {
                    viewHolder.txt_p_name.setText(subCategoriesAdapterbeanArrayList.get(position).getProduct_name() + "(" + subCategoriesAdapterbeanArrayList.get(position).getBrand_name() + ")");
                }
                else {
                    viewHolder.txt_p_name.setText(subCategoriesAdapterbeanArrayList.get(position).getProduct_name());
                }
                viewHolder.txt_price.setPaintFlags(viewHolder.txt_offprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                viewHolder.txt_price.setText("GHC "+subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(arg2).getPrice());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        viewHolder.img_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ProductDetailActivity.class);
                /*intent.putExtra("desc",subCategoriesAdapterbeanArrayList.get(position).getProduct_desc());
                intent.putExtra("url",subCategoriesAdapterbeanArrayList.get(position).getImage_root_path()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_id()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_name());
                Gson gson = new Gson();
                String list = gson.toJson(subCategoriesAdapterbeanArrayList.get(position).getDescription());
                intent.putExtra("map", list);;

                context.startActivity(intent);*/
                Gson gson = new Gson();
                Type type = new TypeToken<List<SubCategoriesAdapterbean>>() {}.getType();
                String json = gson.toJson(subCategoriesAdapterbeanArrayList, type);
                intent.putExtra("data",json);
                intent.putExtra("position",""+position);

                context.startActivity(intent);
            }
        });
        viewHolder.button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.btn_linear1.setVisibility(View.VISIBLE);
                viewHolder.button_add.setVisibility(View.GONE);
                int count = Integer.parseInt(viewHolder.item_count.getText().toString());
                if(count<0)
                {
                    return;
                }
                Constants.updateCart(context,"add",subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getId() ,subCategoriesAdapterbeanArrayList.get(position).getProduct_name()+"("+subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getAttribute_name()+")");
                viewHolder.item_count.setText(""+(Integer.parseInt(viewHolder.item_count.getText().toString())+1));
            }
        });
        viewHolder.button_pluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(viewHolder.item_count.getText().toString());
                if(count<0)
                {
                    return;
                }
                Constants.updateCart(context,"add",subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getId() ,subCategoriesAdapterbeanArrayList.get(position).getProduct_name()+"("+subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getAttribute_name()+")");


                viewHolder.item_count.setText(""+(Integer.parseInt(viewHolder.item_count.getText().toString())+1));
            }
        });
        viewHolder.button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(viewHolder.item_count.getText().toString());
                if(count<=1)
                {  viewHolder.btn_linear1.setVisibility(View.GONE);
                    viewHolder.button_add.setVisibility(View.VISIBLE);

                }

                if(count<=0)
                {
                    return;
                }

               Constants.updateCart(context,"delete",subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getId(),subCategoriesAdapterbeanArrayList.get(position).getProduct_name()+"("+subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getAttribute_name()+")");

                viewHolder.item_count.setText(""+(Integer.parseInt(viewHolder.item_count.getText().toString())-1));
            }
        });

        try {
            viewHolder.btn_linear1.setVisibility(View.GONE);
            viewHolder.button_add.setVisibility(View.VISIBLE);
            for (int i = 0; i < Constants.cartListBeenArray.size(); i++) {
                if (subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(0).getId().equalsIgnoreCase(Constants.cartListBeenArray.get(i).getCartListProductDetailsBeanArrayList().get(0).getId())) {
                    viewHolder.btn_linear1.setVisibility(View.VISIBLE);
                    viewHolder.button_add.setVisibility(View.GONE);
                    viewHolder.item_count.setText(Constants.cartListBeenArray.get(i).getNumber_of_item());
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return subCategoriesAdapterbeanArrayList.size();
    }

    void setVarients(Spinner spinner,int position)
    {
        List<String> list = new ArrayList<String>();

        for(int i=0;i<subCategoriesAdapterbeanArrayList.get(position)
                .getSubCategoriesAdapterAttributesBeanArrayList().size();i++) {
            list.add(subCategoriesAdapterbeanArrayList.get(position)
                    .getSubCategoriesAdapterAttributesBeanArrayList().get(i).getQuantity()+" "+subCategoriesAdapterbeanArrayList.get(position)
                    .getSubCategoriesAdapterAttributesBeanArrayList().get(i).getUnit());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

}
