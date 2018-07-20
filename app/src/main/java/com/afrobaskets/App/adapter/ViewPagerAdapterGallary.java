package com.afrobaskets.App.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.afrobaskets.App.fragments.PageFragment;

import java.util.List;

/**
 * Created by HP-PC on 1/22/2018.
 */

public class ViewPagerAdapterGallary extends FragmentStatePagerAdapter {

    private List<Integer> images;

    public ViewPagerAdapterGallary(FragmentManager fm, List<Integer> imagesList) {
        super(fm);
        this.images = imagesList;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.getInstance(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }
}