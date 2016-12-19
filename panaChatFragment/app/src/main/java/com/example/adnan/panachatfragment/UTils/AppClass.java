package com.example.adnan.panachatfragment.UTils;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by adnan on 6/30/2016.
 */
public class AppClass extends Application {
    //    public static String adnan = "adnasd";
    public static String uid;

    @Override
    public void onCreate() {
        super.onCreate();
//        Firebase.setAndroidContext(getApplicationContext());
//        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
    }
}
