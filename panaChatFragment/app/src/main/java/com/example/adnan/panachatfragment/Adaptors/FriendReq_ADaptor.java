package com.example.adnan.panachatfragment.Adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.Frient_Req_Signature;
import com.example.adnan.panachatfragment.UTils.Global;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * Created by Adnan on 1/5/2016.
 */
public class FriendReq_ADaptor extends BaseAdapter {

    ArrayList<Frient_Req_Signature> list;
    Context con;
    LayoutInflater inflater;
    Firebase fire;

    public FriendReq_ADaptor(ArrayList<Frient_Req_Signature> list, Context con) {
        this.list = list;
        this.con = con;
        inflater = LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView text;
        CircularImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(con).inflate(R.layout.friend_req_layout, parent, false);
            holder = new Holder();
            holder.text = (TextView) convertView.findViewById(R.id.textViewfriendReq);
            holder.imageView = (CircularImageView) convertView.findViewById(R.id.imageViewFriendReq);
            holder.imageView.setBorderColor(R.color.grey);
            holder.imageView.setBorderWidth(1);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.text.setText(list.get(position).getName());

//
//        fire.child("AppData").child("BasicInfo").child(list.get(position).getSentId()).child("picUrl").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////
                Picasso.with(con)
                        .load(list.get(position).getPicUrl())
                        .placeholder(R.drawable.userdefaul)
                        .error(R.drawable.userdefaul)
                        .into(holder.imageView);
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });


        return convertView;


    }
}
