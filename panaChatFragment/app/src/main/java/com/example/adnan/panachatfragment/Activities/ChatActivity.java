package com.example.adnan.panachatfragment.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Transformation;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.example.adnan.panachatfragment.Adaptors.adaptorMessages;
import com.example.adnan.panachatfragment.Calling.PlaceCallActivity;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.CallHistory;
import com.example.adnan.panachatfragment.Signatures.signature_msgs;
import com.example.adnan.panachatfragment.Calling.SinchService;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.example.adnan.panachatfragment.UTils.Utils;
import com.facebook.Profile;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class ChatActivity extends Activity implements SinchService.StartFailedListener, ServiceConnection {
    EmojiconEditText emojiconEditText;
    ImageButton btnSend, smilie;
    String userId = Global.uid;
    ImageView call;
    String partnerId = Global.PartnaerId;
    DatabaseReference fire;
    SharedPreferences pref;
    int i = 0;
    private SinchService.SinchServiceInterface mSinchServiceInterface;

    public static Activity acti;
    adaptorMessages adaptor;
    ListView listView;
    ArrayList<signature_msgs> msgs;
    ImageButton photoButton;
    Uri uri;
    View rootView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_chat);

        fire = Service.fire;
        Global.statusFlag2 = true;
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);

        msgs = new ArrayList<>();
        acti = ChatActivity.this;
        adaptor = new adaptorMessages(this, msgs, Global.uid);
        this.bindService(new Intent(this, SinchService.class), this, this.BIND_AUTO_CREATE);
        call = (ImageView) findViewById(R.id.ActivityimageView);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, PlaceCallActivity.class);
                startActivity(i);
                Global.isRunning = true;
                String date = Date();
                DatabaseReference path = fire.child("AppData").child("CallHistory").child(Global.uid);
                path.push().setValue(new CallHistory(Global.partnerName, "Outgoing", Global.PpUrl, Global.PartnaerId, date));
                finish();
            }
        });

        TextView name = (TextView) findViewById(R.id.activityName);
        final TextView typ = (TextView) findViewById(R.id.typing);
        final TextView seen = (TextView) findViewById(R.id.seenView);
        final TextView status = (TextView) findViewById(R.id.activityStatus);
        CircularImageView imageView = (CircularImageView) findViewById(R.id.activityImage);


        pref = getSharedPreferences("Pref2", Context.MODE_PRIVATE);

//        Log.d("rrrrr", "" + profile.getId());
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("pID", Global.PartnaerId);
//        edit.putString("name", profile.getName());
        edit.commit();

//
//  Picasso.with(this).
        Picasso.with(this).load(Global.partnerPic).error(R.drawable.userdefaul).placeholder(R.drawable.userdefaul).into(imageView);

        fire.child("AppData").child("TypeStatus").child(Global.PartnaerId).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getValue().toString().equals("true")) {
                    typ.setVisibility(View.VISIBLE);
                    typ.setText(Global.partnerName + " is typing");
                } else {
                    typ.setVisibility(View.GONE);

                }
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
        fire.child("AppData").child("Status").child(Global.PartnaerId).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                status.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                status.setText(dataSnapshot.getValue().toString());

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

        fire.child("AppData").child("ActivitySeen").child(Global.PartnaerId).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue().toString().equals("away")) {
                    seen.setText("away");
                } else {
                    seen.setText("seen");
                }
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue().toString().equals("seen")) {
                    seen.setText("seen");
                } else {
                    seen.setText("away");
                }
//
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

        name.setText(Global.partnerName);
        Uploader uploader = Utils.cloudinary().uploader();
        smilie = (ImageButton) findViewById(R.id.smilies);
        rootView = findViewById(R.id.rootView);
        btnSend = (ImageButton) findViewById(R.id.mGroupChatSentButton);
        emojicon();
        photoButton = (ImageButton) findViewById(R.id.mGroupChatPicButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager check = (ConnectivityManager)
                        ChatActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo[] info = check.getAllNetworkInfo();

                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Intent ii = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(ii, 0);
                        Toast.makeText(ChatActivity.this, "Wait while uploading ....", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(photoButton, "No Connectivity", Snackbar.LENGTH_SHORT).show();


                    }
                }

            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        listView.setAdapter(adaptor);

        emojiconEditText = (EmojiconEditText) findViewById(R.id.mGroupChateditText);


        FireBaseHandler.getInstance().chat(partnerId, Global.uid, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {

                signature_msgs signature = dataSnapshot.getValue(signature_msgs.class);
                msgs.add(signature);
                adaptor.notifyDataSetChanged();
//
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });
        emojiconEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnSend.setImageResource(R.drawable.ic_chat_send);
                    Map<String, String> map = new HashMap<>();
                    map.put("value", "false");
                    fire.child("AppData").child("TypeStatus").child(Global.uid).setValue(map);
                } else {
                    btnSend.setImageResource(R.drawable.ic_chat_send_active);
                    Map<String, String> map = new HashMap<>();
                    map.put("value", "true");
                    fire.child("AppData").child("TypeStatus").child(Global.uid).setValue(map);

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiconEditText.getText().length() > 0) {
                    String date = Date();
                    DatabaseReference messRef = fire.child("AppData").child("Conversations").child(userId).child(partnerId);
                    DatabaseReference messRef2 = fire.child("AppData").child("Conversations").child(partnerId).child(userId);
                    messRef.push().setValue(new signature_msgs(emojiconEditText.getText().toString(), userId, date, url));
                    messRef2.push().setValue(new signature_msgs(emojiconEditText.getText().toString(), userId, date, url));

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", Global.name);
                    map.put("picUrl", Global.picUrl);
                    map.put("senderId", userId);
//                    map.put("",partnerId);
                    map.put("message", emojiconEditText.getText().toString());

                    fire.child("AppData").child("Notifications").child("Messages").child(partnerId).push().setValue(map);
                    url = "N/A";
                    emojiconEditText.setText("");
                } else {
                    emojiconEditText.setError("Plz write some text first");
                }

            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.i(TAG, "I've waited for two hole seconds to show this!");

                Map<String, String> map = new HashMap<>();
                map.put("value", "online");
                fire.child("AppData").child("Status").child(Global.uid).setValue(map);

                Map<String, String> map2 = new HashMap<>();
                map2.put("value", "seen");
                fire.child("AppData").child("ActivitySeen").child(Global.uid).setValue(map2);

            }
        }, 3000);
    }

    private void emojicon() {

        final EmojiconsPopup popup = new EmojiconsPopup(rootView, ChatActivity.this);

        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
//                changeEmojiKeyboardIcon(emojiButton, R.drawable.smiley);
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (emojiconEditText == null || emojicon == null) {
                    return;
                }

                int start = emojiconEditText.getSelectionStart();
                int end = emojiconEditText.getSelectionEnd();
                if (start < 0) {
                    emojiconEditText.append(emojicon.getEmoji());
                } else {
                    emojiconEditText.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiconEditText.dispatchKeyEvent(event);
            }
        });

        smilie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!popup.isShowing()) {

                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                    } else {
                        emojiconEditText.setFocusableInTouchMode(true);
                        emojiconEditText.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
                    }
                } else {
                    popup.dismiss();
                }
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            try {
                uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();


                Upload upload1 = new Upload();
                upload1.execute(picturePath);

            } catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String Date() {
        String date = DateFormat.getDateTimeInstance().format(new Date());
        //24 hour format


        return date;

    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }

    class Upload extends AsyncTask<String, Void, HashMap<String, Object>> {

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            File file1 = new File(params[0]);
            Map wait = null;
            try {
               /* wait =*/
                Map options = ObjectUtils.asMap("transformation", new Transformation().width(200).height(200));

                return (HashMap<String, Object>) Utils.cloudinary().uploader().upload(file1, options);
            } catch (IOException e) {
                e.printStackTrace();
                return (HashMap<String, Object>) wait;

            }


        }

        @Override
        protected void onPostExecute(HashMap<String, Object> stringObjectHashMap) {
            super.onPostExecute(stringObjectHashMap);
            try {
                url = (String) stringObjectHashMap.get("url");
            } catch (Exception e) {

            }
            String date = Date();
            DatabaseReference messRef = fire.child("AppData").child("Conversations").child(userId).child(partnerId);
            DatabaseReference messRef2 = fire.child("AppData").child("Conversations").child(partnerId).child(userId);
            messRef.push().setValue(new signature_msgs("", userId, date, url));
            messRef2.push().setValue(new signature_msgs("", userId, date, url));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", Global.name);
            map.put("picUrl", Global.picUrl);
            map.put("senderId", userId);
            map.put("message", Global.name + " sent a image !");

            fire.child("AppData").child("Notifications").child("Messages").child(partnerId).push().setValue(map);
            url = "N/A";
            emojiconEditText.setText("");
        }
    }


    @Override
    public void onResume() {
        Map<String, String> map2 = new HashMap<>();
        map2.put("value", "seen");
        fire.child("AppData").child("ActivitySeen").child(Global.uid).setValue(map2);
        super.onResume();

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    private void onServiceConnected() {

        mSinchServiceInterface.setStartListener(this);
        if (!mSinchServiceInterface.isStarted()) {
            mSinchServiceInterface.startClient(Global.uid);
            Global.flag = true;
        }
    }

    private void onServiceDisconnected() {

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(ChatActivity.this, AfterLoginScreen.class);
        startActivity(i);
//        Global.statusFlag2 = false;
        finish();
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Tagi", "chat activity");

//        Global.isRunning = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Map<String, String> map = new HashMap<>();
        map.put("value", "offline");
        fire.child("AppData").child("Status").child(Global.uid).setValue(map);
        Map<String, String> map1 = new HashMap<>();
        map1.put("value", "false");
        fire.child("AppData").child("TypeStatus").child(Global.uid).setValue(map1);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map2 = new HashMap<>();
                map2.put("value", "away");
                fire.child("AppData").child("ActivitySeen").child(Global.uid).setValue(map2);

            }
        }, 2000);
        Log.d("Tagi", "Caht destroy");
        pref.edit().remove("pID").commit();
    }

    @Override
    public void onPause() {
        Map<String, String> map2 = new HashMap<>();
        map2.put("value", "away");
        fire.child("AppData").child("ActivitySeen").child(Global.uid).setValue(map2);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.i(TAG, "I've waited for two hole seconds to show this!");
                Map<String, String> map1 = new HashMap<>();
                map1.put("value", "false");
                fire.child("AppData").child("TypeStatus").child(Global.uid).setValue(map1);


            }
        }, 2000);


        super.onPause();

    }
}
