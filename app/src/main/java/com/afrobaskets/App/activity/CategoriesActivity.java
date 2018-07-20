package com.afrobaskets.App.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobaskets.App.adapter.CategoriesAdapter;
import com.afrobaskets.App.adapter.ViewPagerAdapter;
import com.afrobaskets.App.bean.BannerBeans;
import com.afrobaskets.App.bean.CartListBean;
import com.afrobaskets.App.bean.CategoriesBean;
import com.afrobaskets.App.bean.ExpandedMenuModel;
import com.afrobaskets.App.bean.TimeSlotsBean;
import com.afrobaskets.App.constant.Constants;
import com.afrobaskets.App.constant.SavePref;
import com.afrobaskets.App.fragments.MerchantsFragments;
import com.afrobaskets.App.fragments.OffersAndHotDealFragment;
import com.afrobaskets.App.fragments.ShopByCategoriesFragments;
import com.afrobaskets.App.interfaces.CartCallback;
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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.webistrasoft.org.ecommerce.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener,
    ViewPagerEx.OnPageChangeListener,CartCallback {
    ProgressDialog pDialog;
    HashMap<String, String> url_maps;
    private List<CategoriesBean> foodList = new ArrayList<>();
    private RecyclerView new_arrival_recyclerView, hot_deal_recyclerView, offer_recyclerView;
    private CategoriesAdapter fAdapter;
    Toolbar toolbar;
    private SliderLayout imageSlider;
    ArrayList<BannerBeans> beansArrayList;
    private DrawerLayout mDrawerLayout;
    com.afrobaskets.App.adapter.ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    JSONObject sendJson;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    TabLayout tabLayout;
    int bannerPosition;
    JSONObject manJson;
    ViewPager viewPager;
TextView time_slots;
    TextView txt_address;



    private void getTimeSlots (){

        try {
            sendJson = new JSONObject();
            sendJson.put("method", "deliveryTimeSlotList");
             }catch (Exception e)
        {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"application",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObject= new JSONObject(response);
                            String responce_time=jObject.getString("response_time");
                            String res[]=responce_time.split("\\s+");;
                            if(jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                Constants.todaySlotsBeanArrayList.clear();
                                Constants.tomorrowSlotsBeanArrayList.clear();
                                JSONObject Object = jObject.getJSONObject("datewisetimeslot");
                                Iterator<String> keys = Object.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                      JSONObject attributeObject = Object.getJSONObject(key);
                                    Iterator<String> attKey = attributeObject.keys();
                                    while (attKey.hasNext()) {
                                        String innerkeys = attKey.next();
                                        JSONObject attribute = attributeObject.getJSONObject(innerkeys);
                                        TimeSlotsBean timeSlotsBean = new TimeSlotsBean();
                                        timeSlotsBean.setId(attribute.getString("id"));
                                        timeSlotsBean.setStart_time_slot(attribute.getString("start_time_slot"));
                                        timeSlotsBean.setEnd_time_slot(attribute.getString("end_time_slot"));
                                        timeSlotsBean.setCreated_on(attribute.getString("created_on"));
                                        timeSlotsBean.setUpdated_on(attribute.getString("updated_on"));
if(key.equalsIgnoreCase(res[0]))
{
    Constants.today_date=key;
    Constants.todaySlotsBeanArrayList.add(timeSlotsBean);
}else {
    Constants.tomorrow_date=key;
    Constants.tomorrowSlotsBeanArrayList.add(timeSlotsBean);
}
 }
                                }
                                time_slots=(TextView)findViewById(R.id.txt_timeslots);
                                if(Constants.todaySlotsBeanArrayList.size()>0) {
                                    String time = "Today " + Constants.todaySlotsBeanArrayList.get(0).getStart_time_slot() + ":00 to " +Constants.todaySlotsBeanArrayList.get(0).getEnd_time_slot() + ":00";
                                    time_slots.setText(time);
                                }
                                else {
                                    String time = "Tomorrow " + Constants.tomorrowSlotsBeanArrayList.get(0).getStart_time_slot() + ":00   to " + Constants.tomorrowSlotsBeanArrayList.get(0).getEnd_time_slot() + ":00";
                                    time_slots.setText(time);
                                }

                            }
                            else
                            {
                                Constants.showSnackBar(CategoriesActivity.this,jObject.getString("msg"));
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(CategoriesActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(CategoriesActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(CategoriesActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(CategoriesActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(CategoriesActivity.this,"Parse Error!");
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+sendJson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }


    //http://54.233.182.212/basketapi/application?parameters=%7b%22method%22:%22deliveryTimeSlotList%22%7d
    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("Home");
        item1.setIconImg(android.R.drawable.ic_delete);
        // Adding data header
        listDataHeader.add(item1);

        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("My Account");
        item2.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item2);

        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("About Us");
        item3.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item3);
        ExpandedMenuModel item5 = new ExpandedMenuModel();
        item5.setIconName("Share");
        item5.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item5);
        ExpandedMenuModel item6 = new ExpandedMenuModel();
        item6.setIconName("Help");
        item6.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item6);
        ExpandedMenuModel item7 = new ExpandedMenuModel();
        item7.setIconName("Rate us");
        item7.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item7);
        ExpandedMenuModel item4 = new ExpandedMenuModel();
        item4.setIconName("Terms and conditions");
        item4.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item4);
        ExpandedMenuModel item8 = new ExpandedMenuModel();
        item8.setIconName("Call Us");
        item8.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item8);
        // Adding child data
        List<String> heading1 = new ArrayList<String>();
        heading1.add("View and Track Order");
      //  heading1.add("Shop From My Previous Order");
        heading1.add("Profile");
        heading1.add("Delivery Address");
        heading1.add("Notification");
        heading1.add("Change Password");
        heading1.add("Logout");
        listDataChild.put(listDataHeader.get(1), heading1);// Header, Child data
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Shop By Categories");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories_select, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Offers and Hot deals");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories_select, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Shop By Merchants");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories_select, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if(SavePref.get_credential(CategoriesActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true"))
        {
            LinearLayout layout=(LinearLayout)findViewById(R.id.linear1);
            layout.setVisibility(View.GONE);
        }

        cart_count.setText(""+Constants.cartcount);
        try {
            OffersAndHotDealFragment frag1 = (OffersAndHotDealFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            frag1.refress();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
            }

    void slider() {
        imageSlider = (SliderLayout) findViewById(R.id.slider);
        for (int i = 0; i < beansArrayList.size(); i++) {

            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView

                    .image(beansArrayList.get(i).getImageRootPath() + "/" + beansArrayList.get(i).getImage_name())

                    .setScaleType(BaseSliderView.ScaleType.Fit)

                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");

            imageSlider.addSlider(textSliderView);
        }
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        imageSlider.setDuration(4000);
        imageSlider.addOnPageChangeListener(this);
    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        imageSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

       /* Intent intent=new Intent(this,Product_List_Activity.class);
        intent.putExtra("id",beansArrayList.get(bannerPosition).getCategory_id());
        intent.putExtra("type","banner");
        intent.putExtra("method", beansArrayList.get(bannerPosition).getMethod());
        intent.putExtra("city_id", beansArrayList.get(bannerPosition).getCity_id());
        startActivity(intent);*/
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.e("Slider Demo", "Page Changed: " + position);
        bannerPosition = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    void setup()
    {
        ImageView voice = (ImageView) findViewById(R.id.voice);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rel1);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, SearchActivity.class);
                intent.putExtra(
                        "type", "search");
                startActivity(intent);

            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, SearchActivity.class);
                intent.putExtra(
                        "type", "search");
                startActivity(intent);

            }
        });

       /* ImageView image=(ImageView)findViewById(R.id.img_cart);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this,CartListActivity.class);
                startActivity(intent);
            }
        });*/


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        prepareListData();
        mMenuAdapter = new com.afrobaskets.App.adapter.ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);
        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (i1 == 0) {
                    startActivity(new Intent(CategoriesActivity.this, ViewAndTrackOrderActivity.class));
                    mDrawerLayout.closeDrawers();
                }
              if (i1 == 3) {
                    startActivity(new Intent(CategoriesActivity.this, NotificationActivity.class));
                    mDrawerLayout.closeDrawers();
                }
                if (i1 == 1) {
                    startActivity(new Intent(CategoriesActivity.this, ProfileActivity.class));
                    mDrawerLayout.closeDrawers();
                }
                if (i1 == 2) {
                    startActivity(new Intent(CategoriesActivity.this, AddressList.class));
                    mDrawerLayout.closeDrawers();
                }
                if (i1 == 4) {
                    startActivity(new Intent(CategoriesActivity.this, ChangePassword.class));
                    mDrawerLayout.closeDrawers();
                }
                if (i1 == 5) {
                    SavePref.save_credential(CategoriesActivity.this,SavePref.is_loogedin,"false");
                    SavePref.removePref(CategoriesActivity.this);
                    Intent intent=new Intent(CategoriesActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("type","logout");

                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            private int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (!SavePref.get_credential(CategoriesActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true")) {
                    expandableList.collapseGroup(1);
                }

            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int lastExpandedPosition = -1;

            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                if (i == 0) {
                    mDrawerLayout.closeDrawers();
                }
                if(i==1)
                {
                    if (!SavePref.get_credential(CategoriesActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true"))
                    {
                            startActivity(new Intent(CategoriesActivity.this, LoginActivity.class));
                        mDrawerLayout.closeDrawers();

                    }
                  //  mDrawerLayout.closeDrawers();

                }
                if (i == 2) {
                    mDrawerLayout.closeDrawers();
                    startActivity(new Intent(CategoriesActivity.this, AboutUsActivity.class));
                }
                if (i == 3) {
                    mDrawerLayout.closeDrawers();

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.ahoy.ureward");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));
                }
                if (i == 4) {
                    mDrawerLayout.closeDrawers();
                    startActivity(new Intent(CategoriesActivity.this, HelpActivity.class));
                }
                if (i == 5) {
                    mDrawerLayout.closeDrawers();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("market://details?id=com.ahoy.ureward")));
                }
                if (i == 6) {
                    mDrawerLayout.closeDrawers();
                }
                if (i == 7) {
                    mDrawerLayout.closeDrawers();
                    onCall();
                }
                return false;
            }
        });

        ImageView slider = (ImageView) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });
        FrameLayout cart = (FrameLayout) findViewById(R.id.cart_layout);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.cartListBeenArray.size()>0) {

                    startActivity(new Intent(CategoriesActivity.this, CartListActivity.class));
                }
                else {
                    Constants.showSnackBar(CategoriesActivity.this,getString(R.string.cart_msg));
                }
            }
        });



        /*ImageView edit_address=(ImageView)findViewById(R.id.edit_address);
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoriesActivity.this, AddressList.class));
            }
        });*/
        try {
            txt_address = (TextView) findViewById(R.id.txt_location);
            txt_address.setText(SavePref.get_credential(CategoriesActivity.this,SavePref.City_name));
           /* if (Constants.addressListBeanArrayList.size() > 0) {
                String address = Constants.addressListBeanArrayList.get(0).getHouse_number() + "," + Constants.addressListBeanArrayList.get(0).getStreet_detail() + "," + Constants.addressListBeanArrayList.get(0).getLandmark() + "," + Constants.addressListBeanArrayList.get(0).getCity_name();
                txt_address.setText(address);
            }*/
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Constants.getCartList(CategoriesActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);
         //getAddress();
          getBanner();
          getTimeSlots ();
        pDialog = new ProgressDialog(CategoriesActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        setup();

    }

    TextView cart_count;


    public void upsateCart(String add) {
        int count = Integer.parseInt(cart_count.getText().toString());
        if (count < 0) {
            return;
        }
        if (add.equalsIgnoreCase("add")) {
            count = count + 1;
        } else {
            count = count - 1;
        }
        cart_count.setText("" + count);
    }

    private void getBanner() {


        if (!Constants.isNetworkAvailable(this)) {
            Snackbar snackbar = Snackbar
                    .make(offer_recyclerView, "Please Connect Internet", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
      /*  pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);*/

        try {
            manJson = new JSONObject();
            manJson.put("method", "banner");
            manJson.put("status", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + "application",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString = "";//your json string here
                        try {
                            JSONObject Object = new JSONObject(response);
                            if (Object.getString("status").equalsIgnoreCase("success")) {
                                //JSONObject jObject = new JSONObject(response).getJSONObject("data");
                                JSONArray jsonArray = Object.getJSONArray("data");
                                url_maps = new HashMap<String, String>();
                                beansArrayList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject innerJObject = jsonArray.getJSONObject(i);
                                    // url_maps.put(""+i,Object.getString("imageRootPath")+"/"+innerJObject.getString("image_name") );
                                 //   JSONObject innerObject = new JSONObject(innerJObject.getString("params"));
                                    BannerBeans bannerBeans = new BannerBeans();
/*
                                    bannerBeans.setMethod(innerObject.getString("method"));

                                    bannerBeans.setCity_id(innerObject.getString("city_id"));

                                    bannerBeans.setCategory_id(innerObject.getString("category_id"));*/
                                    bannerBeans.setImage_name(innerJObject.getString("image_name"));
                                    bannerBeans.setImageRootPath(Object.getString("imageRootPath"));
                                    bannerBeans.setId(innerJObject.getString("id"));

                                    bannerBeans.setDescription(innerJObject.getString("description"));
                                    bannerBeans.setStatus(innerJObject.getString("status"));
                                    beansArrayList.add(bannerBeans);


                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                      //  pDialog.dismiss();
                        slider();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constants.showSnackBar(CategoriesActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    Constants.showSnackBar(CategoriesActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    Constants.showSnackBar(CategoriesActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    Constants.showSnackBar(CategoriesActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    Constants.showSnackBar(CategoriesActivity.this,"Parse Error!");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", manJson.toString());
                params.put("rqid",Constants.get_SHA_512_SecurePassword(Constants.salt+manJson.toString()));

                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:12345678901")));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Toast.makeText(getApplicationContext(), "Call Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }


    void setViewpager() {
         viewPager = (ViewPager) findViewById(R.id.pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add Fragments to adapter one by one
        adapter.addFragment(new ShopByCategoriesFragments(), "");
        adapter.addFragment(new OffersAndHotDealFragment(), "");
        adapter.addFragment(new MerchantsFragments(), "");
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
        for (int i = 0; i < tabLayout.getTabCount() - 1; i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 3, 0);
            tab.requestLayout();
        }
viewPager.setOffscreenPageLimit(3);
        viewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==1) {
                    OffersAndHotDealFragment frag1 = (OffersAndHotDealFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    frag1.refress();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(pDialog!=null)
        {
            pDialog.dismiss();
        }
        /*viewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });*/
        txt_address = (TextView) findViewById(R.id.txt_location);
        txt_address.setText(SavePref.get_credential(CategoriesActivity.this,SavePref.City_name));
       Button btn_login =(Button)findViewById(R.id.btn_login);
        Button sign_up =(Button)findViewById(R.id.sign_up);

                ImageView edit_address=(ImageView)findViewById(R.id.edit_address);
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoriesActivity.this, CityListActivity.class);
                startActivity(intent);
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoriesActivity.this, SignUp_Activity.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoriesActivity.this, LoginActivity.class));
            }
        });
        if(SavePref.get_credential(CategoriesActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true"))
        {
            LinearLayout layout=(LinearLayout)findViewById(R.id.linear1);
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.dashboard, menu);
        return false;
    }


    @Override
    public void onSuccess(ArrayList<CartListBean> cartListBeanArrayList,int result) {

        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_count.setText(""+result);
        if(viewPager!=null)
        {
            return;
        }
        setViewpager();

    }
}