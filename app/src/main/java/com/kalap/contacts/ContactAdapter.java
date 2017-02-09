package com.kalap.contacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kalap.contacts.dialog.PhoneNumberDialog;
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
        public ImageView contactIcon;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            contactName.setOnClickListener(this);
            contactIcon = (ImageView) itemView.findViewById(R.id.contact_icon);
            contactIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getPosition();
            if(position > -1 && position < contactTreeMap.size()) {
                Contact contact = contactTreeMap.get(contactName.getText().toString());
                if(contact.getPhoneNumberList() != null && contact.getPhoneNumberList().size() > 0) {
                    if(contact.getPhoneNumberList().size() == 1) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumberList().get(0)));
                        activity.startActivity(callIntent);
                    } else {
                        PhoneNumberDialog phoneNumberDialog = new PhoneNumberDialog(activity, contact);
                        phoneNumberDialog.setCancelable(true);
                        phoneNumberDialog.show();
                    }
                } else {
                    Toast toast = Toast.makeText(activity,"Phone number not available for this contact",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        }
    }


}
