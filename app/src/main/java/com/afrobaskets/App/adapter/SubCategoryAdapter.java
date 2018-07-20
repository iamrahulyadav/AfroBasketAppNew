package com.afrobaskets.App.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import butterknife.Bind;

/**
 * Created by asdfgh on 10/9/2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
private  ProgressDialog pDialog;
    JSONObject sendJson;
    String url;
    private List<String> itemsData;
ArrayList<SubCategoriesAdapterbean> subCategoriesAdapterbeanArrayList;
Context context;
    public SubCategoryAdapter(Context context,ArrayList<SubCategoriesAdapterbean>subCategoriesAdapterbeanArrayList) {
        this.itemsData = itemsData;
        this.subCategoriesAdapterbeanArrayList=subCategoriesAdapterbeanArrayList;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item_list, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.price.setText("GHC "+subCategoriesAdapterbeanArrayList.get(position).
                getSubCategoriesAdapterAttributesBeanArrayList().get(0).getPrice());

        if(!subCategoriesAdapterbeanArrayList.get(position).getBrand_name().isEmpty()) {
            viewHolder.brand_name.setVisibility(View.VISIBLE);
            viewHolder.brand_name.setText(subCategoriesAdapterbeanArrayList.get(position).getBrand_name());
        }
        else {
            viewHolder.brand_name.setVisibility(View.GONE);
        }
            viewHolder.name.setText(subCategoriesAdapterbeanArrayList.get(position).getProduct_name());

        viewHolder.item_count.setText("0");

        url=subCategoriesAdapterbeanArrayList.get(position).getImage_root_path()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_id()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_name();
        Glide.with(context).load(subCategoriesAdapterbeanArrayList.get(position).getImage_root_path()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_id()+"/"+subCategoriesAdapterbeanArrayList.get(position).getImage_name())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.product_image);
        setVarients(viewHolder.item_spinner,position);
        viewHolder.item_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                viewHolder.price.setText("GHC "+subCategoriesAdapterbeanArrayList.get(position).getSubCategoriesAdapterAttributesBeanArrayList().get(viewHolder.item_spinner.getSelectedItemPosition()).getPrice());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

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


        viewHolder.product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ProductDetailActivity.class);
                  Gson gson = new Gson();
                Type type = new TypeToken<List<SubCategoriesAdapterbean>>() {}.getType();
                String json = gson.toJson(subCategoriesAdapterbeanArrayList, type);
                intent.putExtra("data",json);
                intent.putExtra("position",""+position);

                context.startActivity(intent);
            }
        });

        viewHolder.button_plus.setOnClickListener(new View.OnClickListener() {
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
            viewHolder.item_count.setText(Constants.cartListBeenArray.get(i).getNumber_of_item());
            viewHolder.btn_linear1.setVisibility(View.VISIBLE);
            viewHolder.button_add.setVisibility(View.GONE);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView weight;
ImageView product_image;
        TextView item_count,price,items;
        Button button_minus, button_plus,button_add;
        Spinner item_spinner;
        @Bind(R.id.text)
        TextView text;
        TextView brand_name;
LinearLayout btn_linear1;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            name = (TextView) itemLayoutView.findViewById(R.id.txt_p_name);
            button_add = (Button) itemLayoutView.findViewById(R.id.button_add);
            btn_linear1 = (LinearLayout) itemLayoutView.findViewById(R.id.linear1);
            brand_name = (TextView) itemLayoutView.findViewById(R.id.txt_brand_name);
            price = (TextView) itemLayoutView.findViewById(R.id.price);
            product_image = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            // name = (TextView) itemLayoutView.findViewById(R.id.items);
            button_minus = (Button) itemLayoutView.findViewById(R.id.button_minus);
            button_plus = (Button) itemLayoutView.findViewById(R.id.button_pluse);
            item_count = (TextView) itemLayoutView.findViewById(R.id.textView1);
            item_spinner = (Spinner) itemLayoutView.findViewById(R.id.spinner);
        }
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
