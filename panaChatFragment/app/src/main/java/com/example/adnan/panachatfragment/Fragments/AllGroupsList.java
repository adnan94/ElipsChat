package com.example.adnan.panachatfragment.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.capricorn.ArcMenu;
import com.example.adnan.panachatfragment.Adaptors.Adaptor_Groups_List;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.groupsListSignature;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllGroupsList extends Fragment {


    public AllGroupsList() {
        // Required empty public constructor
    }


    ImageButton img;
    ListView listView;
    ArrayList<groupsListSignature> list;
    DatabaseReference fire;
    ProgressDialog pd;
    Adaptor_Groups_List adaptor;
    ArcMenu arcMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        list = new ArrayList<>();
//        fire = FireBaseHandler.fire;
        fire = Service.fire;

        View v = inflater.inflate(R.layout.fragment_all_groups_list, container, false);
        listView = (ListView) v.findViewById(R.id.GrouplistView);
        allGroupList();
        arcMenu = (ArcMenu) getActivity().findViewById(R.id.arc_menu);
        arcMenu.setVisibility(View.GONE);

        adaptor = new Adaptor_Groups_List(list, getActivity());
        listView.setAdapter(adaptor);
//        adaptor.notifyDataSetChanged();
        return v;
    }

    public void allGroupList() {
        FireBaseHandler.getInstance().allGroupList(new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                list.add(new groupsListSignature(dataSnapshot.child("picUrl").getValue().toString(), dataSnapshot.child("admin").getValue().toString(), dataSnapshot.child("groupName").getValue().toString()));
                adaptor.notifyDataSetChanged();
//
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Global.searchGroupFlag = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Global.searchGroupFlag = false;
        arcMenu.setVisibility(View.VISIBLE);
    }
}
