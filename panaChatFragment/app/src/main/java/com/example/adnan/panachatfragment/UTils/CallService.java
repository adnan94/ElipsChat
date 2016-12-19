package com.example.adnan.panachatfragment.UTils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.example.adnan.panachatfragment.Calling.SinchService;
import com.sinch.android.rtc.SinchError;

/**
 * Created by Adnan on 8/29/2016.
 */
public class CallService extends Service implements SinchService.StartFailedListener, ServiceConnection {
    public static SinchService.SinchServiceInterface mSinchServiceInterface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getApplicationContext().bindService(new Intent(this, SinchService.class), this,
                BIND_AUTO_CREATE);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(),"CAll Service ",Toast.LENGTH_SHORT).show();

        super.onCreate();

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (SinchService.class.getName().equals(name.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) service;

            onServiceConnected();

        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (SinchService.class.getName().equals(name.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }

    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    public static SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }
}
