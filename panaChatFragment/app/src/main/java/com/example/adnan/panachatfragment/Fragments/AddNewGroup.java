package com.example.adnan.panachatfragment.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.Signature_Friend_List;
import com.example.adnan.panachatfragment.Signatures.User;
import com.example.adnan.panachatfragment.Signatures.groupMsgsSignature;
import com.example.adnan.panachatfragment.Signatures.groupsListSignature;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.Service;
import com.example.adnan.panachatfragment.UTils.Utils;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewGroup extends Fragment {
    DatabaseReference firebase;
    Button picButton, submit;
    EditText groupName;
    String url;
    String selectedImagePath;
    Bitmap bitmap;
    String urlOfGroupPic;
    Cloudinary cloudinary;
    ProgressDialog pd;
ArcMenu arcMenu;

    public AddNewGroup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cloudinary = Utils.cloudinary();
//        firebase = FireBaseHandler.fire;
        firebase = Service.fire;

        View v = inflater.inflate(R.layout.fragment_add_new_group, container, false);
        groupName = (EditText) v.findViewById(R.id.groupNameEditText);
        picButton = (Button) v.findViewById(R.id.groupPicButton);
        arcMenu = (ArcMenu) getActivity().findViewById(R.id.arc_menu);
        arcMenu.setVisibility(View.GONE);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);
            }
        });
        submit = (Button) v.findViewById(R.id.groupSubmitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("aiman", url);
                if (groupName.getText().length() > 0) {
                    if (url == null) {
                        String date = Date();
                        url = "N/A";

                        firebase.child("AppData").child("GroupInfo").child(groupName.getText().toString()).setValue(new groupsListSignature(url, Global.name, groupName.getText().toString()));
                        firebase.child("AppData").child("GroupData").child(groupName.getText().toString()).child("Conversation").push().setValue(new groupMsgsSignature(Global.name, url, Global.name + " created the group ", date, Global.uid, "N/A"));
                        firebase.child("AppData").child("MyGroups").child(Global.uid).child(groupName.getText().toString()).setValue(new groupsListSignature(url, Global.name, groupName.getText().toString()));
                        groupName.setText("");
                        Snackbar.make(submit, "Sucessfully Created", Snackbar.LENGTH_SHORT).show();
                    } else {
                        String date = Date();
                        firebase.child("AppData").child("GroupInfo").child(groupName.getText().toString()).setValue(new groupsListSignature(url, Global.name, groupName.getText().toString()));
                        firebase.child("AppData").child("MyGroups").child(Global.uid).child(groupName.getText().toString()).setValue(new groupsListSignature(url, Global.name, groupName.getText().toString()));
                        firebase.child("AppData").child("GroupData").child(groupName.getText().toString()).child("Conversation").push().setValue(new groupMsgsSignature(Global.name, url, Global.name + " created the group ", date, Global.uid, "N/A"));
                        groupName.setText("");
                        Snackbar.make(submit, "Sucessfully Created", Snackbar.LENGTH_SHORT).show();
                        url = "N/A";

                    }
                } else {
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(getActivity().findViewById(R.id.groupNameEditText));
                    groupName.setError("Write group name first");

                }
            }
        });
        return v;
    }

    public String Date() {
        String date = DateFormat.getDateTimeInstance().format(new Date());
        //24 hour format


        return date;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri yy = data.getData();
                Log.d("dfdf", "picture");
                performCrop(yy);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pd = new ProgressDialog(getActivity());
            pd.setTitle("Uploading..");
            pd.setMessage("Plz Wait While Uploading Uploading..");
            pd.show();
            Bundle extras = data.getExtras();
            Uri img = data.getData();
            bitmap = extras.getParcelable("data");
            String path = saveImage(bitmap);
            selectedImagePath = path;
            Log.d("PATH", path);
            decodeFile(path);
            if (img == null) {
                Log.d("FUCK", "null");

            } else {
                Log.d("FUCK", "not null");

            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void performCrop(Uri picUri) {
        try {
            //Start Crop Activity
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 2);
        } catch (Exception e) {

        }
    }

    private String saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/ElipsGroups/ProfileImages");

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

        return root + "/ElipsGroups/ProfileImages/" + fname;
    }

    private void decodeFile(String filePath) {
        Log.d("INVOKED", "Decode File");

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        final int REQUIRED_SIZE = 1024;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        new AlertDialog.Builder(getActivity())
                .setTitle("Upload Picture")
                .setMessage("Are you sure you want to upload picture?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Bitmap oo = getRoundedShape(bitmap);
//                        imageview.setImageBitmap(oo);
                        Log.d("File PATH IS ", selectedImagePath + "");
                        ////////////////////////////////UPLOADING CLOUDINARY////////////////////

                        AsyncTask<String, Void, HashMap<String, Object>> upload = new AsyncTask<String, Void, HashMap<String, Object>>() {
                            @Override
                            protected HashMap<String, Object> doInBackground(String... params) {
                                File file = new File(selectedImagePath);
                                HashMap<String, Object> responseFromServer = null;
                                try {
                                    responseFromServer = (HashMap<String, Object>) cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                                } catch (IOException e) {
                                    Toast.makeText(getActivity(), "Cannot Upload Image Please Try Again", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                return responseFromServer;
                            }

                            @Override
                            protected void onPostExecute(HashMap<String, Object> stringObjectHashMap) {
                                url = (String) stringObjectHashMap.get("url");
                                pd.dismiss();
//                                fire.child("AppData").child("Picture").child(Global.uid).child("PicUrl").setValue(url);
                                Log.d("aiman", "URL" + url);

//

                            }
                        };
                        upload.execute(selectedImagePath);
                        ////////////////////////////////UPLOADING COMPLETED////////////////////////////////////////
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Global.addNewGroupFlag = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Global.addNewGroupFlag = false;
        arcMenu.setVisibility(View.VISIBLE);

    }
}
