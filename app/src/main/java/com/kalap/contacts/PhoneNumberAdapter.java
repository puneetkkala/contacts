package com.kalap.contacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.PhoneNumberViewHolder> {

    private Activity activity;
    private ArrayList<String> phoneNumbers;

    public PhoneNumberAdapter(Activity activity, ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        this.activity = activity;
    }

    @Override
    public PhoneNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_phone_num_row,parent,false);
        return new PhoneNumberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhoneNumberViewHolder holder, int position) {
        if(position > -1 && position < phoneNumbers.size()) {
            holder.phoneNumberTv.setText(phoneNumbers.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return phoneNumbers.size();
    }

    public class PhoneNumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView phoneNumberTv;
        public ImageView contactIcon;

        public PhoneNumberViewHolder(View itemView) {
            super(itemView);
            phoneNumberTv = (TextView) itemView.findViewById(R.id.contact_number);
            phoneNumberTv.setOnClickListener(this);
            contactIcon = (ImageView) itemView.findViewById(R.id.contact_icon);
            contactIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position > -1  && position < phoneNumbers.size()) {
                Intent callIntent = new Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:"+phoneNumbers.get(position)));
                activity.startActivity(callIntent);
            }
        }
    }
}
