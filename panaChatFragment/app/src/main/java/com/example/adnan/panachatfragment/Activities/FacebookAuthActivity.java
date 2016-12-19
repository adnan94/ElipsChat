package com.example.adnan.panachatfragment.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.adnan.panachatfragment.Adaptors.SliderAdaptor;
import com.example.adnan.panachatfragment.Fragments.AllGroupsList;
import com.example.adnan.panachatfragment.Fragments.SignUpFragment;
import com.example.adnan.panachatfragment.Fragments.signInFragment;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.UTils.Service;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.viewpagerindicator.CirclePageIndicator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import java.security.Signature;


public class FacebookAuthActivity extends AppCompatActivity {

    public static Context con;
    public static Activity fa;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    AccessToken accessToken;
    android.support.v4.app.FragmentTransaction ft;
    ArrayList<Integer> list = new ArrayList<>();
    ViewPager pager;
    int currentItem = 0;
    int numPages = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fa = FacebookAuthActivity.this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
      String hash=printKeyHash(this);
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);
        con = FacebookAuthActivity.this;
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        Log.d("TAGi",hash);
        init();
        ((Button)findViewById(R.id.SubmitBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FacebookAuthActivity.this, "Currently not implemented", Toast.LENGTH_SHORT).show();
            }
        });

        ((TextView)findViewById(R.id.textSignUp1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = manager.beginTransaction().setCustomAnimations(R.anim.bottom_to_top, R.anim.top_to_bottom);
                ft.add(R.id.mainLayout, new SignUpFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        ft = this.getSupportFragmentManager().beginTransaction();
        checkAuth();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
//                        Log.d("Logii", "on sucess login button");
//                        Toast.makeText(FacebookAuthActivity.this, "on sucess login button", Toast.LENGTH_SHORT).show();

                        accessToken = loginResult.getAccessToken();
                        if (accessToken != null) {
//                            Toast.makeText(FacebookAuthActivity.this, "On sucess token not null", Toast.LENGTH_SHORT).show();
                            if (Profile.getCurrentProfile() != null) {
                                Global.uid = Profile.getCurrentProfile().getId();
//                                Toast.makeText(FacebookAuthActivity.this, "" + Profile.getCurrentProfile().getId(), Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(FacebookAuthActivity.this, AfterLoginScreen.class);
                                startActivity(i);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

            }
        });
    }

    public void checkAuth() {
        if (AccessToken.getCurrentAccessToken() != null) {
//            Log.d("Logii", "AcessToken not null");
//            Toast.makeText(this, "check auth token not null", Toast.LENGTH_SHORT).show();
            profileTracker();

        } else {
//            Log.d("Logii", "AcessToken null");
        }

        Log.d("Logii", "Check Auth");
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void profileTracker() {
//        Toast.makeText(this, "profile tracker", Toast.LENGTH_SHORT).show();

        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
//            Toast.makeText(this, "profile tracker profile not null", Toast.LENGTH_SHORT).show();
            Global.uid = profile.getId();
//            Toast.makeText(FacebookAuthActivity.this, "" + profile.getId(), Toast.LENGTH_SHORT).show();

//            Log.d("Logii", "Profile Goted");
            Intent i = new Intent(FacebookAuthActivity.this, AfterLoginScreen.class);
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void toast(String message)

    {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void init() {


        Intent ii = new Intent(FacebookAuthActivity.this, Service.class);
        this.startService(ii);
        list.add(R.drawable.aa);
        list.add(R.drawable.bb);
        list.add(R.drawable.cc);
        list.add(R.drawable.dd);
        list.add(R.drawable.ff);
        list.add(R.drawable.ee);


        pager = (ViewPager) findViewById(R.id.pager);
        SliderAdaptor adapter = new SliderAdaptor(this, list);

        pager.setAdapter(adapter);

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(pager);
        final float density = getResources().getDisplayMetrics().density;


        indicator.setRadius(3 * density);

        numPages = list.size();

// Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentItem == numPages) {
                    currentItem = 0;
                }
                pager.setCurrentItem(currentItem++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentItem = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}
