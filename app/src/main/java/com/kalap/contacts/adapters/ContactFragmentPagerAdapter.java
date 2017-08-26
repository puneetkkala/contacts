package com.kalap.contacts.adapters;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kalap.contacts.CallLogsFragment;
import com.kalap.contacts.ContactListFragment;
import com.kalap.contacts.DialerFragment;

public class ContactFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"DIAL", "RECENT","ALL"};
    private Uri data;

    public ContactFragmentPagerAdapter(FragmentManager fm, Uri data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 2) {
            return ContactListFragment.newInstance();
        } else if(position == 1) {
            return CallLogsFragment.newInstance();
        } else {
            return DialerFragment.newInstance(data);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
