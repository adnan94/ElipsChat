package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 1/5/2016.
 */
public class Frient_Req_Signature {
    String name;
    String picUrl;
    String senderId;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Frient_Req_Signature(String name, String picUrl, String senderId) {
        this.name = name;

        this.picUrl = picUrl;
        this.senderId = senderId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Frient_Req_Signature() {

    }
}
