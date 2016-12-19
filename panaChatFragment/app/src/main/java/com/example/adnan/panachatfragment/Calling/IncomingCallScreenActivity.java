package com.example.adnan.panachatfragment.Calling;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.CallHistory;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class IncomingCallScreenActivity extends BaseActivity {

    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private String mCallId;
    String name, namee, picUrl;
    TextView remoteUser;
    CircularImageView callerImage;
    private AudioPlayer mAudioPlayer;
    DatabaseReference path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming);

        path = Service.fire;
        Button answer = (Button) findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = (Button) findViewById(R.id.declineButton);
        decline.setOnClickListener(mClickListener);

        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    @Override
    protected void onServiceConnected() {

        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {

            call.addCallListener(new SinchCallListener());
            remoteUser = (TextView) findViewById(R.id.remoteUser);
            name = call.getRemoteUserId();
            callerImage = (CircularImageView) findViewById(R.id.callerImage);
//

            FireBaseHandler.getInstance().getCallerName(call.getRemoteUserId(), new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
                @Override
                public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    Log.d("Logi", dataSnapshot.getValue().toString());
                    Log.d("Logi", dataSnapshot.child("name").getValue().toString());
                    Log.d("Logi", dataSnapshot.child("picUrl").getValue().toString());
                    namee = dataSnapshot.child("name").getValue().toString();
                    picUrl = dataSnapshot.child("picUrl").getValue().toString();
                    Picasso.with(IncomingCallScreenActivity.this).with(IncomingCallScreenActivity.this).load(picUrl).error(R.drawable.userdefaul).placeholder(R.drawable.userdefaul).into(callerImage);

                    String date = Date();
                    DatabaseReference firebase = path.child("AppData").child("CallHistory").child(Global.uid);
                    firebase.push().setValue(new CallHistory(namee, "Incoming", picUrl, name, date));

                    remoteUser.setText(namee);

                }

                @Override
                public void fail(FirebaseError obj) {

                }
            });



        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallScreenActivity.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    public String Date() {
        String date = DateFormat.getDateTimeInstance().format(new Date());
        //24 hour format


        return date;

    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        try {
            PlaceCallActivity.acti.finish();
        } catch (Exception e) {

        }
        finish();

    }


    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            mAudioPlayer.stopRingtone();

            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    answerClicked();
                    break;
                case R.id.declineButton:
                    declineClicked();
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Tagi", "i call screen start");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Tagi", "i call screen destroy");

    }
}
