package com.example.adnan.panachatfragment.Adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.groupsListSignature;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Adnan on 1/18/2016.
 */
public class Adaptor_Groups_List extends BaseAdapter {
    Context con;
    DatabaseReference fire;
    LayoutInflater inflater;
    ArrayList<groupsListSignature> list;
    CircularImageView imageView;
    ImageView add;
    TextView statusss, text;

    public Adaptor_Groups_List(ArrayList<groupsListSignature> list, Context con) {
        this.list = list;
        this.con = con;
        inflater = LayoutInflater.from(con);
        fire = Service.fire;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.group_list_all, null);
        imageView = (CircularImageView) v.findViewById(R.id.friendListPic);
        imageView.setBorderWidth(0);
        text = (TextView) v.findViewById(R.id.tvName);
        statusss = (TextView) v.findViewById(R.id.tvAdmin);
        add = (ImageView) v.findViewById(R.id.ivAdd);
        check(position, add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                fire.child("AppData").child("MyGroups").child(Global.uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(list.get(position).getGroupName())) {
                            Snackbar.make(add, "You Are Already In This Group", Snackbar.LENGTH_SHORT).show();
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .playOn(v.findViewById(R.id.ivAdd));
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(con);
                            alert.setTitle("What You Want To Do");
                            alert.setMessage("Are you sure you want to add this group ?");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    fire.child("AppData").child("MyGroups").child(Global.uid).child(list.get(position).getGroupName()).setValue(new groupsListSignature(list.get(position).getPicUrl(), list.get(position).getAdmin(), list.get(position).getGroupName()));
                                    Snackbar.make(add, "Sucessfully Added In This Group", Snackbar.LENGTH_SHORT).show();
                                    v.setBackgroundResource(R.drawable.already);
                                    notifyDataSetChanged();

                                }
                            });
                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                            alert.create();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//
            }
        });
        text.setText(list.get(position).getGroupName());
        statusss.setText("ADMIN : " + list.get(position).getAdmin());
        Picasso.with(con)
                .load(list.get(position).getPicUrl())
                .placeholder(R.drawable.group_default)
                .error(R.drawable.group_default)
                .into(imageView);
        return v;
    }

    public void check(final int positionn, final ImageView v) {
        fire.child("AppData").child("MyGroups").child(Global.uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(list.get(positionn).getGroupName())) {
                    v.setImageResource(R.drawable.already);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
