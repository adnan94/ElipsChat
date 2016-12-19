package com.example.adnan.panachatfragment.Adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.groupMsgsSignature;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.Utils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * Created by Adnan on 1/18/2016.
 */
public class Main_Chat_Adaptor extends BaseAdapter {
    Context con;
    LayoutInflater inflator;
    ArrayList<groupMsgsSignature> list;
    ImageView iv;
    CircularImageView userImage;
    TextView name;
    Bitmap bit;
    Bitmap round;
    EmojiconTextView time;
    private Target loadtarget;

    public Main_Chat_Adaptor(Context con, ArrayList<groupMsgsSignature> msgs) {
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
        if (Global.uid.equals(list.get(position).getId())) {
            View v = inflator.inflate(R.layout.group_left, null);
            time = (EmojiconTextView) v.findViewById(R.id.mLtimeUI);
            name = (TextView) v.findViewById(R.id.mLnameUI);
            time.setText(list.get(position).getMsg());
            name.setText(list.get(position).getName());

            iv = (ImageView) v.findViewById(R.id.mLimageView);
            userImage = (CircularImageView) v.findViewById(R.id.mLgroupUserPic);
            userImage.setBorderColor(R.color.grey);
            userImage.setBorderWidth(0);
            Picasso.with(con)
                    .load(Global.picUrl)
                    .placeholder(R.drawable.userdefaul)
                    .error(R.drawable.userdefaul)
                    .into(userImage);

//            userImage
            Picasso.with(con)
                    .load(list.get(position).getSharePic())
                    .into(iv);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert(position);
                }
            });

            return v;
        } else {
            View v2 = inflator.inflate(R.layout.group_right, null);
            time = (EmojiconTextView) v2.findViewById(R.id.mRtimeUI);
            name = (TextView) v2.findViewById(R.id.mRnameUI);
//
            time.setText(list.get(position).getMsg());
            name.setText(list.get(position).getName());
            iv = (ImageView) v2.findViewById(R.id.mRimageView);
            userImage = (CircularImageView) v2.findViewById(R.id.RgroupUserPic);
            userImage.setBorderWidth(0);
            Picasso.with(con)
                    .load(list.get(position).getPicUrl())
                    .placeholder(R.drawable.userdefaul)
                    .error(R.drawable.userdefaul)
                    .into(userImage);

            Picasso.with(con)
                    .load(list.get(position).getSharePic())
                    .into(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert(position);
                }
            });

            return v2;
        }
    }

    public void alert(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("Save Image");
        final Bitmap bitmap = loadBitmap(list.get(position).getSharePic());
        ImageView imageView = new ImageView(con);
        imageView.setImageBitmap(bit);
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
                // do something with the Bitmap
                bit = bitmap;
                handleLoadedBitmap(bitmap);
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

    public void handleLoadedBitmap(Bitmap b) {
        // do something here
        round = Utils.getRoundedShape(b);
        Log.d("Tago", "handle loded bitmap");
    }


}
