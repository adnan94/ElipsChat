package com.example.adnan.panachatfragment.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.capricorn.ArcMenu;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.User;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {
    ImageButton emojiiButton;
    EmojiconEditText emojiiStatus;
    Button submit;
    View rootView;
    DatabaseReference fire;
    ArcMenu arcMenu;

    public StatusFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        fire = FireBaseHandler.fire;
        fire = Service.fire;

        View v = inflater.inflate(R.layout.fragment_status, container, false);
        emojiiButton = (ImageButton) v.findViewById(R.id.emojiiButton);
        emojiiStatus = (EmojiconEditText) v.findViewById(R.id.emojiiStatus);
        submit = (Button) v.findViewById(R.id.SubmitEmojStatus);
        arcMenu = (ArcMenu) getActivity().findViewById(R.id.arc_menu);
        arcMenu.setVisibility(View.GONE);

        rootView = v.findViewById(R.id.rootStatus);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.status = emojiiStatus.getText().toString();

                if (emojiiStatus.getText().length() > 0) {
                    fire.child("AppData").child("BasicInfo").child(Global.uid).setValue(new User(Global.name, Global.picUrl, emojiiStatus.getText().toString(), Global.contact, Global.email, Global.birthday, Global.uid), new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            Snackbar.make(emojiiStatus, "Status Updated..", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            emojiiStatus.setText("");
                        }
                    });
                } else {
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(getActivity().findViewById(R.id.emojiiStatus));
                    emojiiStatus.setError("Write Status First");
                }

            }
        });
        emoj();


        return v;
    }

    private void emoj() {


        final EmojiconsPopup popup = new EmojiconsPopup(rootView, getActivity());

        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener()

                                   {

                                       @Override
                                       public void onDismiss() {
//                changeEmojiKeyboardIcon(emojiButton, R.drawable.smiley);
                                       }
                                   }

        );

        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener()

                                                 {

                                                     @Override
                                                     public void onKeyboardOpen(int keyBoardHeight) {

                                                     }

                                                     @Override
                                                     public void onKeyboardClose() {
                                                         if (popup.isShowing())
                                                             popup.dismiss();
                                                     }
                                                 }

        );

        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener()

                                           {

                                               @Override
                                               public void onEmojiconClicked(Emojicon emojicon) {
                                                   if (emojiiStatus == null || emojicon == null) {
                                                       return;
                                                   }

                                                   int start = emojiiStatus.getSelectionStart();
                                                   int end = emojiiStatus.getSelectionEnd();
                                                   if (start < 0) {
                                                       emojiiStatus.append(emojicon.getEmoji());
                                                   } else {
                                                       emojiiStatus.getText().replace(Math.min(start, end),
                                                               Math.max(start, end), emojicon.getEmoji(), 0,
                                                               emojicon.getEmoji().length());
                                                   }
                                               }
                                           }

        );

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener()

                                                    {

                                                        @Override
                                                        public void onEmojiconBackspaceClicked(View v) {
                                                            KeyEvent event = new KeyEvent(
                                                                    0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                                                            emojiiStatus.dispatchKeyEvent(event);
                                                        }
                                                    }

        );

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        emojiiButton.setOnClickListener(new View.OnClickListener()

                                        {

                                            @Override
                                            public void onClick(View v) {

                                                //If popup is not showing => emoji keyboard is not visible, we need to show it
                                                if (!popup.isShowing()) {

                                                    //If keyboard is visible, simply show the emoji popup
                                                    if (popup.isKeyBoardOpen()) {
                                                        popup.showAtBottom();
                                                    }

                                                    //else, open the text keyboard first and immediately after that show the emoji popup
                                                    else {
                                                        emojiiStatus.setFocusableInTouchMode(true);
                                                        emojiiStatus.requestFocus();
                                                        popup.showAtBottomPending();
                                                        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                        inputMethodManager.showSoftInput(emojiiStatus, InputMethodManager.SHOW_IMPLICIT);
                                                    }
                                                }

                                                //If popup is showing, simply dismiss it to show the undelying text keyboard
                                                else {
                                                    popup.dismiss();
                                                }
                                            }
                                        }

        );
    }

    @Override
    public void onStart() {
        super.onStart();
        Global.statusFlag = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Global.statusFlag = false;
        arcMenu.setVisibility(View.VISIBLE);
    }
}
