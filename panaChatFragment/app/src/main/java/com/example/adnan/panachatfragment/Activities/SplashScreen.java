package com.example.adnan.panachatfragment.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.adnan.panachatfragment.R;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    int progressStatus = 0;
    TextView logo, textView2, line;
    Handler handler = new Handler();
    TextView textView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        textView2 = (TextView) findViewById(R.id.load_per);

        logo = (TextView) findViewById(R.id.textView5);
        line = (TextView) findViewById(R.id.textView6);

        YoYo.with(Techniques.Shake)
                .duration(1000)
                .playOn(findViewById(R.id.textView5));
        YoYo.with(Techniques.Shake)
                .duration(1000)
                .playOn(findViewById(R.id.textView6));

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 10;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView2.setText(progressStatus + "%");
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (progressStatus == 100) {

                    Intent ii = new Intent(SplashScreen.this, FacebookAuthActivity.class);
                    startActivity(ii);
                    finish();

                }

            }
        }).start();
    }


}
