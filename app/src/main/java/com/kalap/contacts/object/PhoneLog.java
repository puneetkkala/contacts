package com.kalap.contacts.object;

/**
 * Created by puneetkkala on 12/04/17.
 */

public class PhoneLog {

    public String name;
    public String number;
    public String type;
    public String date;
    public String duration;

    public PhoneLog() {
        name = "";
        number = "";
        type = "";
        date = "";
        duration = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
