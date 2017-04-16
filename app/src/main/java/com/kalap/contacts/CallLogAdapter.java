package com.kalap.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalap.contacts.object.PhoneLog;

import java.util.ArrayList;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder> {

    private ArrayList<PhoneLog> phoneLogs;
    private Context context;

    public CallLogAdapter(Context context, ArrayList<PhoneLog> phoneLogs) {
        this.phoneLogs = phoneLogs;
        this.context = context;
    }

    @Override
    public CallLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_log_row,parent,false);
        return new CallLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CallLogViewHolder holder, int position) {
        if (position > -1 && position < phoneLogs.size()) {
            PhoneLog phoneLog = phoneLogs.get(position);
            if (TextUtils.isEmpty(phoneLog.getName())) {
                holder.contactName.setText(phoneLog.getNumber());
            } else {
                holder.contactName.setText(phoneLog.getName());
            }
            holder.typeDate.setText(phoneLog.getType() + ", " + phoneLog.getDate());
            switch (phoneLog.getType()) {
                case "REJECTED":
                case "INCOMING": {
                    holder.typeDate.setTextColor(Color.parseColor("#2196F3"));
                    break;
                }
                case "OUTGOING": {
                    holder.typeDate.setTextColor(Color.parseColor("#4CAF50"));
                    break;
                }
                case "MISSED": {
                    holder.typeDate.setTextColor(Color.parseColor("#F44336"));
                    break;
                }
                case "VOICEMAIL":
                case "BLOCKED":
                default: {
                    holder.typeDate.setTextColor(Color.BLACK);
                    break;
                }
            }
            holder.duration.setText(phoneLog.getDuration());
            holder.duration.setTextColor(Color.parseColor("#9E9E9E"));
        }
    }

    @Override
    public int getItemCount() {
        return phoneLogs.size();
    }

    public class CallLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView contactName;
        public TextView typeDate;
        public TextView duration;
        public ImageView callImage;

        public CallLogViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            typeDate = (TextView) itemView.findViewById(R.id.type_date);
            duration = (TextView) itemView.findViewById(R.id.duration);
            callImage = (ImageView) itemView.findViewById(R.id.call_image);
            callImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position > -1  && position < phoneLogs.size()) {
                PhoneLog phoneLog = phoneLogs.get(position);
                Intent callIntent = new Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:"+phoneLog.getNumber()));
                context.startActivity(callIntent);
            }
        }
    }
}
