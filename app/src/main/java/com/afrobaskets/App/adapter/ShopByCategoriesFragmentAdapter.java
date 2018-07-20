package com.afrobaskets.App.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobaskets.App.activity.Product_List_Activity;
import com.afrobaskets.App.activity.SubCategoryActivity;
import com.afrobaskets.App.bean.CategoryListBeans;
import com.afrobaskets.App.bean.ShopByCategoriesFragmentsBean;
import com.afrobaskets.App.bean.ShopByCategoriesFragmentsBeanImage;
import com.afrobaskets.App.constant.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hh on 13-Nov-17.
 */

 public  class ShopByCategoriesFragmentAdapter extends RecyclerView.Adapter<ShopByCategoriesFragmentAdapter.MyViewHolder> {
    List<ShopByCategoriesFragmentsBean> shopByCategoriesFragmentsBeanList;
    Context context;
    ArrayList<CategoryListBeans> categoryListBeansArrayList=new ArrayList<>();

    List<ShopByCategoriesFragmentsBeanImage> shopByCategoriesFragmentsBeanImageList;
    ProgressDialog pDialog;
    JSONObject sendjson;
    public ShopByCategoriesFragmentAdapter(Context context, List<ShopByCategoriesFragmentsBean> shopByCategoriesFragmentsBeanList,
                                           List<ShopByCategoriesFragmentsBeanImage> shopByCategoriesFragmentsBeanImageList) {
        this.shopByCategoriesFragmentsBeanList = shopByCategoriesFragmentsBeanList;
        this.shopByCategoriesFragmentsBeanImageList = shopByCategoriesFragmentsBeanImageList;
        this.context=context;
    }

    @Override
    public ShopByCategoriesFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_by_categories_fragments_adapter, parent, false);
        return new ShopByCategoriesFragmentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ShopByCategoriesFragmentAdapter.MyViewHolder viewHolder, final int position ) {

        viewHolder.txt_name.setText(shopByCategoriesFragmentsBeanList.get(position).getCategory_name());

        viewHolder.img_product.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                /*Intent intent=new Intent(context,SubCategoryActivity.class);
                intent.putExtra("id",shopByCategoriesFragmentsBeanList.get(position).getId());
                intent.putExtra("url",shopByCategoriesFragmentsBeanList.get(position).getImage_root_path()+"/"+shopByCategoriesFragmentsBeanList.get(position).getImage_id()+"/"+shopByCategoriesFragmentsBeanList.get(position).getImage_name());
                intent.putExtra("name",shopByCategoriesFragmentsBeanList.get(position).getCategory_name());

                intent.putExtra("type","category");
                context.startActivity(intent);*/
                getCategory(shopByCategoriesFragmentsBeanList.get(position).getId(), "category", position);
                // SavePref.saveStringPref(mcontext,SavePref.city_id,cityArrayList.get(position).getId());

            }
        });

        String url=shopByCategoriesFragmentsBeanList.get(position).getImage_root_path()+"/"+shopByCategoriesFragmentsBeanList.get(position).getImage_id()+"/"+shopByCategoriesFragmentsBeanList.get(position).getImage_name();
        Glide.with(context).load(url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img_product);
    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_name;
        ImageView img_product;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            txt_name= (TextView) itemLayoutView.findViewById(R.id.txt_name);
            img_product= (ImageView) itemLayoutView.findViewById(R.id.img_product);

        }
    }
    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return shopByCategoriesFragmentsBeanList.size();
    }




    private void getCategory(final String ids, final String type,final int position) {



        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        try {
            sendjson = new JSONObject();
            sendjson.put("method", "categoryList");
            sendjson.put("parent_category_id", ids);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObject= new JSONObject(response).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            categoryListBeansArrayList=new ArrayList<>();
                            while( keys.hasNext() )
                            {
                                CategoryListBeans categoryListBeans=new CategoryListBeans();
                                String key = keys.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                categoryListBeans.setId(innerJObject.getString("id"));
                                categoryListBeans.setCategory_name(innerJObject.getString("category_name"));
                                categoryListBeans.setParent_category_id(innerJObject.getString("parent_category_id"));
                                categoryListBeans.setCategory_des(innerJObject.getString("category_des"));
                                categoryListBeansArrayList.add(categoryListBeans);
                            }
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<CategoryListBeans>>() {}.getType();
                            String json = gson.toJson(categoryListBeansArrayList, type);

                            Intent intent=new Intent(context,SubCategoryActivity.class);
                            intent.putExtra("data",json);
                            // intent.putExtra("id",shopByCategoriesFragmentsBeanList.get(position).getId());
                            intent.putExtra("url",shopByCategoriesFragmentsBeanList.get(position).getImage_root_path()+"/"+shopByCategoriesFragmentsBeanList.get(position).getImage_id()+"/"+shopByCategoriesFragmentsBeanList.get(position).getImage_name());
                            intent.putExtra("name",shopByCategoriesFragmentsBeanList.get(position).getCategory_name());
                            intent.putExtra("type","category");
                            context.startActivity(intent);
                            pDialog.dismiss();
                        }
                        catch (JSONException e)
                        {  e.printStackTrace();
                           Intent intent=new Intent(context,Product_List_Activity.class);
                            intent.putExtra("id",ids);
                            intent.putExtra("type",type);
                            context.startActivity(intent);
                            pDialog.dismiss();
                               }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,"Communication Error!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Authentication Error!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,"Server Side Error!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context,"Parse Error!",Toast.LENGTH_SHORT).show();
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendjson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+sendjson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }


}