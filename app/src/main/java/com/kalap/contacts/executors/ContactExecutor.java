package com.kalap.contacts.executors;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Looper;
import android.provider.ContactsContract;

import com.kalap.contacts.database.ContactsDatabaseHelper;
import com.kalap.contacts.listeners.ContactsLoadListener;
import com.kalap.contacts.object.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by puneetkkala on 27/08/17.
 */

public class ContactExecutor {

    private static final ExecutorService THREADPOOL = Executors.newCachedThreadPool();
    private ContactsLoadListener listener;

    public void loadContacts(final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                load(context);
            }
        };
        runButNotOnMainThread(runnable, Looper.getMainLooper().getThread());
    }

    public void setListener(ContactsLoadListener listener) {
        this.listener = listener;
    }

    private void load(Context context) {
        HashMap<String, Contact> contactHashMap = new HashMap<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                Contact contact;
                String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNum = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNum = phoneNum.replaceAll("-","").replaceAll(" ","");
                ArrayList<String> phNumList;
                if (contactHashMap.containsKey(name)) {
                    contact = contactHashMap.get(name);
                    phNumList = contact.getPhoneNumberList();
                    if (!phNumList.contains(phoneNum)) {
                        phNumList.add(phoneNum);
                    }
                    contact.setPhoneNumberList(phNumList);
                    contactHashMap.put(name,contact);
                } else {
                    contact = new Contact();
                    contact.setName(name);
                    phNumList = new ArrayList<>();
                    phNumList.add(phoneNum);
                    contact.setPhoneNumberList(phNumList);
                    contactHashMap.put(name,contact);
                }
            }
            cursor1.close();
        }
        for (String name : contactHashMap.keySet()) {
            ContactsDatabaseHelper helper = new ContactsDatabaseHelper(context);
            helper.addContact(contactHashMap.get(name));
        }
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("contactsPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putLong("lastFetchTime", System.currentTimeMillis());
        editor.apply();
        if (listener != null) {
            listener.onContactsLoaded();
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
