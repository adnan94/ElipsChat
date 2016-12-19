package com.example.adnan.panachatfragment.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.capricorn.ArcMenu;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Adaptors.Search_Bar_Adaptor;
import com.example.adnan.panachatfragment.Signatures.Search_Signature;
import com.example.adnan.panachatfragment.Signatures.User;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindFriends extends Fragment implements SearchView.OnQueryTextListener {
    // List view

    LayoutInflater inflater;
    ArrayList<User> list;
    DatabaseReference fire;
    ListView mListView;
    private SearchView mSearchView;
    Search_Bar_Adaptor adapter;
    ArcMenu arcMenu;

    public FindFriends() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fire = Service.fire;

        View v = inflater.inflate(R.layout.fragment_friend, container, false);
        mSearchView = (SearchView) v.findViewById(R.id.searchView1);
        mListView = (ListView) v.findViewById(R.id.listView1);
        arcMenu = (ArcMenu) getActivity().findViewById(R.id.arc_menu);
        arcMenu.setVisibility(View.GONE);

        list = new ArrayList<>();
        findFriendList();


        adapter = new Search_Bar_Adaptor(getActivity(), list);
        mListView.setAdapter(adapter);

        mListView.setTextFilterEnabled(true);
        setupSearchView();


        return v;
    }

    public void findFriendList() {
        FireBaseHandler.getInstance().FindFriendList(new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {

                Global.userIds.add(dataSnapshot.getKey());
                list.add(new User(dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("picUrl").getValue().toString(), dataSnapshot.child("status").getValue().toString(), dataSnapshot.child("email").getValue().toString(), dataSnapshot.child("contact").getValue().toString(), dataSnapshot.child("birthday").getValue().toString(), dataSnapshot.getKey()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });

    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Click Here");
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public void onStart() {
        super.onStart();
        Global.addFriendFlag = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Global.addFriendFlag = false;
        arcMenu.setVisibility(View.VISIBLE);
    }


}


