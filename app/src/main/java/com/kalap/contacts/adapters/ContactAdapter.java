package com.kalap.contacts.adapters;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kalap.contacts.R;
import com.kalap.contacts.object.Contact;

import java.util.ArrayList;
import java.util.TreeMap;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Activity activity;
    private TreeMap<String,Contact> contactTreeMap;
    private ArrayList<String> contactNames;

    public ContactAdapter(Activity activity, TreeMap<String,Contact> contactTreeMap) {
        this.activity = activity;
        this.contactTreeMap = contactTreeMap;
        this.contactNames = new ArrayList<>(contactTreeMap.keySet());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_contact_row,parent,false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        if(position > -1 && position < contactTreeMap.size()) {
            holder.contactName.setText(contactNames.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return contactTreeMap.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView contactName;
        public RecyclerView phoneList;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            contactName.setOnClickListener(this);
            phoneList = (RecyclerView) itemView.findViewById(R.id.phone_number_list);
        }

        @Override
        public void onClick(View view) {
            if (phoneList.getVisibility() == View.VISIBLE) {
                phoneList.setVisibility(View.GONE);
            } else {
                phoneList.setVisibility(View.VISIBLE);
                Contact contact = contactTreeMap.get(contactName.getText().toString());
                if (contact.getPhoneNumberList() != null && contact.getPhoneNumberList().size() > 0) {
                    PhoneNumberAdapter phoneNumberAdapter = new PhoneNumberAdapter(activity,contact.getPhoneNumberList());
                    phoneList.setAdapter(phoneNumberAdapter);
                    phoneList.setLayoutManager(new LinearLayoutManager(activity));
                } else {
                    Snackbar snackbar = Snackbar.make(phoneList,"Phone number not available for this contact",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }
    }


}
