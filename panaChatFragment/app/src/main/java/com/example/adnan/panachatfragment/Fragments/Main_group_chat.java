package com.example.adnan.panachatfragment.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.adnan.panachatfragment.Adaptors.Main_Chat_Adaptor;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.groupMsgsSignature;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.example.adnan.panachatfragment.UTils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class Main_group_chat extends Fragment {
    ImageButton send, photoButton, smilie;
    EmojiconEditText msg;
    DatabaseReference fire;
    ArrayList<groupMsgsSignature> list;
    Main_Chat_Adaptor adaptor;
    ListView listView;
    Uri uri;
    String url;
    View rootView;
    int i = 0;
    ArcMenu arcMenu;

    public Main_group_chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fire = Service.fire;

        TextView text2 = (TextView) getActivity().findViewById(R.id.title);
        text2.setText(Global.nameOfGroup);
        View v = inflater.inflate(R.layout.fragment_main_group_chat, container, false);
        list = new ArrayList<>();
        listView = (ListView) v.findViewById(R.id.mmlistView);
        adaptor = new Main_Chat_Adaptor(getActivity(), list);
        listView.setAdapter(adaptor);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        smilie = (ImageButton) v.findViewById(R.id.smilies);
        send = (ImageButton) v.findViewById(R.id.mGroupChatSentButton);
        photoButton = (ImageButton) v.findViewById(R.id.mGroupChatPicButton);
        rootView = v.findViewById(R.id.rootView);
        msg = (EmojiconEditText) v.findViewById(R.id.mGroupChateditText);
        emojImplement();
        chatWork();
        arcMenu = (ArcMenu) getActivity().findViewById(R.id.arc_menu);
        arcMenu.setVisibility(View.GONE);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getText().length() > 0) {
                    String date = Date();
                    fire.child("AppData").child("GroupData").child(Global.nameOfGroup).child("Conversation").push().setValue(new groupMsgsSignature(Global.name, Global.picUrl, msg.getText().toString(), date, Global.uid, "N/A"));
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", Global.name);
                    map.put("groupName", Global.nameOfGroup);
                    map.put("picUrl", Global.iconGroup);
                    map.put("sender", Global.uid);
                    map.put("message", msg.getText().toString());
                    fire.child("AppData").child("Notifications").child("Groups").push().setValue(map);

//                    HashMap<String, String> map = new HashMap<String, String>();
//                    map.put("name", Global.name);
//                    map.put("picUrl", Global.picUrl);
//                    map.put("message", msg.getText().toString());
//                    map.put("groupName", Global.nameOfGroup);

//                    fire.child("AppData").child("Notifications").child("GroupMessages").child("Messages").push().setValue(map);
                    msg.setText("");
                } else {
                    msg.setError("Plz write some text first");

                }
            }
        });
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager check = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo[] info = check.getAllNetworkInfo();

                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Intent ii = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(ii, 0);
                        Toast.makeText(getActivity(), "Wait while uploading ....", Toast.LENGTH_SHORT).show();

                    } else {

//                        Toast.makeText(getActivity(), "Internet is connected ", Toast.LENGTH_SHORT).show();
                        Snackbar.make(photoButton, "No Connectivity", Snackbar.LENGTH_SHORT).show();

                    }
                }


            }
        });
        msg.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    send.setImageResource(R.drawable.ic_chat_send);
                } else {
                    send.setImageResource(R.drawable.ic_chat_send_active);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });


        return v;
    }

    private void emojImplement() {

        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        final EmojiconsPopup popup = new EmojiconsPopup(rootView, getActivity());

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
                if (msg == null || emojicon == null) {
                    return;
                }

                int start = msg.getSelectionStart();
                int end = msg.getSelectionEnd();
                if (start < 0) {
                    msg.append(emojicon.getEmoji());
                } else {
                    msg.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                msg.dispatchKeyEvent(event);
            }
        });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        smilie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
//                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        msg.setFocusableInTouchMode(true);
                        msg.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(msg, InputMethodManager.SHOW_IMPLICIT);
//                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();
                }
            }
        });

        //On submit, add the edittext text to listview and clear the edittext


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        arcMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            try {
                uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(uri,
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
            Log.d("Amazing", url);
            String date = Date();
            fire.child("AppData").child("GroupData").child(Global.nameOfGroup).child("Conversation").push().setValue(new groupMsgsSignature(Global.name, Global.picUrl, "", "", Global.uid, url));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", Global.name);
            map.put("groupName", Global.nameOfGroup);
            map.put("picUrl", Global.iconGroup);
            map.put("sender", Global.uid);
            map.put("message", Global.name + " sent a image !");
            fire.child("AppData").child("Notifications").child("Groups").push().setValue(map);
            url = "N/A";
            msg.setText("");
        }
    }

    public String Date() {
        String date = DateFormat.getDateTimeInstance().format(new Date());
        //24 hour format


        return date;

    }

    public void chatWork() {
        FireBaseHandler.getInstance().groupChatWork(Global.nameOfGroup, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                groupMsgsSignature sig = dataSnapshot.getValue(groupMsgsSignature.class);
                list.add(sig);
                adaptor.notifyDataSetChanged();
//
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });


    }

    @Override
    public void onPause() {
        TextView text2 = (TextView) getActivity().findViewById(R.id.title);
        text2.setText(Global.name);
        super.onPause();
    }
}
