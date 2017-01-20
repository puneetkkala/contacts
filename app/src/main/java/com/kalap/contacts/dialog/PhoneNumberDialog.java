package com.kalap.contacts.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.kalap.contacts.PhoneNumberAdapter;
import com.kalap.contacts.R;
import com.kalap.contacts.object.Contact;

import java.util.ArrayList;

public class PhoneNumberDialog extends Dialog implements PhoneNumberAdapter.OnClickListener {

    private Activity activity;
    private RecyclerView phoneNumberList;
    private Contact contact;
    private ArrayList<String> phoneNumbers;

    public PhoneNumberDialog(Activity activity, Contact contact) {
        super(activity);
        this.activity = activity;
        this.contact  = contact;
        this.phoneNumbers = contact.getPhoneNumberList();
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone_number_dialog);
        phoneNumberList = (RecyclerView) findViewById(R.id.phone_number_list);
        PhoneNumberAdapter phoneNumberAdapter = new PhoneNumberAdapter(activity,phoneNumbers);
        phoneNumberAdapter.setOnClickListener(this);
        phoneNumberList.setAdapter(phoneNumberAdapter);
        phoneNumberList.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void onClick() {
        dismiss();
    }
}
