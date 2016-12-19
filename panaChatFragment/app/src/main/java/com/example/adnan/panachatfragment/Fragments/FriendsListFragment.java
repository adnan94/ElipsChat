package com.example.adnan.panachatfragment.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.example.adnan.panachatfragment.Adaptors.Friend_List_Adaptor;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.Signature_Friend_List;
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
public class FriendsListFragment extends Fragment {
    ListView listView;
    public static ArrayList<Signature_Friend_List> list;
    DatabaseReference fire;
    int i = 0;
    ArrayList<String> picUrls;
    public static Friend_List_Adaptor adaptor;
//    ArcMenu arcMenu;


    public FriendsListFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        fire = Service.fire;

        View v = inflater.inflate(R.layout.friends_list_fragment, null);
        listView = (ListView) v.findViewById(R.id.ListViewfriendList);
        registerForContextMenu(listView);
//        arcMenu = (ArcMenu) getActivity().findViewById(R.id.arc_menu);
//        arcMenu.setVisibility(View.GONE);

        list = new ArrayList<>();
        picUrls = new ArrayList<>();
        adaptor = new Friend_List_Adaptor(getActivity(), list, picUrls);
        getList();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Global.PartnaerId = list.get(position).getSenderId();
                Global.partnerName = list.get(position).getName();


                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.in_from_right, R.anim.out_from_left, R.anim.out_from_left);
                transaction.add(R.id.RelativeLayoutHomeScreen, new friend_profile());
                transaction.addToBackStack("friendProfile");
                transaction.commit();
            }
        });
        listView.setAdapter(adaptor);

        return v;
    }


    @Override
    public void onResume() {
        Log.d("kk", "resume");

        adaptor.notifyDataSetChanged();
        super.onResume();

    }

    @Override
    public void onStart() {
        Log.d("kk", "start");

        adaptor.notifyDataSetChanged();

        super.onStart();
    }

    public void getList() {
        FireBaseHandler.getInstance().getFriendListListListener(Global.uid, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                list.add(new Signature_Friend_List(dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("senderId").getValue().toString(), dataSnapshot.child("picUrl").getValue().toString()));
                adaptor.notifyDataSetChanged();

            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//    arcMenu.setVisibility(View.VISIBLE);
    }
}
