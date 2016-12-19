package com.example.adnan.panachatfragment.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adnan.panachatfragment.Activities.ChatActivity;
import com.example.adnan.panachatfragment.Adaptors.ConversationAdaptor;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.Signature_Friend_List;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.example.adnan.panachatfragment.UTils.Global.picUrl;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {

    ListView listView;
    ConversationAdaptor adaptor;
    ArrayList<Signature_Friend_List> list;
    ArrayList<String> picUrlss;
    DatabaseReference fire;
    String picUrlll;
    int selectedIndex;
    TextView alternate;

    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fire = Service.fire;

        View v = inflater.inflate(R.layout.fragment_conversation, container, false);
        list = new ArrayList<>();
        picUrlss = new ArrayList<>();
        visibility();


        listView = (ListView) v.findViewById(R.id.converList);
        alternate = (TextView) v.findViewById(R.id.conveersationAlternate);
        adaptor = new ConversationAdaptor(getActivity(), list);
        listView.setAdapter(adaptor);
        registerForContextMenu(listView);

        getList();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Global.PartnaerId = list.get(position).getSenderId();
                Global.partnerName = list.get(position).getName();
                Global.PpUrl = list.get(position).getPicUrl();
                Global.partnerPic = list.get(position).getPicUrl();

                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
                Global.statusFlag2 = false;
                getActivity().finish();

            }
        });


        return v;
    }

    public void visibility() {
//        ImageButton img = (ImageButton) getActivity().findViewById(R.id.addFriendButton);
//        img.setVisibility(View.VISIBLE);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("What Do you want");
        String[] options = {"Unfriend", "Delete Msgs", "Return"};
        for (String opt : options) {
            menu.add(opt);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedIndex = info.position;

        if (item.getTitle() == "Unfriend") {
            unfriend();
        }

        if (item.getTitle() == "Delete Msgs") {
            deleteMsgs();
        }
        return true;
    }

    public void getList() {

        FireBaseHandler.getInstance().getConversationListListener(Global.uid, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d("Dogs", dataSnapshot.child("name").getValue().toString());
                alternate.setVisibility(View.GONE);
                final String sender = dataSnapshot.child("senderId").getValue().toString();
                fire.child("AppData").child("BasicInfo").child(sender).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        picUrlll = dataSnapshot.child("picUrl").getValue().toString();
                        list.add(new Signature_Friend_List(dataSnapshot.child("name").getValue().toString(), sender, picUrlll));
                        adaptor.notifyDataSetChanged();
//
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });

    }

    public void unfriend() {
        FireBaseHandler.getInstance().unFriend(new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {

                dataSnapshot.child(Global.uid).child(list.get(selectedIndex).getSenderId()).getRef().removeValue();
                dataSnapshot.child(list.get(selectedIndex).getSenderId()).child(Global.uid).getRef().removeValue();

                list.remove(selectedIndex);
                FriendsListFragment.list.remove(selectedIndex);
                FriendsListFragment.adaptor.notifyDataSetChanged();
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });
    }

    public void deleteMsgs() {
        FireBaseHandler.getInstance().deleteChatMsgs(Global.uid, list.get(selectedIndex).getSenderId(), new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                adaptor.notifyDataSetChanged();
//
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });

    }
}
