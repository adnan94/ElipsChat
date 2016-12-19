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
import android.widget.Toast;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.Signature_Friend_List;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
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

import github.ankushsachdeva.emojicon.EmojiconTextView;


/**
 * Created by Adnan on 1/15/2016.
 */
public class ConversationAdaptor extends BaseAdapter {
    Context context;
    ArrayList<Signature_Friend_List> list;
    LayoutInflater inflator;
    DatabaseReference fire;

    public ConversationAdaptor(Context context, ArrayList<Signature_Friend_List> list) {
        this.context = context;
        this.list = list;
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        CircularImageView imageView;
        TextView text;
        ImageView iv;
        EmojiconTextView count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friend_list_xml, parent, false);
            holder = new Holder();
            holder.text = (TextView) convertView.findViewById(R.id.friendListTextView);
            holder.imageView = (CircularImageView) convertView.findViewById(R.id.friendListPic);
            holder.imageView.setBorderColor(R.color.grey);
            holder.imageView.setBorderWidth(1);
            holder.count = (EmojiconTextView) convertView.findViewById(R.id.friendListTv);
holder.iv=(ImageView)convertView.findViewById(R.id.imageView2);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.text.setText(list.get(position).getName());
        holder.count.setText("Conversation");
        Picasso.with(context)
                .load(list.get(position).getPicUrl())
                .placeholder(R.drawable.userdefaul)
                .error(R.drawable.userdefaul)
                .into(holder.imageView);

        return convertView;

    }

}
