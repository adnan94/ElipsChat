package com.example.adnan.panachatfragment.UTils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adnan.panachatfragment.Activities.AfterLoginScreen;
import com.example.adnan.panachatfragment.Activities.ChatActivity;
import com.example.adnan.panachatfragment.Adaptors.adaptorMessages;
import com.example.adnan.panachatfragment.ChatHeads.*;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.signature_msgs;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.client.collection.LLRBNode;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adnan Ahmed on 2/23/2016.
 */
public class Service extends android.app.Service {
    String uid, nameUser;
    String ChatActivity;
    SharedPreferences pref;
    String picUrl;
    SharedPreferences pref2;
    private WindowManager windowManager;
    private RelativeLayout chatheadView, removeView;
    private LinearLayout txtView, txt_linearlayout;
    private ImageView removeImg;
    CircularImageView chatheadImg;
    private TextView txt1;
    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;
    private Point szWindow = new Point();
    private boolean isLeft = true;
    private String sMsg = "";
    String sUrl, sName, sId;
    ArrayList<signature_msgs> msgs;
    adaptorMessages adaptor;
    public static DatabaseReference fire;
    Bitmap bit;
    int notifyID = 1234;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

    }

    @Override
    public void onCreate() {

        fire=FirebaseDatabase.getInstance().getReference();
        fire.keepSynced(true);
        msgs = new ArrayList<>();


        pref = getApplicationContext().getSharedPreferences("SignInData", Context.MODE_PRIVATE);

        uid = pref.getString("uid", "uid");
        nameUser = pref.getString("name", "name");
        picUrl = pref.getString("picUrl", "picUrl");

        if (uid == null) {
            Log.d("Lol", uid);
            stopSelf();

        }
        requestNotification();
        messageNotification();
        groupNotification();


        adaptor = new adaptorMessages(this, msgs, uid);

        super.onCreate();

    }


    public void messageNotification() {
        Log.d("Lol", uid);
        fire.child("AppData").child("Notifications").child("Messages").child(uid).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot != null) {
                    Log.d("DataSnapShot", dataSnapshot.getValue().toString());
                    String id = dataSnapshot.child("senderId").getValue().toString();
                    String message = dataSnapshot.child("message").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String picUrl = dataSnapshot.child("picUrl").getValue().toString();
                    NotificationMessage(name, message, picUrl, id);


                }
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void groupNotification() {
        fire.child("AppData").child("Notifications").child("Groups").addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot != null) {
                    Log.d("DataSnapShot", dataSnapshot.getValue().toString());
                    final String message = dataSnapshot.child("message").getValue().toString();
                    final String groupName = dataSnapshot.child("groupName").getValue().toString();
                    final String picUrl = dataSnapshot.child("picUrl").getValue().toString();
                    final String senderUid = dataSnapshot.child("sender").getValue().toString();
                    final String name = dataSnapshot.child("name").getValue().toString();
//                    Log.d("LolM", message);
//                    Log.d("LolM", groupName);
//                    Log.d("LogM", picUrl);
//                    Log.d("LolM", senderUid);
//                    Log.d("LolM", name);
                    fire.child("AppData").child("MyGroups").child(uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(groupName)) {
                                if (senderUid.equals(uid)) {
                                    Log.d("LOGIister", "equals");
                                } else {
                                    Log.d("LOGIister", "not equals");
                                    NotificationGroup(name, groupName, message, picUrl);

                                }

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }






























    public void requestNotification() {
//
        fire.child("AppData").child("Notifications").child("Request").child(uid).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    String namee = dataSnapshot.child("name").getValue().toString();
                    String picUrl = dataSnapshot.child("picUrl").getValue().toString();
                    notificationRequest(namee, picUrl);
                }
//
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notificationRequest(String name, String picUrl) {
        Bitmap bitmap = loadBitmap(picUrl);
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.adnan.panachatfragment");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setTicker("Office Work")
                .setContentTitle("Friend Request")
                .setContentText(name + " want to be your friend ")
                .setTicker("Request")
                .setSmallIcon(R.drawable.ddddddd)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{500, 500})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .build();
        notificationManager.notify(notifyID++, notification);
        fire.child("AppData").child("Notifications").child("Request").child(uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void NotificationGroup(String name, String groupName, String message, String picUrl) {
        Bitmap bitmap = loadBitmap(picUrl);
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.adnan.panachatfragment");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setTicker("Office Work")
                .setContentTitle(groupName + " : " + name)
                .setContentText(message)
                .setTicker("Group Message")
                .setSmallIcon(R.drawable.ddddddd)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{500, 500})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .build();
        notificationManager.notify(0, notification);
        fire.child("AppData").child("Notifications").child("Request").child(uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)


    public void NotificationMessage(final String name, final String mesage, final String picUrl, String id) {
        pref2 = getApplicationContext().getSharedPreferences("Pref2", Context.MODE_PRIVATE);
        if (pref2 != null) {
            ChatActivity = pref2.getString("pID", "pID");

        }

        if (ChatActivity.equals(id)) {

            fire.child("AppData").child("Notifications").child("Messages").child(uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            notifyyyMessage(name, mesage, picUrl, id);
            fire.child("AppData").child("Notifications").child("Messages").child(uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


//  }
//        }


    }

    public void notifyyyMessage(final String name, final String mesage, final String picUrl, String id) {
        int num = (9999 - 1000) + 1;
        Bitmap bitmap = loadBitmap(picUrl);
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.adnan.panachatfragment");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        notificationManager.notify(num++,n);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setTicker("Office Work")
                .setContentTitle(name)
                .setContentText(mesage)
//                .setContentTitle("Elips")
//                .setContentText("new notification")

                .setTicker("New Message")
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ddddddd)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{500, 500})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .build();
//        floting(name, mesage, picUrl, id);
        sId = id;
        sUrl = picUrl;
        sMsg = mesage;
        sName = name;
//        floatingNew(name, mesage, picUrl, id, uid);

        notificationManager.notify(0, notification);


    }

    private void floatingNew(String name, final String message, String picUrl, String senderId, String uid) {
//if(windowManager != null){
//

//}
//        chathead_longclick();
//        WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
//        param_remove.x = x_cord_remove;
//        param_remove.y = y_cord_remove;

//        windowManager.updateViewLayout(removeView, param_remove);

        if (windowManager != null) {
            windowManager.removeView(chatheadView);
            windowManager = null;
            show(message, senderId,picUrl);
        } else {
            show(message, senderId,picUrl);


        }
    }

//        Intent it = new Intent(Service.this, ChatHeadService.class);
//        it.putExtra("extra_msg", message);
//        it.putExtra("extra_name", name);
//        it.putExtra("extra_picUrl", picUrl);
//        it.putExtra("extra_id", senderId);
//        it.putExtra("extra_uid", uid);
//
//        startService(it);


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DestroyService", "destroy");
        if (chatheadView != null) {
            windowManager.removeView(chatheadView);
        }

        if (txtView != null) {
            windowManager.removeView(txtView);
        }

        if (removeView != null) {
            windowManager.removeView(removeView);
        }
//    windowManager=null;
    }



    public void show(final String message, String senderId, String picUrl) {
        if (message != null && message.length() > 0) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    showMsg(message);
                }
            }, 300);

        } else {
            showMsg(message);
        }
        handleStart(picUrl, uid, senderId);

    }

    private Target loadtarget;

    public void stop() {
        stopSelf();
    }

    public Bitmap loadBitmap(String url) {
        if (loadtarget == null) loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // do something with the Bitmap
                bit = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {

            }

            @Override
            public void onPrepareLoad(Drawable drawable) {

            }


        };

        Picasso.with(getApplicationContext()).load(url).into(loadtarget);
        return bit;
    }

    public void chat(String partnerId, String uid) {



         fire.child("AppData").child("Conversations").child(uid).child(partnerId).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
             @Override
             public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
//                Log.d("snapppp", dataSnapshot.getValue().toString());
                signature_msgs signature = dataSnapshot.getValue(signature_msgs.class);
                msgs.add(signature);
                adaptor.notifyDataSetChanged();
//

             }

             @Override
             public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

             }

             @Override
             public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

    }


    private void handleStart(String picUrl, String uid, String pid) {
        chat(pid, uid);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        removeView = (RelativeLayout) inflater.inflate(R.layout.remove, null);
        WindowManager.LayoutParams paramRemove = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        paramRemove.gravity = Gravity.TOP | Gravity.LEFT;

        removeView.setVisibility(View.GONE);
        removeImg = (ImageView) removeView.findViewById(R.id.remove_img);
        windowManager.addView(removeView, paramRemove);


        chatheadView = (RelativeLayout) inflater.inflate(R.layout.chathead, null);
        chatheadImg = (CircularImageView) chatheadView.findViewById(R.id.chathead_img);
        Picasso.with(getApplicationContext()).load(picUrl).placeholder(R.drawable.group_default).error(R.drawable.group_default).into(chatheadImg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                130,
                130,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        windowManager.addView(chatheadView, params);

        chatheadView.setOnTouchListener(new View.OnTouchListener() {
            long time_start = 0, time_end = 0;
            boolean isLongclick = false, inBounded = false;
            int remove_img_width = 0, remove_img_height = 0;

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
//                    Log.d(com.example.adnan.panachatfragment.ChatHeads.Utils.LogTag, "Into runnable_longClick");

                    isLongclick = true;
                    removeView.setVisibility(View.VISIBLE);
                    chathead_longclick();
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) chatheadView.getLayoutParams();

                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                int x_cord_Destination, y_cord_Destination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_start = System.currentTimeMillis();
                        handler_longClick.postDelayed(runnable_longClick, 600);

                        remove_img_width = removeImg.getLayoutParams().width;
                        remove_img_height = removeImg.getLayoutParams().height;

                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;

                        if (txtView != null) {
                            txtView.setVisibility(View.GONE);
                            myHandler.removeCallbacks(myRunnable);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;

                        if (isLongclick) {
                            int x_bound_left = szWindow.x / 2 - (int) (remove_img_width * 1.5);
                            int x_bound_right = szWindow.x / 2 + (int) (remove_img_width * 1.5);
                            int y_bound_top = szWindow.y - (int) (remove_img_height * 1.5);

                            if ((x_cord >= x_bound_left && x_cord <= x_bound_right) && y_cord >= y_bound_top) {
                                inBounded = true;

                                int x_cord_remove = (int) ((szWindow.x - (remove_img_height * 1.5)) / 2);
                                int y_cord_remove = (int) (szWindow.y - ((remove_img_width * 1.5) + getStatusBarHeight()));

                                if (removeImg.getLayoutParams().height == remove_img_height) {
                                    removeImg.getLayoutParams().height = (int) (remove_img_height * 1.5);
                                    removeImg.getLayoutParams().width = (int) (remove_img_width * 1.5);

                                    WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                    param_remove.x = x_cord_remove;
                                    param_remove.y = y_cord_remove;

                                    windowManager.updateViewLayout(removeView, param_remove);
                                }

                                layoutParams.x = x_cord_remove + (Math.abs(removeView.getWidth() - chatheadView.getWidth())) / 2;
                                layoutParams.y = y_cord_remove + (Math.abs(removeView.getHeight() - chatheadView.getHeight())) / 2;

                                windowManager.updateViewLayout(chatheadView, layoutParams);
                                break;
                            } else {
                                inBounded = false;
                                removeImg.getLayoutParams().height = remove_img_height;
                                removeImg.getLayoutParams().width = remove_img_width;

                                WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
                                int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

                                param_remove.x = x_cord_remove;
                                param_remove.y = y_cord_remove;

                                windowManager.updateViewLayout(removeView, param_remove);
                            }

                        }


                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;

                        windowManager.updateViewLayout(chatheadView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongclick = false;

                        removeView.setVisibility(View.GONE);
                        removeImg.getLayoutParams().height = remove_img_height;
                        removeImg.getLayoutParams().width = remove_img_width;
                        handler_longClick.removeCallbacks(runnable_longClick);

                        if (inBounded) {


//                            stopService(new Intent(ChatHeadService.this, ChatHeadService.class));
                            windowManager.removeView(chatheadView);
                            windowManager=null;
                            inBounded = false;
                            break;
                        }


                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;

                        if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                            time_end = System.currentTimeMillis();
                            if ((time_end - time_start) < 300) {
                                chathead_click();
                            }
                        }

                        y_cord_Destination = y_init_margin + y_diff;

                        int BarHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (chatheadView.getHeight() + BarHeight) > szWindow.y) {
                            y_cord_Destination = szWindow.y - (chatheadView.getHeight() + BarHeight);
                        }
                        layoutParams.y = y_cord_Destination;

                        inBounded = false;
                        resetPosition(x_cord);

                        break;
                    default:
//                        Log.d(com.example.adnan.panachatfragment.ChatHeads.Utils.LogTag, "chatheadView.setOnTouchListener  -> event.getAction() : default");
                        break;
                }
                return true;
            }
        });


        txtView = (LinearLayout) inflater.inflate(R.layout.txt, null);
        txt1 = (TextView) txtView.findViewById(R.id.txt1);
        txt_linearlayout = (LinearLayout) txtView.findViewById(R.id.txt_linearlayout);


        WindowManager.LayoutParams paramsTxt = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        paramsTxt.gravity = Gravity.TOP | Gravity.LEFT;

        txtView.setVisibility(View.GONE);
        windowManager.addView(txtView, paramsTxt);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//         try {
             windowManager.getDefaultDisplay().getSize(szWindow);
//         }catch (Exception e){
//
//         }
         } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) chatheadView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Log.d(com.example.adnan.panachatfragment.ChatHeads.Utils.LogTag, "ChatHeadService.onConfigurationChanged -> landscap");

            if (txtView != null) {
                txtView.setVisibility(View.GONE);
            }

            if (layoutParams.y + (chatheadView.getHeight() + getStatusBarHeight()) > szWindow.y) {
                layoutParams.y = szWindow.y - (chatheadView.getHeight() + getStatusBarHeight());
                windowManager.updateViewLayout(chatheadView, layoutParams);
            }

            if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                resetPosition(szWindow.x);
            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Log.d(com.example.adnan.panachatfragment.ChatHeads.Utils.LogTag, "ChatHeadService.onConfigurationChanged -> portrait");

            if (txtView != null) {
                txtView.setVisibility(View.GONE);
            }

            if (layoutParams.x > szWindow.x) {
                resetPosition(szWindow.x);
            }

        }

    }

    private void resetPosition(int x_cord_now) {
        if (x_cord_now <= szWindow.x / 2) {
            isLeft = true;
            moveToLeft(x_cord_now);

        } else {
            isLeft = false;
            moveToRight(x_cord_now);

        }

    }

    public void dialog(String mesage, final String id, final String picUrl, final String name) {

        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setTitle(mesage);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_content);

        final EditText edit = (EditText) dialog.findViewById(R.id.editTextPop);
        TextView nameText = (TextView) dialog.findViewById(R.id.nameDialog);
        nameText.setText(sName);
        ListView list = (ListView) dialog.findViewById(R.id.dialogListView);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        ImageView textView = (ImageView) dialog.findViewById(R.id.textViewAlert);
        Picasso.with(getApplicationContext()).load(picUrl).placeholder(R.drawable.userdefaul).error(R.drawable.userdefaul).into(textView);

        list.setAdapter(adaptor);

        dialog.findViewById(R.id.sendPop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();

                if (!edit.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
////
                    DatabaseReference messRef = fire.child("AppData").child("Conversations").child(uid).child(id);
                    DatabaseReference messRef2 = fire.child("AppData").child("Conversations").child(id).child(uid);
                    messRef.push().setValue(new signature_msgs(edit.getText().toString(), uid, Date(), "N/A"));
                    messRef2.push().setValue(new signature_msgs(edit.getText().toString(), uid, Date(), "N/A"));

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", name);
                    map.put("picUrl", picUrl);
                    map.put("senderId", uid);
                    map.put("message", edit.getText().toString());
                    fire.child("AppData").child("Notifications").child("Messages").child(id).push().setValue(map);
                    edit.setText("");

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Message !", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
    }


    private void moveToLeft(final int x_cord_now) {
        final int x = szWindow.x - x_cord_now;

        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) chatheadView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = 0 - (int) (double) bounceValue(step, x);
                windowManager.updateViewLayout(chatheadView, mParams);
            }

            public void onFinish() {
                mParams.x = 0;
                windowManager.updateViewLayout(chatheadView, mParams);
            }
        }.start();
    }

    private void moveToRight(final int x_cord_now) {
        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) chatheadView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - chatheadView.getWidth();
                windowManager.updateViewLayout(chatheadView, mParams);
            }

            public void onFinish() {
                mParams.x = szWindow.x - chatheadView.getWidth();
                windowManager.updateViewLayout(chatheadView, mParams);
            }
        }.start();
    }

    private double bounceValue(long step, long scale) {
        double value = scale * Math.exp(-0.055 * step) * Math.cos(0.08 * step);
        return value;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    private void chathead_click() {

        dialog(sMsg, sId, sUrl, sName);
    }

    private void chathead_longclick() {
//        Log.d(com.example.adnan.panachatfragment.ChatHeads.Utils.LogTag, "Into ChatHeadService.chathead_longclick() ");
        if (windowManager != null) {
            WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
            int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
            int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

            param_remove.x = x_cord_remove;
            param_remove.y = y_cord_remove;
//        windowManager.removeView(chatheadView);

            windowManager.updateViewLayout(removeView, param_remove);

        }
    }

    private void showMsg(String sMsg) {
        if (txtView != null && chatheadView != null) {
//            Log.d(com.example.adnan.panachatfragment.ChatHeads.Utils.LogTag, "ChatHeadService.showMsg -> sMsg=" + sMsg);
            txt1.setText(sMsg);
            myHandler.removeCallbacks(myRunnable);

            WindowManager.LayoutParams param_chathead = (WindowManager.LayoutParams) chatheadView.getLayoutParams();
            WindowManager.LayoutParams param_txt = (WindowManager.LayoutParams) txtView.getLayoutParams();

            txt_linearlayout.getLayoutParams().height = chatheadView.getHeight();
            txt_linearlayout.getLayoutParams().width = szWindow.x / 2;

            if (isLeft) {
                param_txt.x = param_chathead.x + chatheadImg.getWidth();
                param_txt.y = param_chathead.y;

                txt_linearlayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else {
                param_txt.x = param_chathead.x - szWindow.x / 2;
                param_txt.y = param_chathead.y;

                txt_linearlayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }

            txtView.setVisibility(View.VISIBLE);
            windowManager.updateViewLayout(txtView, param_txt);

            myHandler.postDelayed(myRunnable, 4000);

        }

    }

    Handler myHandler = new Handler();
    Runnable myRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (txtView != null) {
                txtView.setVisibility(View.GONE);
            }
        }
    };

    public String Date() {
        String date = DateFormat.getDateTimeInstance().format(new Date());
        //24 hour format


        return date;

    }


}

