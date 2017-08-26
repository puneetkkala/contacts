package com.kalap.contacts.listeners;

import com.kalap.contacts.object.PhoneLog;

import java.util.ArrayList;

/**
 * Created by puneetkkala on 27/08/17.
 */

public interface CallLogLoadListener {
    void onCallLogLoaded(ArrayList<PhoneLog> phoneLogs);
}
