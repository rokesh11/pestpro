package com.example.pestpro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=2450;
    private ImageView bgapp,clover;
    private LinearLayout textSplash,textHome,menus;

    Animation fromBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fromBottom= AnimationUtils.loadAnimation(SplashActivity.this,R.anim.frombottom);

        bgapp=findViewById(R.id.bgapp);
        clover=findViewById(R.id.clover);
        textSplash=findViewById(R.id.textSplash);
        textHome=findViewById(R.id.textHome);
        menus=findViewById(R.id.menus);

        bgapp.animate().translationY(-2200).setDuration(900).setStartDelay(300);
        clover.animate().alpha(0).setDuration(900).setStartDelay(600);
        textSplash.animate().translationY(140).alpha(0).setDuration(900).setStartDelay(300);

        textHome.startAnimation(fromBottom);
        menus.startAnimation(fromBottom);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, SPLASH_TIME_OUT);



    }

    @Override
    public void onBackPressed() {
        SplashActivity.this.finish(); // Remove this
        super.onBackPressed();
    }
}
