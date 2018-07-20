package com.afrobaskets.App.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrobaskets.App.activity.ImageShow;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webistrasoft.org.ecommerce.R;

import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by HP-PC on 12/9/2017.
 */

public class ProductDetailFagments extends Fragment {
    ProgressDialog pDialog;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    JSONObject sendJson=null;

    public static Fragment newInstance(Context context)
    {
        ProductDetailFagments f = new ProductDetailFagments();
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.productdetailfragments, null);
        TextView description=(TextView)root.findViewById(R.id.description);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("bullet"))
        {
            TextView bullet = (TextView) root.findViewById(R.id.bullet);
            String bullets = getArguments().getString("bullet");
            if (bullets != null && !bullets.isEmpty()) {
                String bull[] = bullets.split("#");
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < bull.length; i++) {
                    stringBuffer.append(getString(R.string.text_with_bullet));
                    stringBuffer.append(" "+bull[i]);
                    stringBuffer.append("\n");

                }
                bullet.setText("" + stringBuffer.toString());
            }
        }
        ImageView image=(ImageView) root.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ImageShow.class);


                intent.putExtra("image",getArguments().getString("image"));

                startActivity(intent);
            }
        });
        description.setText(arguments.getString("id"));
        if (arguments != null && arguments.containsKey("image_show"))
        {
            //description.setVisibility(View.GONE);
           image.setVisibility(View.VISIBLE);
            Glide.with(ProductDetailFagments.this).load(getArguments().getString("image"))
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }
        else {
            image.setVisibility(View.GONE);
        }

        return root;
    }
}

