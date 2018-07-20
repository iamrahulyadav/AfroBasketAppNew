package com.afrobaskets.App.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.afrobaskets.App.bean.CategoryListBeans;
import com.afrobaskets.App.fragments.SubCategoriesFragment;

import java.util.ArrayList;

/**
 * Created by asdfgh on 10/9/2017.
 */

public class SubCategoryPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
String type;
    ArrayList<CategoryListBeans> categoryListBeansArrayList;
Context context;
    // Build a Constructor and assign the passed Values to appropriate values in the class

    public SubCategoryPagerAdapter(Context context, FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, ArrayList<CategoryListBeans> categoryListBeansArrayList,
                                   String type)
    {
        super(fm);
        this.categoryListBeansArrayList=categoryListBeansArrayList;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.type=type;
        this.context=context;


    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("id",categoryListBeansArrayList.get(position).getId());
        bundle.putString("type",type);
       /* if(type.equalsIgnoreCase("banner"))
        {
            bundle.putString("city_id",((Activity) context).getIntent().getStringExtra("city_id"));
            bundle.putString("method",((Activity) context).getIntent().getStringExtra("method"));

        }*/
        SubCategoriesFragment tab = new SubCategoriesFragment();
        tab.setArguments(bundle);
            return tab;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE ;
    }

// This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
