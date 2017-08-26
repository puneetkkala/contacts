package com.kalap.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kalap.contacts.adapters.ContactAdapter;
import com.kalap.contacts.database.ContactsDatabaseHelper;
import com.kalap.contacts.object.Contact;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by puneetkkala on 12/04/17.
 */

public class ContactListFragment extends Fragment implements TextWatcher {

    private EditText queryEditText;
    private RecyclerView contactRecyclerView;
    private TreeMap<String,Contact> displayAll;
    private TreeMap<String,Contact> displayList;
    private ContactAdapter contactAdapter;

    public static ContactListFragment newInstance() {

        Bundle args = new Bundle();

        ContactListFragment fragment = new ContactListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void prepareList() {
        ContactsDatabaseHelper helper = new ContactsDatabaseHelper(getActivity());
        ArrayList<Contact> contacts = helper.getAllContacts();
        displayAll = new TreeMap<>();
        for(Contact contact: contacts) {
            displayAll.put(contact.getName(),contact);
        }
        displayList = new TreeMap<>(displayAll);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        queryEditText = (EditText) view.findViewById(R.id.edit_query_text);
        contactRecyclerView = (RecyclerView) view.findViewById(R.id.contacts_recycler_view);
        prepareList();
        contactAdapter = new ContactAdapter(getActivity(),displayList);
        contactRecyclerView.setAdapter(contactAdapter);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        queryEditText.addTextChangedListener(this);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().length() == 0) {
            displayList = new TreeMap<>(displayAll);
        } else {
            displayList = new TreeMap<>();
            for (String name: displayAll.keySet()) {
                if(name.toLowerCase().startsWith(s.toString().toLowerCase())) {
                    displayList.put(name,displayAll.get(name));
                }
            }
        }
        contactAdapter = new ContactAdapter(getActivity(),displayList);
        contactRecyclerView.setAdapter(contactAdapter);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
