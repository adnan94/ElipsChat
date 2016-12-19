package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 1/3/2016.
 */
public class Search_Signature {
    String Name;
    String picUrl;


    public Search_Signature(String name, String picUrl) {
        Name = name;
        this.picUrl = picUrl;
    }

    public String getPicUrl() {

        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Search_Signature() {
    }

    public Search_Signature(String name) {

        Name = name;
    }

    public String getName() {

        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
