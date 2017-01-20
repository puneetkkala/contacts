package com.kalap.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.kalap.contacts.database.ContactsDatabaseHelper;
import com.kalap.contacts.object.Contact;

import java.util.ArrayList;
import java.util.TreeMap;

public class ContactActivity extends AppCompatActivity implements TextWatcher {

    public static final String TAG = "ContactActivity";
    private static final int CALL_PHONE_REQUEST = 102;

    private EditText queryEditText;
    private RecyclerView contactRecyclerView;
    private TreeMap<String,Contact> displayAll;
    private TreeMap<String,Contact> displayList;
    private ContactAdapter contactAdapter;

    private void prepareList() {
        ContactsDatabaseHelper helper = new ContactsDatabaseHelper(getApplicationContext());
        ArrayList<Contact> contacts = helper.getAllContacts();
        displayAll = new TreeMap<>();
        for(Contact contact: contacts) {
            displayAll.put(contact.getName(),contact);
        }
        displayList = new TreeMap<>(displayAll);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_contact);
        queryEditText = (EditText) findViewById(R.id.edit_query_text);
        contactRecyclerView = (RecyclerView) findViewById(R.id.contacts_recycler_view);
        prepareList();
        contactAdapter = new ContactAdapter(ContactActivity.this,displayList);
        contactRecyclerView.setAdapter(contactAdapter);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(ContactActivity.this));
        queryEditText.addTextChangedListener(this);
        if(ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this,new String[] {Manifest.permission.CALL_PHONE},CALL_PHONE_REQUEST);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if(s.toString().length() == 0) {
            displayList = new TreeMap<>(displayAll);
        } else {
            displayList = new TreeMap<>();
            for (String name: displayAll.keySet()) {
                if(name.toLowerCase().startsWith(s.toString().toLowerCase())) {
                    displayList.put(name,displayAll.get(name));
                }
            }
        }
        contactAdapter = new ContactAdapter(ContactActivity.this,displayList);
        contactRecyclerView.setAdapter(contactAdapter);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(ContactActivity.this));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
