package com.example.adnan.panachatfragment.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.adnan.panachatfragment.Adaptors.FriendReq_ADaptor;
import com.example.adnan.panachatfragment.Signatures.Frient_Req_Signature;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Friend_Request_Fragment extends Fragment {
    ListView listView;
    DatabaseReference fire;
    ArrayList<Frient_Req_Signature> list;

    FriendReq_ADaptor adaptor;
    String key;
    int count;
    TextView alternate;

    public Friend_Request_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        fire = FireBaseHandler.getInstance().fire;
        fire = Service.fire;

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.friend_req_fragment, container, false);
        listView = (ListView) v.findViewById(R.id.listView);


        alternate = (TextView) v.findViewById(R.id.alternate);

        list = new ArrayList<>();
        getList();
        adaptor = new FriendReq_ADaptor(list, getActivity());

        listView.setAdapter(adaptor);


        adaptor.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                final int posi = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("What You Want To Do");
                alert.setMessage("Are you sure you want to add this person ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idFriend = list.get(position).getSenderId();
                        fire.child("AppData").child("Friends").child(list.get(position).getSenderId()).child(Global.uid).setValue(new Frient_Req_Signature(Global.name, Global.picUrl, Global.uid));
                        fire.child("AppData").child("Friends").child(Global.uid).child(list.get(position).getSenderId()).setValue(new Frient_Req_Signature(list.get(position).getName(), list.get(position).getPicUrl(), list.get(position).getSenderId()));

                        fire.child("AppData").child("FriendRequests").child(Global.uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                int i = 0;
                                for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    if (i == position) {
                                        DatabaseReference ref = dataSnapshot1.getRef();
                                        ref.removeValue();
                                        list.remove(position);

                                        adaptor.notifyDataSetChanged();
                                    } else {
                                        i++;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(getActivity(), "Friend Is Add To Your List ", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.create();
                alert.show();
            }
        });

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Ali", "Resume");
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    public void getList() {
        FireBaseHandler.getInstance().getFriendRequestListListener(Global.uid, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                alternate.setVisibility(View.GONE);
                String key = dataSnapshot.getKey();
                Frient_Req_Signature signature = dataSnapshot.getValue(Frient_Req_Signature.class);
                Log.d("Tagee", key);
                list.add(new Frient_Req_Signature(signature.getName(), signature.getPicUrl(), key));
                count = list.size();
                adaptor.notifyDataSetChanged();

            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });

    }

}
