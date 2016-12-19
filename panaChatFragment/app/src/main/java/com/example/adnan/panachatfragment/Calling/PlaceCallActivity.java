package com.example.adnan.panachatfragment.Calling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.UTils.Global;
import com.sinch.android.rtc.calling.Call;

public class PlaceCallActivity extends BaseActivity {
    public static Activity acti;
    private Button mCallButton;
    private EditText mCallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        acti = PlaceCallActivity.this;
        mCallName = (EditText) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setOnClickListener(buttonClickListener);

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {

        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(getSinchServiceInterface().getUserName());
        mCallButton.setEnabled(true);
        callButtonClicked(Global.PartnaerId);

    }



    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
            Log.d("Tagi", "palce destroy");
        }
        super.onDestroy();
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked(String name) {
        String userName = name;
        if (userName.isEmpty()) {
            return;
        }
        Call call = getSinchServiceInterface().callUser(userName);
        String callId = call.getCallId();
///
//        finish();
        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);

    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    break;

                case R.id.stopButton:
                    stopButtonClicked();
                    break;

            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Tagi", "palce start");

    }

}
