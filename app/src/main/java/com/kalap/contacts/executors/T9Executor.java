package com.kalap.contacts.executors;

import android.os.Looper;

import com.kalap.contacts.object.Contact;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class T9Executor {

    private static final ExecutorService THREADPOOL = Executors.newCachedThreadPool();
    private T9SearchCompleteListener listener;

    public void setListener(T9SearchCompleteListener listener) {
        this.listener = listener;
    }

    public void getT9Contacts(final ArrayList<Contact> allContacts, final String phoneNumberStr) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                load(allContacts,phoneNumberStr);
            }
        };
        runButNotOnMainThread(runnable, Looper.getMainLooper().getThread());
    }

    private void runButNotOnMainThread(Runnable toRun, Thread notOn) {
        if (Thread.currentThread() == notOn) {
            THREADPOOL.submit(toRun);
        } else {
            toRun.run();
        }
    }

    private void load(ArrayList<Contact> allContacts, String phoneNumberStr) {
        String t9Pattern = phoneNumberStr
                .replaceAll("1","")
                .replaceAll("2","[abc]")
                .replaceAll("3","[def]")
                .replaceAll("4","[ghi]")
                .replaceAll("5","[jkl]")
                .replaceAll("6","[mno]")
                .replaceAll("7","[pqrs]")
                .replaceAll("8","[tuv]")
                .replaceAll("9","[wxyz]")
                .replaceAll("\\*","")
                .replaceAll("0","")
                .replaceAll("\\+","")
                .replaceAll("#","")
                + ".*";
        TreeMap<String,Contact> displayContacts = new TreeMap<>();
        if (allContacts != null) {
            for (Contact contact: allContacts) {
                if (contact.getName() != null) {
                    if (contact.getName().toLowerCase().matches(t9Pattern)) {
                        displayContacts.put(contact.getName(),contact);
                    }
                    if (displayContacts.size() == 10) {
                        break;
                    }
                }
            }
            if(listener != null) {
                listener.t9SearchCompleted(displayContacts);
            }
        }
    }

    public interface T9SearchCompleteListener {
        void t9SearchCompleted(TreeMap<String,Contact> displayContacts);
    }
}
