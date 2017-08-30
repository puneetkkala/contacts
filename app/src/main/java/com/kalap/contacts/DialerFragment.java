package com.kalap.contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.kalap.contacts.adapters.ContactAdapter;
import com.kalap.contacts.database.ContactsDatabaseHelper;
import com.kalap.contacts.object.Contact;

import java.util.ArrayList;
import java.util.TreeMap;

public class DialerFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private TextView phoneNumber;
    private RecyclerView contactsRv;
    private String phoneNumberStr;
    private String t9Pattern = ".*";
    private ArrayList<Contact> allContacts;
    private TreeMap<String,Contact> displayContacts;
    private ContactAdapter adapter;

    public static DialerFragment newInstance(Uri uri) {
        Bundle args = new Bundle();
        if (uri != null) {
            args.putString("data", uri.toString());
        }
        DialerFragment fragment = new DialerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialer_fragment,container,false);
        phoneNumberStr = "";
        TextView _1 = (TextView) view.findViewById(R.id._1);
        TextView _2 = (TextView) view.findViewById(R.id._2);
        TextView _3 = (TextView) view.findViewById(R.id._3);
        TextView _4 = (TextView) view.findViewById(R.id._4);
        TextView _5 = (TextView) view.findViewById(R.id._5);
        TextView _6 = (TextView) view.findViewById(R.id._6);
        TextView _7 = (TextView) view.findViewById(R.id._7);
        TextView _8 = (TextView) view.findViewById(R.id._8);
        TextView _9 = (TextView) view.findViewById(R.id._9);
        TextView _0 = (TextView) view.findViewById(R.id._0);
        TextView star = (TextView) view.findViewById(R.id._star);
        TextView hash = (TextView) view.findViewById(R.id._hash);
        TextView plus = (TextView) view.findViewById(R.id._plus);
        ImageView call = (ImageView) view.findViewById(R.id._call);
        ImageView backspace = (ImageView) view.findViewById(R.id.backspace);
        phoneNumber = (TextView) view.findViewById(R.id.phone_number);
        contactsRv = (RecyclerView) view.findViewById(R.id.contacts_rv);
        _1.setOnClickListener(this);
        _2.setOnClickListener(this);
        _3.setOnClickListener(this);
        _4.setOnClickListener(this);
        _5.setOnClickListener(this);
        _6.setOnClickListener(this);
        _7.setOnClickListener(this);
        _8.setOnClickListener(this);
        _9.setOnClickListener(this);
        _0.setOnClickListener(this);
        star.setOnClickListener(this);
        hash.setOnClickListener(this);
        plus.setOnClickListener(this);
        call.setOnClickListener(this);
        backspace.setOnClickListener(this);
        backspace.setOnLongClickListener(this);
        phoneNumber.setOnClickListener(this);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_HIDDEN);
        Bundle b = getArguments();
        if (b != null) {
            String data = b.getString("data");
            if (data != null) {
                data = data.substring(4);
                phoneNumberStr = data;
                phoneNumber.setText(phoneNumberStr);
            }
        }
        ContactsDatabaseHelper helper = new ContactsDatabaseHelper(getActivity());
        allContacts = helper.getAllContacts();
        displayContacts = new TreeMap<>();
        contactsRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        adapter = new ContactAdapter(getActivity(),displayContacts);
        contactsRv.setAdapter(adapter);
        return view;
    }

    private void matchPattern() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                displayContacts.clear();
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
                }
                adapter.updateData(displayContacts);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id._1: {
                phoneNumberStr += "1";
                break;
            }
            case R.id._2: {
                phoneNumberStr += "2";
                t9Pattern = t9Pattern.substring(0,t9Pattern.length() - 2) + "[abc].*";
                matchPattern();
                break;
            }
            case R.id._3: {
                phoneNumberStr += "3";
                t9Pattern = t9Pattern.substring(0,t9Pattern.length() - 2) + "[def].*";
                matchPattern();
                break;
            }
            case R.id._4: {
                phoneNumberStr += "4";
                t9Pattern = t9Pattern.substring(0,t9Pattern.length() - 2) + "[ghi].*";
                matchPattern();
                break;
            }
            case R.id._5: {
                phoneNumberStr += "5";
                t9Pattern = t9Pattern.substring(0,t9Pattern.length() - 2) + "[jkl].*";
                matchPattern();
                break;
            }
            case R.id._6: {
                phoneNumberStr += "6";
                t9Pattern = t9Pattern.substring(0,t9Pattern.length() - 2) + "[mno].*";
                matchPattern();
                break;
            }
            case R.id._7: {
                phoneNumberStr += "7";
                t9Pattern = t9Pattern.substring(0,t9Pattern.length() - 2) + "[pqrs].*";
                matchPattern();
                break;
            }
            case R.id._8: {
                phoneNumberStr += "8";
                t9Pattern = t9Pattern.substring(0,t9Pattern.length() - 2) + "[tuv].*";
                matchPattern();
                break;
            }
            case R.id._9: {
                phoneNumberStr += "9";
                t9Pattern = t9Pattern.substring(0,t9Pattern.length() - 2) + "[wxyz].*";
                matchPattern();
                break;
            }
            case R.id._0: {
                phoneNumberStr += "0";
                break;
            }
            case R.id._star: {
                phoneNumberStr += "*";
                break;
            }
            case R.id._hash: {
                phoneNumberStr += "#";
                break;
            }
            case R.id._plus: {
                phoneNumberStr += "+";
                break;
            }
            case R.id._call: {
                if (phoneNumberStr.length() > 0) {
                    String data = "tel:" + phoneNumberStr;
                    Intent callIntent = new Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse(data));
                    getActivity().startActivity(callIntent);
                    phoneNumberStr = "";
                    phoneNumber.setText("");
                    try {
                        Answers.getInstance().logCustom(new CustomEvent("CALL BUTTON CLICKED")
                                .putCustomAttribute("source","DialerFragment")
                                .putCustomAttribute("version",BuildConfig.VERSION_NAME));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            case R.id.backspace: {
                if (phoneNumberStr.length() > 0) {
                    phoneNumberStr = phoneNumberStr.substring(0, phoneNumberStr.length() - 1);
                    if (t9Pattern.length() > 2) {
                        int start = t9Pattern.lastIndexOf('[');
                        t9Pattern = t9Pattern.substring(0,start) + ".*";
                        matchPattern();
                    }
                }
                break;
            }
        }
        phoneNumber.setText(phoneNumberStr);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.backspace: {
                phoneNumberStr = "";
                phoneNumber.setText("");
                t9Pattern = ".*";
                displayContacts.clear();
                return true;
            }
        }
        return false;
    }
}
