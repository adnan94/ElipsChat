package com.example.adnan.panachatfragment.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adnan.panachatfragment.Adaptors.CallAdaptor;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.CallHistory;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {
    ListView listView;
    ArrayList<CallHistory> list;
    DatabaseReference fire;
    CallAdaptor callAdaptor;
    TextView alternate;

    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_calls, container, false);
        listView = (ListView) v.findViewById(R.id.listView2);
        alternate=(TextView)v.findViewById(R.id.callsAlter);
        list = new ArrayList<>();
        fire = Service.fire;
        callAdaptor = new CallAdaptor(getActivity(), list);
        listView.setAdapter(callAdaptor);
        Button clear=(Button)v.findViewById(R.id.clearistory);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fire.child("AppData").child("CallHistory").child(Global.uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                        list.clear();
                        callAdaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//
            }
        });
        DatabaseReference firebase = fire.child("AppData").child("CallHistory").child(Global.uid);
        firebase.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.d("Logi", "" + dataSnapshot.getValue().toString());
                CallHistory callHistory = dataSnapshot.getValue(CallHistory.class);
                list.add(callHistory);
                alternate.setVisibility(View.GONE);
                callAdaptor.notifyDataSetChanged();
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
        return v;
    }

}
