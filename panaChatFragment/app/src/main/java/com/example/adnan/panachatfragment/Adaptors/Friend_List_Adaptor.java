package com.example.adnan.panachatfragment.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconTextView;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * Created by Adnan on 1/6/2016.
 */
public class Friend_List_Adaptor extends BaseAdapter {
    Context context;
    ArrayList<Signature_Friend_List> list;
    LayoutInflater inflator;
    ArrayList<String> picUrl = new ArrayList<>();
    DatabaseReference fire;

    public Friend_List_Adaptor(Context context, ArrayList<Signature_Friend_List> list, ArrayList<String> picUrls) {
        this.context = context;
        this.list = list;
        this.picUrl = picUrls;
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        fire = FireBaseHandler.FirebaseRef();
//        fire = FireBaseHandler.fire;
        fire = Service.fire;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public class Holder {
        TextView text;
        CircularImageView imageView;
        EmojiconTextView status;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friend_list_xml, parent, false);
            holder = new Holder();
            holder.text = (TextView) convertView.findViewById(R.id.friendListTextView);
            holder.imageView = (CircularImageView) convertView.findViewById(R.id.friendListPic);
            holder.status = (EmojiconTextView) convertView.findViewById(R.id.friendListTv);
            holder.imageView.setBorderColor(R.color.grey);
            holder.imageView.setBorderWidth(0);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.text.setText(list.get(position).getName());
        FireBaseHandler.getInstance().friendListAdaptor(list.get(position).getSenderId(), new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                holder.status.setText(dataSnapshot.child("status").getValue().toString());
                Picasso.with(context)
                        .load(dataSnapshot.child("picUrl").getValue().toString())
                        .placeholder(R.drawable.userdefaul)
                        .error(R.drawable.userdefaul)
                        .into(holder.imageView);
//
            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });


        return convertView;


    }

}
