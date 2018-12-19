package com.example.jun.whereareyou.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jun.whereareyou.R;



public class SplashScreen extends Activity {

    int SPLASH_TIME=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(0, android.R.anim.fade_in);
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_TIME);
    }
}



