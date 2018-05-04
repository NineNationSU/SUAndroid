package com.android.suapp;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.suapp.suapp.sdk.LKUtility;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.timetable.TimeTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;

import  android.content.Context;


/**
 * Created by fokin on 10.04.2018.
 */

public class TableFragment extends Fragment{

    String json = null;
    TableFragment fragment;
    /*
        public TableFragment newInstance() {
             return new TableFragment();
        }
    */
    public TableFragment(){}

    //GsonBuilder builder = new GsonBuilder();
    //Gson gson = builder.create();
    //Table mTable = gson.fromJson(this.loadJSONFromAsset(), Table.class);

    static TimeTable t;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_table, container, false);

        final TextView m = (TextView)rootView.findViewById(R.id.FirstTime);

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler h = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String timeTable = LKUtility.getTimeTable(new Student().setId(1), "1");
                            t = new Gson().fromJson(timeTable, TimeTable.class);
                            System.err.println(t.getTime().get(0));
                            Activity myActivity = fragment.getActivity();
                            /*
                                для работы с файлами юзаем активити
                             */
                        } catch (URISyntaxException | IOException e) {
                            e.printStackTrace();
                        }
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                if (t != null)
                                    m.setText(t.getTime().get(0));
                                else
                                    m.setText("sorry");
                            }
                        });
                    }
                }).start();


            }
        });

        return rootView;
    }


}
