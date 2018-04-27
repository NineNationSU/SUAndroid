package com.android.suapp;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.suapp.suapp.RaspController;
import com.android.suapp.suapp.sdk.LKUtility;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.timetable.TimeTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;


/**
 * Created by fokin on 10.04.2018.
 */

public class TableFragment extends Fragment {

    String json = null;



    public static TableFragment newInstance() {
        TableFragment fragment = new TableFragment();
        //RaspController raspController =
        //new RaspController();
        return fragment;
    }

    public TableFragment(){}

    //GsonBuilder builder = new GsonBuilder();
    //Gson gson = builder.create();
    //Table mTable = gson.fromJson(this.loadJSONFromAsset(), Table.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_table, container, false);

        final TextView m = (TextView)rootView.findViewById(R.id.FirstTime);


        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] s = {""};
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TimeTable t;
                        try {
                            t = new Gson().fromJson(
                                    LKUtility.getTimeTable(new Student().setId(1), "1"), TimeTable.class);
                            m.setText(t.getTime().get(0));
                        } catch (URISyntaxException | IOException e) {
                            e.printStackTrace();
                            m.setText("Ошибка");
                        }
                    }
                }, 0);
            }
        });

        return rootView;
    }


}
