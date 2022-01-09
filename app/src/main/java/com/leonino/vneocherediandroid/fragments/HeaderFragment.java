package com.leonino.vneocherediandroid.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.leonino.vneocherediandroid.R;


public class HeaderFragment extends Fragment {
    private Context context;

    public HeaderFragment() {}

    public HeaderFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View headerView = inflater.inflate(R.layout.fragment_header, container, false);

        boolean flagLogin = getArguments() != null && getArguments().getBoolean("flagLogin");
        
        if(flagLogin) {
            animateText(headerView);
        }

        headerView.findViewById(R.id.header_ava).setOnClickListener(view -> {
            Intent intent = new Intent(context, AppCompatActivity.class);

            context.startActivity(intent);
        });

        return headerView;
    }

    @SuppressLint("SetTextI18n")
    private void animateText(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        ImageView logo = view.findViewById(R.id.logo);
        ImageView avatar = view.findViewById(R.id.header_ava);

        TextView findText1 = view.findViewById(R.id.find_text1);
        TextView findText2 = view.findViewById(R.id.find_text2);

        TextView welcomeUsername = view.findViewById(R.id.welcome_username);
        TextView welcomeText = view.findViewById(R.id.welcome_text);

        findText1.setVisibility(View.INVISIBLE);
        findText2.setVisibility(View.INVISIBLE);
        avatar.setVisibility(View.INVISIBLE);
        welcomeUsername.setVisibility(View.VISIBLE);
        welcomeText.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);

        welcomeUsername.setText("Привет, " + preferences.getString("username", "друг"));

        Animation animationWelcomeUsernameStart =
                AnimationUtils.loadAnimation(context, R.anim.motion_welcome_username_start);
        Animation animationWelcomeUsernameEnd =
                AnimationUtils.loadAnimation(context, R.anim.motion_welcome_username_end);
        Animation animationWelcomeTextStart =
                AnimationUtils.loadAnimation(context, R.anim.motion_welcome_text_start);
        Animation animationWelcomeTextEnd =
                AnimationUtils.loadAnimation(context, R.anim.motion_welcome_text_end);
        Animation animationText1 = AnimationUtils.loadAnimation(context, R.anim.motion_find_text1);
        Animation animationText2 = AnimationUtils.loadAnimation(context, R.anim.motion_find_text2);

        animationWelcomeUsernameStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                welcomeText.startAnimation(animationWelcomeTextStart);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                welcomeUsername.startAnimation(animationWelcomeUsernameEnd);
                welcomeText.startAnimation(animationWelcomeTextEnd);

                animateImage(logo, avatar);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        animationWelcomeUsernameEnd.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                welcomeUsername.setVisibility(View.INVISIBLE);
                welcomeText.setVisibility(View.INVISIBLE);
                findText1.setVisibility(View.VISIBLE);
                findText2.setVisibility(View.VISIBLE);

                findText1.startAnimation(animationText1);
                findText2.startAnimation(animationText2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });


        welcomeUsername.startAnimation(animationWelcomeUsernameStart);
    }

    private void animateImage(ImageView logo, ImageView avatar) {
        logo.setVisibility(View.VISIBLE);
        avatar.setVisibility(View.INVISIBLE);

        Animation animationLogo = AnimationUtils.loadAnimation(context, R.anim.alpha_logo);
        Animation animationAvatar = AnimationUtils.loadAnimation(context, R.anim.alpha_avatar);

        animationLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setVisibility(View.INVISIBLE);
                avatar.setVisibility(View.VISIBLE);

                avatar.startAnimation(animationAvatar);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        logo.startAnimation(animationLogo);
    }
}