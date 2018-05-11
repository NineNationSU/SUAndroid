package com.android.suapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import static com.android.suapp.LoginActivity.APP_PREFERENCES;

/**
 * Created by fokin on 09.05.2018.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        if(!hasVisited) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
        }
        else{
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
