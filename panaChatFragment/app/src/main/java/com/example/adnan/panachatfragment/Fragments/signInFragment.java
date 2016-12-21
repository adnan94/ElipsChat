package com.example.adnan.panachatfragment.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.adnan.panachatfragment.Activities.AfterLoginScreen;
import com.example.adnan.panachatfragment.Activities.FacebookAuthActivity;
import com.example.adnan.panachatfragment.Adaptors.PagerAdaptor;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Calling.SinchService;
import com.example.adnan.panachatfragment.UTils.CallService;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.example.adnan.panachatfragment.UTils.Utils;
import com.example.adnan.panachatfragment.Signatures.User;
import com.facebook.Profile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;


/**
 * A simple {@link Fragment} subclass.
 */
public class signInFragment extends android.support.v4.app.Fragment implements SinchService.StartFailedListener, ServiceConnection {
    Bitmap bitmap;
    String ImgUrl;
    private SinchService.SinchServiceInterface mSinchServiceInterface;
    DatabaseReference fire;
    android.support.v4.app.FragmentTransaction ft;
    CircularImageView imageview;
    Cloudinary cloudinary;
    View v;
    String url;
    SharedPreferences pref;
    String selectedImagePath;
    Picasso picasso;
    Profile profile;
    TextView text, text2;
    ProgressDialog dialog;
    TabLayout tabLayout;
    StorageReference storegeRef, folderRef, imgRef;

    public signInFragment() {


    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().bindService(new Intent(getActivity(), SinchService.class), this, getActivity().BIND_AUTO_CREATE);
//        text = AfterLoginScreen.text;
//        text2 = AfterLoginScreen.text2;
//        imageview = AfterLoginScreen.imageview;
        //
//
        fire = Service.fire;
//

        cloudinary = Utils.cloudinary();
//

        v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        casts();
        storegeRef = FirebaseStorage.getInstance().getReference();
        picasso = Picasso.with(getActivity());
        profile = Profile.getCurrentProfile();
        profile();
        onClickImageView();
        getNavImage();
        if (profile != null) {
            FacebookAuthActivity.fa.finish();
        }
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ViewPager pager = (ViewPager) v.findViewById(R.id.view_pager);
        pager.setOffscreenPageLimit(4);
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
//        int titles[] = {R.drawable.conversation, R.drawable.friends, R.drawable.group};
        String title[] = {"Chats", "Contacts", "Groups", "Calls"};
        pager.setAdapter(new PagerAdaptor(manager, title));
        tabLayout.setupWithViewPager(pager);


        return v;

    }


    public void profile() {

/// /ok
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading...");
        dialog.setMessage("Fetching You Data Plz Wait");
        dialog.show();
//

        if (profile != null) {
            Log.d("TAGi", "" + profile.getId());
            Log.d("TAGi", "" + profile.getName());

//            text.setText(profile.getName());
//            text2.setText(profile.getName());
            pref = getActivity().getSharedPreferences("SignInData", Context.MODE_PRIVATE);


//            Global.uid = profile.getId();
//            Global.name = profile.getName();
            if (Global.uid != null) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                Log.i(TAG, "I've waited for two hole seconds to show this!");
                        Map<String, String> map = new HashMap<>();
                        map.put("value", "online");
                        fire.child("AppData").child("Status").child(Global.uid).setValue(map);

                    }
                }, 3000);
            }


            fire.child("AppData").child("BasicInfo").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(profile.getId())) {
                        dialog.dismiss();
                        get(profile.getId());
                    } else {

                        Toast.makeText(getActivity(), "Saving New User Data", Toast.LENGTH_SHORT).show();
                        Log.d("User Data Store", "New User Data Entry SAVING data");
                        fire.child("AppData").child("BasicInfo").child(profile.getId()).setValue(new User(profile.getName(), "N/A", "Available", "Not Available", "00000-0000", "Not Available", Global.uid));

                        Global.birthday = "Not Available";
                        Global.name = profile.getName();
                        Global.uid = profile.getId();
                        Global.picUrl = "N/A";
                        Global.contact = "00000-0000";
                        Global.status = "Available";
                        Global.email = "Not Available";
                        dialog.dismiss();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
//            Log.d("TAGi", "" + profile.getId());
//            Log.d("TAGi", "" + profile.getName());

//            text.setText(profile.getName());


            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            text2.setText(profile.getName());
//            pref = getActivity().getSharedPreferences("SignInData", Context.MODE_PRIVATE);


            if (userId != null) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                Log.i(TAG, "I've waited for two hole seconds to show this!");
                        Map<String, String> map = new HashMap<>();
                        map.put("value", "online");
                        fire.child("AppData").child("Status").child(userId).setValue(map);

                    }
                }, 3000);
            }


            fire.child("AppData").child("BasicInfo").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

//                    if (dataSnapshot.hasChild(userId)) {
                        dialog.dismiss();
                        get(userId);
//                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }

    public void onClickImageView() {
        imageview.setBorderWidth(0);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
//            try {
////                Uri yy = data.getData();
////                Log.d("dfdf", "picture");
////                performCrop(yy);
//            } catch (Exception e) {
////                e.printStackTrace();
//            }
            String path = getRealPathFromURI(data.getData());
            getImage(path, data);
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            Bundle extras = data.getExtras();
            Uri img = data.getData();
            bitmap = extras.getParcelable("data");
            String path = saveImage(bitmap);
            selectedImagePath = path;
            Log.d("PATH", path);
            decodeFile(path);
            if (img == null) {

            } else {

            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public void get(String id) {
        FireBaseHandler.getInstance().getCurrentProfileData(id, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                Log.d("Ultimate", u.getName());
                Log.d("Ultimate", u.getEmail());

                Global.birthday = u.getBirthday();
                Global.name = u.getName();
                Global.picUrl = u.getPicUrl();
                Global.contact = u.getContact();
                Global.status = u.getStatus();
                Global.email = u.getEmail();
                text.setText(Global.name );
                dialog.dismiss();

            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });


    }

    public void casts() {

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
//        navigationView.addHeaderView(View.inflate(getActivity(),R.layout.nav_header_after_login_screen,null));
        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header_after_login_screen);
        text = (TextView) headerLayout.findViewById(R.id.nameNav);
        imageview = (CircularImageView) headerLayout.findViewById(R.id.imageView);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);

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
                        Bitmap oo = Utils.getRoundedShape(bitmap);
                        imageview.setImageBitmap(oo);
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
//                                fire.child("AppData").child("Picture").child(Global.uid).child("PicUrl").setValue(url);
                                Global.picUrl = url;
                                Log.d("HHH", Global.status);
                                Log.d("HHH", Global.email);
                                fire.child("AppData").child("BasicInfo").child(Global.uid).setValue(new User(Global.name, url, Global.status, Global.email, Global.contact, Global.birthday, Global.uid), new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        Snackbar.make(imageview, "Picture Updated", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                    }
                                });

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


    public void getNavImage() {
        fire.child("AppData").child("BasicInfo").child(Global.uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                try {
                    Toast.makeText(getActivity(), Global.uid, Toast.LENGTH_SHORT).show();
                    ImgUrl = dataSnapshot.child("picUrl").getValue().toString();
                    loadBitmap(ImgUrl);
                    Global.picUrl = ImgUrl;
                    addPrefs();
                    Log.d("Tag", "" + ImgUrl);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addPrefs() {

//    Log.d("rrrrr", "" + profile.getId());
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("uid", Global.uid);
        edit.putString("name", Global.name);
        edit.putString("picUrl", Global.picUrl);
//            edit.putString("name", profile.getName());

        edit.commit();

    }
//                .into(imageView);

    private Target loadtarget;


    public void loadBitmap(String url) {

        if (loadtarget == null) loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // do something with the Bitmap
                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {

            }

            @Override
            public void onPrepareLoad(Drawable drawable) {

            }


        };

        Picasso.with(getActivity()).load(url).into(loadtarget);
    }


    public void handleLoadedBitmap(Bitmap b) {
        // do something here
        Bitmap round = Utils.getRoundedShape(b);
        Log.d("Tago", "handle loded bitmap");
        Global.myRoundedPic = round;
        imageview.setImageBitmap(round);
    }


    @Override
    public void onStarted() {
//        Toast.makeText(this, "Service started mainActivity", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStartFailed(SinchError error) {
//        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;

            onServiceDisconnected();
        }
    }

    private void onServiceConnected() {
        mSinchServiceInterface.setStartListener(this);
        if (!mSinchServiceInterface.isStarted()) {
            mSinchServiceInterface.startClient(Global.uid);
            Global.flag = true;
        }
    }

    private void onServiceDisconnected() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {

//        Toast.makeText(getActivity(),"onResumeSignIn",Toast.LENGTH_SHORT).show();

//        ImageButton grp = (ImageButton) getActivity().findViewById(R.id.searchGroups);
//        grp.setVisibility(View.VISIBLE);
        TextView text2 = (TextView) getActivity().findViewById(R.id.title);
        text2.setText(Global.name);
//        ImageButton add = (ImageButton) getActivity().findViewById(R.id.addFriendButton);
//        add.setVisibility(View.VISIBLE);

        super.onStart();


    }


    public void getImage(String path, Intent data) {
        Uri file = Uri.fromFile(new File(path));
        Log.d("TAG", file.toString());

//        StorageReference riversRef = storegeRef.child("image/");
        File f = new File(path);

        imgRef = storegeRef.child("imageOnly").child(f.getName());

        UploadTask uploadTask = imgRef.putFile(file);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                String urll = taskSnapshot.getDownloadUrl().toString();
                Global.picUrl = urll;
                Log.d("HHH", Global.status);
                Log.d("HHH", Global.email);
                fire.child("AppData").child("BasicInfo").child(Global.uid).setValue(new User(Global.name, urll, Global.status, Global.email, Global.contact, Global.birthday, Global.uid), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Snackbar.make(imageview, "Picture Updated", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Uploading Failed", Toast.LENGTH_SHORT).show();
                Log.d("TAG", e.getMessage().toString());
            }
        });

        try {
            Bitmap ii = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            imageview.setImageBitmap(ii);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}





