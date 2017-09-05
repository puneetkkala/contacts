package com.kalap.contacts.executors;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Looper;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import com.kalap.contacts.listeners.CallLogLoadListener;
import com.kalap.contacts.object.PhoneLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallLogExecutor {

    private static final ExecutorService THREADPOOL = Executors.newCachedThreadPool();
    private CallLogLoadListener listener;

    public void loadCallLogs(final Activity context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                load(context);
            }
        };
        runButNotOnMainThread(runnable, Looper.getMainLooper().getThread());
    }

    public void setListener(CallLogLoadListener listener) {
        this.listener = listener;
    }

    private void load(Activity context) {
        ArrayList<PhoneLog> phoneLogs = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.READ_CALL_LOG},103);
        } else {
            Cursor callLogsCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC", null);
            if (callLogsCursor != null) {
                while (callLogsCursor.moveToNext()) {
                    PhoneLog phoneLog = new PhoneLog();
                    phoneLog.setName(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                    phoneLog.setNumber(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    phoneLog.setType(getType(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.TYPE))));
                    String date = callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.DATE));
                    try {
                        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.FULL);
                        format.applyPattern("dd-MM-yyyy hh:mm:ss");
                        String localDateTime = format.format(new Date(Long.valueOf(date)));
                        phoneLog.setDate(localDateTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String duration = calculateDuration(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.DURATION)));
                    phoneLog.setDuration(duration);
                    phoneLogs.add(phoneLog);
                }
                callLogsCursor.close();
            }
            if (listener != null) {
                listener.onCallLogLoaded(phoneLogs);
            }
        }
    }

    private String getType(String typeNum) {
        switch (Integer.valueOf(typeNum)) {
            case 1:
                return "Incoming";
            case 2:
                return "Outgoing";
            case 3:
                return "Missed";
            case 4:
                return "Voicemail";
            case 5:
                return "Rejected";
            case 6:
                return "Blocked";
            default:
                return null;
        }
    }

    private String calculateDuration(String duration) {
        int durationSec = Integer.valueOf(duration);
        int min = durationSec / 60;
        int sec = durationSec % 60;
        if (min >= 60) {
            int hour = min / 60;
            min %= 60;
            return  hour + "h " + min + "m " + sec + "s";
        } else if (min == 0) {
            return sec + "s";
        } else {
            return min + "m " + sec + "s";
        }
    }

    private void runButNotOnMainThread(Runnable toRun, Thread notOn) {
        if (Thread.currentThread() == notOn) {
            THREADPOOL.submit(toRun);
        } else {
            toRun.run();
        }
    }
}
