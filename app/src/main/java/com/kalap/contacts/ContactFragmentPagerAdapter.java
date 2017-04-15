package com.kalap.contacts;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by puneetkkala on 12/04/17.
 */

public class ContactFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"DIAL", "RECENT","ALL"};

    public ContactFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 2) {
            return ContactListFragment.newInstance();
        } else if(position == 1) {
            return CallLogsFragment.newInstance();
        } else {
            return DialerFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
