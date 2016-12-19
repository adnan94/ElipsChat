package com.example.adnan.panachatfragment.Signatures;

/**
 * Created by Adnan on 10/21/2015.
 */
public class signature_data {
    String picture;
    String name;
    String gender;
    String email;
    String password;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public signature_data() {

    }

    public signature_data(String picture, String name, String gender, String email, String password) {
        this.picture = picture;
        this.name = name;
        this.gender = gender;

        this.email = email;
        this.password = password;
    }
}
