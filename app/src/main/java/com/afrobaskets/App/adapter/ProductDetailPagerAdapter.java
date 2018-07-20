package com.afrobaskets.App.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.afrobaskets.App.fragments.ProductDetailFagments;

import java.util.LinkedHashMap;

/**
 * Created by asdfgh on 10/9/2017.
 */

public class ProductDetailPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    LinkedHashMap<String ,String> hashMapdesc;
String image;
    String bullet;
    // Build a Constructor and assign the passed Values to appropriate values in the class

    public ProductDetailPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, LinkedHashMap<String ,String> hashMapdesc,String image,String bullet)
    {
        super(fm);
        this.hashMapdesc=hashMapdesc;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.image=image;
        this.bullet=bullet;
    }


    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("id",hashMapdesc.get(Titles[position]));
        bundle.putString("image",image);
        if(position==0)
        {
            bundle.putString("bullet",bullet);
        }
        if(position==NumbOfTabs-1)
        {
            bundle.putString("image_show","Show");
        }
        ProductDetailFagments tab = new ProductDetailFagments();
        tab.setArguments(bundle);

            return tab;
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
