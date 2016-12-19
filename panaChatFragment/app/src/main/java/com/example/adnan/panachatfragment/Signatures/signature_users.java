package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 10/20/2015.
 */
public class signature_users {
    String names;
    String images;

    public signature_users(String names, String images) {
        this.names = names;
        this.images = images;
    }

    public signature_users() {
    }

    public String getNames() {

        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
