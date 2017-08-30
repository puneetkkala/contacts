package com.kalap.contacts.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if (position > -1) {
            return fragments.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
