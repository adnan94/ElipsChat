package com.example.adnan.panachatfragment.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.signature_data;
import com.example.adnan.panachatfragment.UTils.Utils;

import java.util.ArrayList;

/**
 * Created by Adnan on 10/20/2015.
 */
public class adaptorUsers extends BaseAdapter {
    LayoutInflater inflator;
    ArrayList<signature_data> list;
    Context con;
    Bitmap rounded;

    public adaptorUsers(Context con, ArrayList<signature_data> list) {
        this.con = con;
        this.list = list;
        inflator = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflator.inflate(R.layout.user_layout, null);

        ImageView iv = (ImageView) v.findViewById(R.id.imageViewList);
        TextView text = (TextView) v.findViewById(R.id.textViewMainUserList);

        byte[] imageAsBytes = Base64.decode(list.get(position).getPicture(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        rounded = Utils.getRoundedShape(resizedbitmap);
        text.setText("jkl" + list.get(position).getName());
        iv.setImageBitmap(rounded);


        return v;
    }



}
