package com.android.suapp.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.suapp.R;

public class MainActivityNotes extends AppCompatActivity {
    public static volatile String discipline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notes);
        savedInstanceState = getIntent().getExtras();
        discipline = savedInstanceState.getString("name");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_2, BodyNotesFragment.newInstance())
                .commit();
    }

}
