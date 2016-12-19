package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 1/31/2016.
 */
public class signature_status_picurl {
    String picUrl;
    String status;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public signature_status_picurl(String picUrl, String status) {

        this.picUrl = picUrl;
        this.status = status;
    }
}
