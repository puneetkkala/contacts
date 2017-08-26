package com.kalap.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.kalap.contacts.adapters.ContactFragmentPagerAdapter;

public class ContactActivity extends AppCompatActivity {

    public static final String TAG = "ContactActivity";
    private static final int CALL_PHONE_REQUEST = 102;
    private ContactFragmentPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Uri data = getIntent().getData();
        setContentView(R.layout.activity_contact);
        if(ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this,new String[] {Manifest.permission.CALL_PHONE},CALL_PHONE_REQUEST);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new ContactFragmentPagerAdapter(getSupportFragmentManager(), data);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
