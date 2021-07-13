package in.app.dharm.info.online.shopping.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.VideoView;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.common.DataProcessor;

public class SplashActivity extends AppCompatActivity {
    int SPLASH_TIME_OUT = 2000;
    DataProcessor dataProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //// To Save a value
        dataProcessor = new DataProcessor(this);

//        dataProccessor.setStr("email","johndoe@mail.com");
        //// To Retreive a value
//        dataProccessor.getStr("email");
        try {
            VideoView videoHolder = new VideoView(this);
            setContentView(videoHolder);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.app_welcome_view);
            videoHolder.setVideoURI(video);

            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });
            videoHolder.start();
        } catch (Exception ex) {
            jump();
        }
     /*   new Handler().postDelayed(new Runnable() {

            *//*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             *//*

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity


            }
        }, SPLASH_TIME_OUT);*/

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        if (dataProcessor.getBool("isLogin") == true || dataProcessor.getBool("isSkip") == true)  {
            startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
            finishAffinity();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finishAffinity();
        }
    }
}