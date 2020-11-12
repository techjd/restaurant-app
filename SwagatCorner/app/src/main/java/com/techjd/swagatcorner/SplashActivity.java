package com.techjd.swagatcorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");


        Log.d("TOKEN", token);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (token.isEmpty()) {
                    Intent openLogin = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(openLogin);
                    finish();
                } else {
                    Intent openMain = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(openMain);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
