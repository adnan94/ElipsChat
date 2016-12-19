package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 10/20/2015.
 */
public class signature_msgs {
    String message;
    String uids;
    String date;
    String url;


    public signature_msgs(String message, String uids, String date, String url) {

        this.message = message;
        this.uids = uids;
        this.date = date;
        this.url = url;

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUids() {
        return uids;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUids(String uids) {
        this.uids = uids;
    }

    public signature_msgs() {

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}