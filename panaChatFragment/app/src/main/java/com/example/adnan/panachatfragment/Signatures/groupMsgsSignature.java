package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 1/17/2016.
 */
public class groupMsgsSignature {
    String name,picUrl,msg,date,id,sharePic;

    public String getSharePic() {
        return sharePic;
    }

    public void setSharePic(String sharePic) {
        this.sharePic = sharePic;
    }

    public groupMsgsSignature(String name, String picUrl, String msg, String date, String id, String sharePic) {

        this.name = name;
        this.picUrl = picUrl;
        this.msg = msg;
        this.date = date;
        this.id = id;
        this.sharePic = sharePic;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public groupMsgsSignature() {

    }
}
