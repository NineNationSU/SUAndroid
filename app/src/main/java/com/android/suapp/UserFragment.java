package com.android.suapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;

import static com.android.suapp.LoginActivity.APP_PREFERENCES;
import static com.android.suapp.LoginActivity.APP_PREFERENCES_LOG;
import static com.android.suapp.LoginActivity.APP_PREFERENCES_PASS;

/**
 * Created by fokin on 10.04.2018.
 */

public class UserFragment extends Fragment implements View.OnClickListener {

    private ImageButton buttonSettings;
    private SharedPreferences sp;

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    public UserFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        buttonSettings = rootView.findViewById(R.id.user_settings);
        buttonSettings.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        sp = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("FirstRun", true);
        editor.commit();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);


    }
}