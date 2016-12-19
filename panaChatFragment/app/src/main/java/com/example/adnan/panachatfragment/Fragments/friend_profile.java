package com.example.adnan.panachatfragment.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.capricorn.ArcMenu;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class friend_profile extends Fragment {

    TextView name, contact, birthday;
    EmojiconTextView status;
    CircularImageView pic;
    DatabaseReference fire;
    ArcMenu arcMenu;

    public friend_profile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        fire = FireBaseHandler.fire;
        fire = Service.fire;

        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        name = (TextView) v.findViewById(R.id.mProfileName);
        contact = (TextView) v.findViewById(R.id.mProfileContact);
        birthday = (TextView) v.findViewById(R.id.mProfileBirthday);
        status = (EmojiconTextView) v.findViewById(R.id.mProfileStatus);
        pic = (CircularImageView) v.findViewById(R.id.mProfileImage);
        arcMenu = (ArcMenu) getActivity().findViewById(R.id.arc_menu);
        arcMenu.setVisibility(View.GONE);

        pic.setBorderColor(R.color.grey);
        pic.setBorderWidth(0);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.myanimation);
                pic.startAnimation(animation);
            }
        });
        getFriendProfile();
        return v;
    }

    public void getFriendProfile() {
        FireBaseHandler.getInstance().friendProfile(Global.PartnaerId, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                contact.setText(dataSnapshot.child("contact").getValue().toString());
                birthday.setText(dataSnapshot.child("birthday").getValue().toString());
                status.setText(dataSnapshot.child("status").getValue().toString());
                name.setText(dataSnapshot.child("name").getValue().toString());
                Picasso.with(getActivity()).load(dataSnapshot.child("picUrl").getValue().toString()).error(R.drawable.userdefaul).placeholder(R.drawable.userdefaul).into(pic);
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arcMenu.setVisibility(View.VISIBLE);
    }
}


