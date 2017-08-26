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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class ContactActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int CALL_PHONE_REQUEST = 102;
    private Uri data;

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
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        changeFragment(0);
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
                changeFragment(0);
                return true;
            }
            case R.id.action_recent: {
                changeFragment(1);
                return true;
            }
            case R.id.action_all: {
                changeFragment(2);
                return true;
            }
        }
        return false;
    }

    private void changeFragment(int position) {
        Fragment newFragment;
        if (position == 0) {
            newFragment = DialerFragment.newInstance(data);
        } else if (position == 1) {
            newFragment = CallLogsFragment.newInstance();
        } else {
            newFragment = ContactListFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).commit();
    }

    @Override
    public void onClick(View v) {
        shareApp();
    }
}
