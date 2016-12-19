package com.example.adnan.panachatfragment.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.example.adnan.panachatfragment.Adaptors.FriendReq_ADaptor;
import com.example.adnan.panachatfragment.Calling.BaseActivity;
import com.example.adnan.panachatfragment.Fragments.AddNewGroup;
import com.example.adnan.panachatfragment.Fragments.AllGroupsList;
import com.example.adnan.panachatfragment.Fragments.ContactsInformation;
import com.example.adnan.panachatfragment.Fragments.FindFriends;
import com.example.adnan.panachatfragment.Fragments.Friend_Request_Fragment;
import com.example.adnan.panachatfragment.Fragments.Main_group_chat;
import com.example.adnan.panachatfragment.Fragments.MyProfile;
import com.example.adnan.panachatfragment.Fragments.StatusFragment;
import com.example.adnan.panachatfragment.Fragments.signInFragment;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.Frient_Req_Signature;
import com.example.adnan.panachatfragment.UTils.CallService;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.InterFace;
import com.example.adnan.panachatfragment.UTils.Service;
import com.example.adnan.panachatfragment.UTils.Utils;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class AfterLoginScreen extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener


{
    public static Context context;
    public static Activity acti;
    signInFragment signInFragment;
    ImageButton addFriendButton;
    DatabaseReference fire;
    ArrayList<Frient_Req_Signature> listNoti;

    FriendReq_ADaptor adaptorNoti;

    DrawerLayout drawer;
    ImageView notification;
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD = 1234;
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG = 5678;
    FragmentManager manager;
    public static TextView text, text2;
    public static CircularImageView imageview;
    private static final int[] ITEM_DRAWABLES = {R.drawable.groupadd, R.drawable.friendadd};
    private ArcMenu arcMenu;
    View indicator;
    //    ArrayAdapter<String> arrayAdapter;
//    ArrayList<String> str;
    PopupWindow popupWindow;
    TextView alternate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = AfterLoginScreen.this;
        fire = Service.fire;


        setContentView(R.layout.activity_after_login_screen);


        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);


        acti = AfterLoginScreen.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = AfterLoginScreen.this.getSupportFragmentManager();

        indicator = (View) findViewById(R.id.indicator);

        final LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.friend_req_fragment, null);
        final ListView list = (ListView) popupView.findViewById(R.id.listView);
        alternate = (TextView) popupView.findViewById(R.id.alternate);
        listNoti = new ArrayList<>();
        adaptorNoti = new FriendReq_ADaptor(listNoti, this);
        getList();
        checkPermission();

        signInFragment = new signInFragment();
        transactionOfLoginScreen();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int posi = i;
                AlertDialog.Builder alert = new AlertDialog.Builder(AfterLoginScreen.this);
                alert.setTitle("What You Want To Do");
                alert.setMessage("Are you sure you want to add this person ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idFriend = listNoti.get(posi).getSenderId();
                        fire.child("AppData").child("Friends").child(listNoti.get(posi).getSenderId()).child(Global.uid).setValue(new Frient_Req_Signature(Global.name, Global.picUrl, Global.uid));
                        fire.child("AppData").child("Friends").child(Global.uid).child(listNoti.get(posi).getSenderId()).setValue(new Frient_Req_Signature(listNoti.get(posi).getName(), listNoti.get(posi).getPicUrl(), listNoti.get(posi).getSenderId()));

                        fire.child("AppData").child("FriendRequests").child(Global.uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                int pos = 0;
                                for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    if (pos == posi) {
                                        DatabaseReference ref = dataSnapshot1.getRef();
                                        ref.removeValue();
                                        listNoti.remove(posi);

                                        adaptorNoti.notifyDataSetChanged();
                                    } else {
                                        pos++;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(AfterLoginScreen.this, "Friend Is Add To Your List ", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.create();
                alert.show();
            }
        });
        notification = (ImageView) findViewById(R.id.notifications);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                listNoti.clear();

//                list.setAdapter(adaptorNoti);
                if (popupWindow == null) {
                    popupWindow = new PopupWindow(
                            popupView,
                            ActionBar.LayoutParams.WRAP_CONTENT,
                            ActionBar.LayoutParams.WRAP_CONTENT);
                    list.setAdapter(adaptorNoti);
                    popupWindow.showAsDropDown(notification, 50, -2);

                } else if (popupWindow.isShowing()) {
                    if (listNoti.size() == 0) {
                        indicator.setVisibility(View.GONE);
                    }
                    popupWindow.dismiss();
                } else if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(notification, 50, -2);

                }
//                Toast.makeText(AfterLoginScreen.this, "no notifications yet", Toast.LENGTH_SHORT).show();
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                findViewById(R.id.test).setTranslationX(drawerView.getWidth() * slideOffset);
                drawer.setScrimColor(Color.TRANSPARENT);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.scrollTo(5, 10);
        navigationView.setNavigationItemSelectedListener(this);
        arcMenu = (ArcMenu) findViewById(R.id.arc_menu);
        initArcMenu(arcMenu, ITEM_DRAWABLES);
        final int itemCount = ITEM_DRAWABLES.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(ITEM_DRAWABLES[i]);

            final int position = i;
        }

    }

    public void getList() {
        FireBaseHandler.getInstance().getFriendRequestListListener(Global.uid, new InterFace<com.google.firebase.database.DataSnapshot, FirebaseError>() {
            @Override
            public void sucess(com.google.firebase.database.DataSnapshot dataSnapshot) {
                indicator.setVisibility(View.VISIBLE);
                alternate.setVisibility(View.GONE);
                String key = dataSnapshot.getKey();
                Frient_Req_Signature signature = dataSnapshot.getValue(Frient_Req_Signature.class);
                Log.d("Tagee", key);
                listNoti.add(new Frient_Req_Signature(signature.getName(), signature.getPicUrl(), key));

//                count = list.size();


                adaptorNoti.notifyDataSetChanged();

            }

            @Override
            public void fail(FirebaseError obj) {

            }
        });



    }

    private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            menu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (position == 1) {
                        FragmentManager manager = AfterLoginScreen.this.getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = manager.beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.out_from_left);
                        FindFriends addFriendFragment = new FindFriends();
                        ft.add(R.id.RelativeLayoutHomeScreen, addFriendFragment);
                        ft.addToBackStack(null);
                        ft.commit();

                    } else {
                        FragmentManager manager = AfterLoginScreen.this.getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = manager.beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.out_from_left);
                        ft.add(R.id.RelativeLayoutHomeScreen, new AllGroupsList());
                        ft.addToBackStack(null);
                        ft.commit();

                    }
                }
            });
        }
    }


    public void checkPermission() {
        if (Utils.canDrawOverlays(AfterLoginScreen.this)) {

        } else {
            requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG);
        }


    }

    private void requestPermission(int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OVERLAY_PERMISSION_REQ_CODE_CHATHEAD) {
            if (!Utils.canDrawOverlays(AfterLoginScreen.this)) {
                needPermissionDialog(requestCode);
            } else {
//                startChatHead();
            }

        } else if (requestCode == OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG) {
            if (!Utils.canDrawOverlays(AfterLoginScreen.this)) {
                needPermissionDialog(requestCode);
            } else {
//                showChatHeadMsg();
            }

        }

    }

    private void needPermissionDialog(final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AfterLoginScreen.this);
        builder.setMessage("You need to allow permission");
        builder.setPositiveButton("OK",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        requestPermission(requestCode);
                    }
                });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(1).getId(), getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            arcMenu.setVisibility(View.GONE);

        } else {
            super.onBackPressed();
            arcMenu.setVisibility(View.VISIBLE);
        }
    }


    public void finis() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.after_login_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.contact_Infoo) {

//            FragmentManager manager = AfterLoginScreen.this.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ftt = manager.beginTransaction();
            ftt.add(R.id.RelativeLayoutHomeScreen, new ContactsInformation());
            ftt.addToBackStack(null);
            ftt.commit();

        } else if (id == R.id.updateStatus) {

            android.support.v4.app.FragmentTransaction ftt1 = manager.beginTransaction().setCustomAnimations(R.anim.bottom_to_top, R.anim.out_from_left);
            ftt1.add(R.id.RelativeLayoutHomeScreen, new StatusFragment());
            ftt1.addToBackStack(null);

            ftt1.commit();

        } else if (id == R.id.addGroup) {
            android.support.v4.app.FragmentTransaction ftt = manager.beginTransaction().setCustomAnimations(R.anim.bottom_to_top, R.anim.out_from_left);
            ftt.add(R.id.RelativeLayoutHomeScreen, new AddNewGroup());
            ftt.addToBackStack(null);

            ftt.commit();

        } else if (id == R.id.mMyProfile) {

            android.support.v4.app.FragmentTransaction ftt = manager.beginTransaction().setCustomAnimations(R.anim.bottom_to_top, R.anim.out_from_left);
            ftt.add(R.id.RelativeLayoutHomeScreen, new MyProfile());
            ftt.addToBackStack(null);

            ftt.commit();

        } else if (id == R.id.fb_logout_button) {

//
            final SharedPreferences pref = getApplicationContext().getSharedPreferences("SignInData", Context.MODE_PRIVATE);
            SharedPreferences.Editor sdit = pref.edit();
            sdit.clear();
            sdit.commit();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LoginManager.getInstance().logOut();
                    Intent stop = new Intent(AfterLoginScreen.this, Service.class);
                    stopService(stop);
                    pref.edit().remove("uid");
                    pref.edit().remove("name");
                    pref.edit().remove("picUrl");

//                    SharedPreferences.Editor edit = pref.edit();
//                    edit.putString("uid", profile.getId());
//                    edit.putString("name", profile.getName());
//                    edit.putString("picUrl",Global.picUrl);
//                    Service s = new Service();
//                    s.stopSelf();

                    finish();

                    Intent i = new Intent(AfterLoginScreen.this, FacebookAuthActivity.class);
                    startActivity(i);


                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.setTitle("Alert");
            alert.setMessage("Are you sure you want to Logout ?");
            AlertDialog ad = alert.create();

            alert.show();


        }
// else if (id == R.id.FriendRequest) {
//
//            android.support.v4.app.FragmentTransaction ftt = manager.beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.in_from_right, R.anim.out_from_left, R.anim.out_from_left);
//            ftt.add(R.id.RelativeLayoutHomeScreen, new Friend_Request_Fragment());
//            ftt.addToBackStack(null);
//            ftt.commit();
//
//
//        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void transactionOfLoginScreen() {
        Intent i = new Intent(AfterLoginScreen.this, Service.class);
        startService(i);

        android.support.v4.app.FragmentTransaction ft2 = AfterLoginScreen.this.getSupportFragmentManager().beginTransaction();
        ft2.replace(R.id.RelativeLayoutHomeScreen, signInFragment);
        ft2.commit();

    }


    @Override
    protected void onPause() {
//        Log.d("nnn", "pause");
//        Global.Afteractive = true;

        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        Global.Afteractive = false;
        Map<String, String> map = new HashMap<>();
        map.put("value", "offline");
        fire.child("AppData").child("Status").child(Global.uid).setValue(map);


        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


}
