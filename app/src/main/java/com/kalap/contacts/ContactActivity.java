package com.kalap.contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.kalap.contacts.adapters.ViewPagerAdapter;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int CALL_PHONE_REQUEST = 102;
    private Uri data;
    private ViewPager pager;
    private BottomNavigationView navigationView;
    private MenuItem prevMenuItem;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        data = getIntent().getData();
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppCompatImageView share = (AppCompatImageView) findViewById(R.id.share);
        share.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST);
        }
        navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        pager = (ViewPager) findViewById(R.id.fragment_container);
        ArrayList<Fragment> fragments = new ArrayList<>();
        DialerFragment dialerFragment = DialerFragment.newInstance(data);
        fragments.add(dialerFragment);
        CallLogsFragment callLogsFragment = CallLogsFragment.newInstance();
        fragments.add(callLogsFragment);
        ContactListFragment contactListFragment = ContactListFragment.newInstance();
        fragments.add(contactListFragment);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);
    }

    private void shareApp() {
        String text = "Download this simple contacts app from https://play.google.com/store/apps/details?id=com.kalap.contacts";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_dial: {
                pager.setCurrentItem(0);
                return true;
            }
            case R.id.action_recent: {
                pager.setCurrentItem(1);
                return true;
            }
            case R.id.action_all: {
                pager.setCurrentItem(2);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        shareApp();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (prevMenuItem != null) {
            prevMenuItem.setChecked(false);
        } else {
            navigationView.getMenu().getItem(0).setChecked(false);
        }
        navigationView.getMenu().getItem(position).setChecked(true);
        prevMenuItem = navigationView.getMenu().getItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
