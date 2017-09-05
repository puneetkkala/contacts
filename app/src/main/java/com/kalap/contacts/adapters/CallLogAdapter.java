package com.kalap.contacts.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.kalap.contacts.BuildConfig;
import com.kalap.contacts.R;
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
            String typeDate;
            if (phoneLog.getType() != null) {
                switch (phoneLog.getType()) {
                    case "Rejected":
                    case "Missed": {
                        typeDate = phoneLog.getType();
                        holder.typeDate.setTextColor(Color.parseColor("#F44336"));
                        break;
                    }
                    default: {
                        typeDate = phoneLog.getType() + ", " + phoneLog.getDuration();
                        holder.typeDate.setTextColor(Color.parseColor("#8B99A3"));
                        break;
                    }
                }
            } else {
                typeDate = phoneLog.getType();
                holder.typeDate.setTextColor(Color.parseColor("#8B99A3"));
            }
            holder.typeDate.setText(typeDate);
            holder.duration.setText(phoneLog.getDate());
            holder.duration.setTextColor(Color.parseColor("#8B99A3"));
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
        public FloatingActionButton callImage;

        public CallLogViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            typeDate = (TextView) itemView.findViewById(R.id.type_date);
            duration = (TextView) itemView.findViewById(R.id.duration);
            callImage = (FloatingActionButton) itemView.findViewById(R.id.call_image);
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
                try {
                    Answers.getInstance().logCustom(new CustomEvent("CALL BUTTON CLICKED")
                            .putCustomAttribute("source","CallLogFragment")
                            .putCustomAttribute("version", BuildConfig.VERSION_NAME));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
