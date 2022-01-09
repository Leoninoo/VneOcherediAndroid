package com.leonino.vneocherediandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leonino.vneocherediandroid.R;
import com.leonino.vneocherediandroid.forms.LoginForm;
import com.leonino.vneocherediandroid.models.User;
import com.leonino.vneocherediandroid.service.UserService;

public class LoginActivity extends AppCompatActivity {
    private UserService userService;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        userService = new UserService();

        String token = preferences.getString("token", "");

        if(!token.isEmpty()) {
            //if(userService.getUser(token).getLogin() != null)
                startMainActivity();
        }

        setListeners();
    }

    public void setListeners() {
        View signInButton = findViewById(R.id.signIn_button);

        signInButton.setOnClickListener(view -> {
            String number = ((EditText) findViewById(R.id.form_login_number)).getText().toString();
            String password = ((EditText) findViewById(R.id.form_login_password)).getText().toString();

            if(number.isEmpty()) {
                Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.isEmpty()) {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_LONG).show();
                return;
            }

//            String token = userService.login(new LoginForm(number, password, false));
//
//            if(token.equals("login")) {
//                Toast.makeText(this, "Неверный пароль", Toast.LENGTH_LONG).show();
//                return;
//            }

            editor.putString("token", "a2WdEF13OIEk");
            editor.putString("username", "Константин");

            editor.commit();

            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("flagLogin", true);

        startActivity(intent);
        finish();
    }
}