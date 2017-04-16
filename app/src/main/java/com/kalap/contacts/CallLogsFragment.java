package com.kalap.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalap.contacts.object.PhoneLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CallLogsFragment extends Fragment {

    public static CallLogsFragment newInstance() {

        Bundle args = new Bundle();

        CallLogsFragment fragment = new CallLogsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String getType(String typeNum) {
        switch (Integer.valueOf(typeNum)) {
            case 1:
                return "INCOMING";
            case 2:
                return "OUTGOING";
            case 3:
                return "MISSED";
            case 4:
                return "VOICEMAIL";
            case 5:
                return "REJECTED";
            case 6:
                return "BLOCKED";
            default:
                return null;
        }
    }

    private String getTimeUnit(int num) {
        return String.valueOf(num).length() == 1 ? "0" + num : String.valueOf(num);
    }

    private String calculateDuration(String duration) {
        int durationSec = Integer.valueOf(duration);
        int min = durationSec / 60;
        int sec = durationSec % 60;
        if (min >= 60) {
            int hour = min / 60;
            min %= 60;
            return  getTimeUnit(hour)+ ":" + getTimeUnit(min) + ":" + getTimeUnit(sec);
        } else {
            return "00:" + getTimeUnit(min) + ":" + getTimeUnit(sec);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_logs_fragment,container,false);
        RecyclerView callLogs = (RecyclerView) view.findViewById(R.id.call_logs);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            ArrayList<PhoneLog> phoneLogs = new ArrayList<>();
            Cursor callLogsCursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC", null);
            if (callLogsCursor != null) {
                while (callLogsCursor.moveToNext()) {
                    PhoneLog phoneLog = new PhoneLog();
                    phoneLog.setName(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                    phoneLog.setNumber(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    phoneLog.setType(getType(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.TYPE))));
                    String date = callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.DATE));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss", Locale.getDefault());
                    String localDateTime = format.format(new Date(Long.valueOf(date)));
                    phoneLog.setDate(localDateTime);
                    String duration = calculateDuration(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.DURATION)));
                    phoneLog.setDuration(duration);
                    phoneLogs.add(phoneLog);
                }
                callLogsCursor.close();
                CallLogAdapter callLogAdapter = new CallLogAdapter(getActivity(), phoneLogs);
                callLogs.setAdapter(callLogAdapter);
                callLogs.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                callLogs.setVisibility(View.GONE);
            }
        } else {
            callLogs.setVisibility(View.GONE);
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_CALL_LOG},102);
        }
        return view;
    }
}
