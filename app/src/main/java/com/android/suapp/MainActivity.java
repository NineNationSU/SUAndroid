package com.android.suapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import static com.android.suapp.LoginActivity.APP_PREFERENCES;
import static com.android.suapp.LoginActivity.APP_PREFERENCES_LOG;
import static com.android.suapp.LoginActivity.APP_PREFERENCES_PASS;

/**
 * Created by fokin on 09.05.2018.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (sp.getBoolean("FirstRun", false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
