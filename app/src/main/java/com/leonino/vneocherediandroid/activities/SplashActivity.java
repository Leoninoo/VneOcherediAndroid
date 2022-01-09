package com.leonino.vneocherediandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.leonino.vneocherediandroid.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation animationVne = AnimationUtils.loadAnimation(this, R.anim.motion_splash_text1);
        Animation animationOcheredi = AnimationUtils.loadAnimation(this, R.anim.motion_splash_text2);

        TextView vne = findViewById(R.id.vne_text);
        TextView ocheredi = findViewById(R.id.ocheredi_text);

        Intent intent = new Intent(this, LoginActivity.class);
        Activity activity = this;

        animationVne.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ocheredi.startAnimation(animationOcheredi);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        vne.startAnimation(animationVne);
    }
}