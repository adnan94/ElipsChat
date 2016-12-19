package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 1/18/2016.
 */
public class groupsListSignature {
    String picUrl;
    String admin;


    public groupsListSignature(String picUrl, String admin, String groupName) {
        this.picUrl = picUrl;
        this.admin = admin;
        this.groupName = groupName;
    }

    public String getGroupName() {

        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    String groupName;

    public groupsListSignature() {
    }

}
