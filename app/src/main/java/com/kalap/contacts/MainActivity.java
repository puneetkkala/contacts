package com.kalap.contacts;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.kalap.contacts.executors.ContactExecutor;
import com.kalap.contacts.listeners.ContactsLoadListener;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements ContactsLoadListener {

    private static final int MY_READ_CONTACT_PERMISSION_REQUEST = 101;
    ProgressBar loadingContacts;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_READ_CONTACT_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    execute();
                }
            }
        }
    }

    private void execute() {
        ContactExecutor executor = new ContactExecutor();
        executor.setListener(this);
        executor.loadContacts(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        });
        setContentView(R.layout.activity_main);
        TextView preparingApp = (TextView) findViewById(R.id.preparing_text_view);
        loadingContacts = (ProgressBar) findViewById(R.id.loading_contacts_bar);
        loadingContacts.setVisibility(View.VISIBLE);
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("contactsPreferences",MODE_PRIVATE);
            long time = sharedPreferences.getLong("lastFetchTime",0);
            if(System.currentTimeMillis() > time + 7 * 24 * 60 * 60 * 1000) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_CONTACTS},MY_READ_CONTACT_PERMISSION_REQUEST);
                } else {
                    ContactExecutor executor = new ContactExecutor();
                    executor.loadContacts(this);
                }
            } else {
                preparingApp.setText("Loading...");
                loadingContacts.setVisibility(View.INVISIBLE);
                startActivity(new Intent(MainActivity.this,ContactActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onContactsLoaded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingContacts.setVisibility(View.INVISIBLE);
                startActivity(new Intent(MainActivity.this,ContactActivity.class));
                finish();
            }
        });
    }
}
