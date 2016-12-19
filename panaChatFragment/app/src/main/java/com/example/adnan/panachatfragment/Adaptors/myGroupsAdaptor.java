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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.groupsListSignature;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by Adnan on 1/18/2016.
 */
public class myGroupsAdaptor extends BaseAdapter {
    Context con;
    DatabaseReference fire;
    LayoutInflater inflater;
    ArrayList<groupsListSignature> list;
    CircularImageView imageView;
    TextView statusss, text;

    public myGroupsAdaptor(ArrayList<groupsListSignature> list, Context con) {
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
        View v = inflater.inflate(R.layout.friend_list_xml, null);
        imageView = (CircularImageView) v.findViewById(R.id.friendListPic);
        imageView.setBorderWidth(0);
        text = (TextView) v.findViewById(R.id.friendListTextView);
        statusss = (TextView) v.findViewById(R.id.friendListTv);

        text.setText(list.get(position).getGroupName());
        statusss.setText("ADMIN : " + list.get(position).getAdmin());
        Picasso.with(con).load(list.get(position).getPicUrl()).placeholder(R.drawable.group_default).into(imageView);
        return v;
    }


}