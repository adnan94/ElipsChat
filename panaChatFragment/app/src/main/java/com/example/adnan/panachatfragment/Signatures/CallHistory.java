package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 8/29/2016.
 */
public class CallHistory {
    String name,status,picUrl;
    String id,date;

    public CallHistory() {
    }

    public String getName() {

        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getId() {
        return id;
    }

    public CallHistory(String name, String status, String picUrl, String id,String date) {

        this.name = name;
        this.status = status;
        this.picUrl = picUrl;
        this.id = id;
        this.date=date;

    }
    public String getDate() {
        return date;
    }

}
