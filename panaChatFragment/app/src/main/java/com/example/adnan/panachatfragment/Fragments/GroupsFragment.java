package com.example.adnan.panachatfragment.Fragments;


import android.app.ProgressDialog;
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

import com.example.adnan.panachatfragment.Adaptors.myGroupsAdaptor;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.groupsListSignature;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {
    ImageButton img;
    ListView listView;
    ArrayList<groupsListSignature> list;
    DatabaseReference fire;
    ProgressDialog pd;
    myGroupsAdaptor adaptor;
    int i = 0;
    TextView alternate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        fire = FireBaseHandler.fire;
        fire = Service.fire;

        list = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        listView = (ListView) v.findViewById(R.id.mGrouplistView);
        alternate = (TextView) v.findViewById(R.id.groupsAlternate);
        adaptor = new myGroupsAdaptor(list, getActivity());
        listView.setAdapter(adaptor);
        registerForContextMenu(listView);
        getList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Global.nameOfGroup = list.get(position).getGroupName();
                Global.iconGroup = list.get(position).getPicUrl();
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.in_from_right, R.anim.out_from_left, R.anim.out_from_left);
                transaction.add(R.id.RelativeLayoutHomeScreen, new Main_group_chat());
                transaction.addToBackStack(null).commit();
            }
        });


        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("What Do you want");
        String[] options = {"Delete", "Return"};
        for (String opt : options) {
            menu.add(opt);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int selectedIndex = info.position;

        if (item.getTitle() == "Delete") {
            Log.d("DeleteGroup", "selected");

            FireBaseHandler.getInstance().groupFragmentContextItemSelected(Global.uid, list.get(selectedIndex).getGroupName(), new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
                @Override
                public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue();
                    list.remove(selectedIndex);
                    adaptor.notifyDataSetChanged();
//
                }

                @Override
                public void fail(FirebaseError obj) {

                }
            });
        }
        return true;
        }

    public void getList() {
        FireBaseHandler.getInstance().getGroupListListener(Global.uid, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                alternate.setVisibility(View.GONE);
                list.add(new groupsListSignature(dataSnapshot.child("picUrl").getValue().toString(), dataSnapshot.child("admin").getValue().toString(), dataSnapshot.child("groupName").getValue().toString()));
                adaptor.notifyDataSetChanged();
//
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });

    }
}
///////////////