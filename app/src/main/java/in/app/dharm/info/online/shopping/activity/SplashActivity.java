package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    int SPLASH_TIME_OUT = 2000;
    DataProcessor dataProcessor;
    FirebaseFirestore db;
    VideoView videoHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //// To Save a value
        dataProcessor = new DataProcessor(this);
        db = FirebaseFirestore.getInstance();

        checkAppStatus();
        try {
            videoHolder = new VideoView(this);
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

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        checkAppStatus();

    }

    private void checkLoginStatus() {
        if (dataProcessor.getBool("isLogin") == true || dataProcessor.getBool("isSkip") == true) {
            startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
            finishAffinity();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finishAffinity();
        }
    }


    private void checkAppStatus() {
        db.collection("app_status")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.getId().equals("dharm_customer")) {
                                    if (document.getString("app_status").equals("true")) {
                                        checkLoginStatus();
                                    } else {
                                        startActivity(new Intent(SplashActivity.this, UnderConstructionActivity.class));
                                        finishAffinity();
                                    }
                                }
                            }
                        } else {
                            checkLoginStatus();
                            Log.d(TAG, "Error getting app_status: ", task.getException());
                        }
                    }
                });
    }

}