package com.example.adnan.panachatfragment.Adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.signature_msgs;
import com.example.adnan.panachatfragment.UTils.AppClass;
import com.example.adnan.panachatfragment.UTils.Global;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import github.ankushsachdeva.emojicon.EmojiconTextView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Adnan on 10/20/2015.
 */
public class adaptorMessages extends BaseAdapter {
    Context con;
    LayoutInflater inflator;
    ArrayList<signature_msgs> list;
    ImageView iv;
    TextView time;

    EmojiconTextView message;
    Bitmap bit;
    SharedPreferences pref;
    String currentUserId;
    private Target loadtarget;

    public adaptorMessages(Context con, ArrayList<signature_msgs> msgs, String currentUserIdd) {
        this.con = con;
        inflator = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = msgs;
        this.currentUserId = currentUserIdd;
//        pref = getApplicationContext().getSharedPreferences("SignInData", Context.MODE_PRIVATE);

//       currentUserId = pref.getString("uid", "uid");
    }

    public adaptorMessages(Context con, ArrayList<signature_msgs> msgs) {
        this.con = con;
        inflator = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = msgs;

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
        if (currentUserId != null) {
            Log.d("UserIdd", currentUserId);
            if (currentUserId.equals(list.get(position).getUids())) {
                View v = inflator.inflate(R.layout.left_layout, null);
                time = (TextView) v.findViewById(R.id.LtimeUI);
                message = (EmojiconTextView) v.findViewById(R.id.LmessageUI);
                time.setText(list.get(position).getDate());
                message.setText(list.get(position).getMessage());
                iv = (ImageView) v.findViewById(R.id.LimageView);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert(position);
                    }

                });
                Log.d("rrr", "" + list.get(position).getUrl());
                Picasso.with(con)
                        .load(list.get(position).getUrl())
                        .into(iv);

                return v;
            } else {
                View v2 = inflator.inflate(R.layout.right_layout, null);
                time = (TextView) v2.findViewById(R.id.RtimeUI);
                message = (EmojiconTextView) v2.findViewById(R.id.RmessageUI);
                time.setText(list.get(position).getDate());
                message.setText(list.get(position).getMessage());
                iv = (ImageView) v2.findViewById(R.id.RimageView);

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert(position);
                    }
                });

                Picasso.with(con)
                        .load(list.get(position).getUrl())
                        .into(iv);


                return v2;
            }
        }
//        String compare = currentUserId;

        return null;

    }


    public void alert(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("Save Image");
        final Bitmap bitmap = loadBitmap(list.get(position).getUrl());
        ImageView imageView = new ImageView(con);
        imageView.setImageBitmap(bitmap);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage(bitmap);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.setView(imageView);
        builder.create();
        builder.show();

    }

    private String saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Elips/ProfileImages");

        if (!myDir.exists())
            myDir.mkdirs();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image_" + n + ".jpg";
        File file = new File(myDir, fname);

        if (file.exists())
            file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return root + "/Elips/ProfileImages/" + fname;
    }

    public Bitmap loadBitmap(String url) {

        if (loadtarget == null) loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bit = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {

            }

            @Override
            public void onPrepareLoad(Drawable drawable) {

            }


        };

        Picasso.with(con).load(url).into(loadtarget);
        return bit;
    }

}