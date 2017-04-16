package com.kalap.contacts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.kalap.contacts.database.ContactsDatabaseHelper;
import com.kalap.contacts.object.Contact;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int MY_READ_CONTACT_PERMISSION_REQUEST = 101;
    ProgressBar loadingContacts;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_READ_CONTACT_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    HashMap<String, Contact> contactHashMap = new HashMap<>();
                    ContentResolver contentResolver = getContentResolver();
                    Cursor cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
                    if (cursor1 != null) {
                        while (cursor1.moveToNext()) {
                            Contact contact;
                            String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phoneNum = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            ArrayList<String> phNumList;
                            if (contactHashMap.containsKey(name)) {
                                contact = contactHashMap.get(name);
                                phNumList = contact.getPhoneNumberList();
                                phNumList.add(phoneNum);
                                contact.setPhoneNumberList(phNumList);
                                contactHashMap.put(name,contact);
                            } else {
                                contact = new Contact();
                                contact.setName(name);
                                phNumList = new ArrayList<>();
                                phNumList.add(phoneNum);
                                contact.setPhoneNumberList(phNumList);
                                contactHashMap.put(name,contact);
                            }
                        }
                        cursor1.close();
                    }
                    for (String name : contactHashMap.keySet()) {
                        ContactsDatabaseHelper helper = new ContactsDatabaseHelper(getApplicationContext());
                        helper.addContact(contactHashMap.get(name));
                    }
                    SharedPreferences sharedPreferences1 = getSharedPreferences("contactsPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putLong("lastFetchTime", System.currentTimeMillis());
                    editor.apply();
                    loadingContacts.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(MainActivity.this,ContactActivity.class));
                    finish();
                }
            }
        }
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
                    HashMap<String, Contact> contactHashMap = new HashMap<>();
                    ContentResolver contentResolver = getContentResolver();
                    Cursor cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
                    if (cursor1 != null) {
                        while (cursor1.moveToNext()) {
                            Contact contact;
                            String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phoneNum = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            ArrayList<String> phNumList;
                            if (contactHashMap.containsKey(name)) {
                                contact = contactHashMap.get(name);
                                phNumList = contact.getPhoneNumberList();
                                phNumList.add(phoneNum);
                                contact.setPhoneNumberList(phNumList);
                            } else {
                                contact = new Contact();
                                contact.setName(name);
                                phNumList = new ArrayList<>();
                                phNumList.add(phoneNum);
                                contact.setPhoneNumberList(phNumList);
                            }
                            contactHashMap.put(name,contact);
                        }
                        cursor1.close();
                    }
                    for (String name : contactHashMap.keySet()) {
                        ContactsDatabaseHelper helper = new ContactsDatabaseHelper(getApplicationContext());
                        helper.addContact(contactHashMap.get(name));
                    }
                    SharedPreferences sharedPreferences1 = getSharedPreferences("contactsPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putLong("lastFetchTime", System.currentTimeMillis());
                    editor.apply();
                    loadingContacts.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(MainActivity.this,ContactActivity.class));
                    finish();
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
}
