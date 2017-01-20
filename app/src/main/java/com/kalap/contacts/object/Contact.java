package com.kalap.contacts.object;

import java.util.ArrayList;

public class Contact {
    String id;
    String name;
    ArrayList<String> phoneNumberList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(ArrayList<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }
}
