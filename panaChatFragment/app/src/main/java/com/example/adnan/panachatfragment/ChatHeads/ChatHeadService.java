package com.example.adnan.panachatfragment.ChatHeads;

//package com.example.adnan.panachatfragment.ChatHeads;
//
//
//import android.app.Dialog;
//import android.app.Service;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.graphics.PixelFormat;
//import android.graphics.Point;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.os.IBinder;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AbsListView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.adnan.panachatfragment.Adaptors.adaptorMessages;
//import com.example.adnan.panachatfragment.R;
//import com.example.adnan.panachatfragment.Signatures.signature_msgs;
//import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
//import com.example.adnan.panachatfragment.UTils.Global;
//import com.example.adnan.panachatfragment.UTils.InterFace;
//import com.firebase.client.ChildEventListener;
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.Query;
//import com.mikhaellopez.circularimageview.CircularImageView;
//import com.squareup.picasso.Picasso;
//
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//
public class ChatHeadService  {
//
//    @SuppressWarnings("deprecation")
//
//    @Override
//    public void onCreate() {
//        // TODO Auto-generated method stub
//        super.onCreate();
//        \adaptor = new adaptorMessages(this, msgs);
    }
//
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // TODO Auto-generated method stub
//        Log.d(Utils.LogTag, "ChatHeadService.onStartCommand() -> startId=" + startId);
//
//        if (intent != null) {
//            Bundle bd = intent.getExtras();
//
//            if (bd != null)
//                sMsg = bd.getString("extra_msg");
//            sUrl = bd.getString("extra_picUrl");
//            sName = bd.getString("extra_name");
//            sId = bd.getString("extra_id");
//            uid = bd.getString("extra_uid");
//
//        }
//
//        if (startId == Service.START_STICKY) {
//            handleStart();
//            return super.onStartCommand(intent, flags, startId);
//        } else {
//            return Service.START_NOT_STICKY;
//        }
//
//    }
//
//    @Override
//    public void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//
//
//    }
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO Auto-generated method stub
//        Log.d(Utils.LogTag, "ChatHeadService.onBind()");
//        return null;
//    }
//
//
//}
