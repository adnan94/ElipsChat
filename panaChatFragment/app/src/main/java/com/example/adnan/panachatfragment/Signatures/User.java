package com.example.adnan.panachatfragment.Signatures;

import java.util.HashMap;

/**
 * Created by Adnan on 1/9/2016.
 */
public class User {
    String Name;
    String PicUrl;
    String Status;
    String Email;
    String Contact;
    String Birthday;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String name, String picUrl, String status, String email, String contact, String birthday, String uid) {
        Name = name;
        PicUrl = picUrl;
        Status = status;
        Email = email;
        Contact = contact;
        Birthday = birthday;
        this.uid = uid;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public User() {

    }
}
