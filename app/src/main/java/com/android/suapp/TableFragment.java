package com.android.suapp;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.suapp.suapp.sdk.LKUtility;
import com.android.suapp.suapp.server.database.exceptions.IllegalObjectStateException;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.timetable.Couple;
import com.android.suapp.suapp.server.timetable.TimeTable;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


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

    /**
     * Преобразует названия предметов
     */
    private String toSimpleName(String str){
        StringBuilder answer = new StringBuilder();
        if (str.length() > 8){
            String[] array = str.split(" ");
            for(String s: array){
                answer.append(s.charAt(0));
            }
            return answer.toString().toUpperCase();
        }
        return str;
    }


    static TimeTable t;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_table, container, false);
        final Handler h = new Handler();
        final HorizontalScrollView scroll = new HorizontalScrollView(this.getContext());
        final TableLayout timeTableLayout = new TableLayout(scroll.getContext());
        final TableRow firstRow = new TableRow(timeTableLayout.getContext());
        final ArrayList<TextView> columnNames = new ArrayList<>(7);
        final TextView timeTextView = new TextView(firstRow.getContext());
        timeTextView.setText("Время");
        timeTextView.setTextSize(20);
        /*Activity myActivity = fragment.getActivity();

        //для работы с файлами юзаем активити*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String timeTable = LKUtility.getTimeTable(new Student().setId(1), "1");
                    t = new Gson().fromJson(timeTable, TimeTable.class);
                    System.err.println(t.getTime().get(0));

                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        if (t != null) {
                            //timeTextView.setText(t.getTime().get(0));
                            for (int i = 0; i < 8; i++){
                                TableRow newRow = new TableRow(timeTableLayout.getContext());
                                for (int j = 0; j < 6; j++){
                                    Couple couple = t.getWeeks().get(0).getDays().get(j).getCouple().get(i);
                                    TextView coupleName = new TextView(newRow.getContext());
                                    coupleName.setTextSize(28);
                                    if (couple != null){
                                        if (couple.getBySubgroups()){
                                            try {
                                                String s = couple.getFirstSubgroup().getDiscipline();
                                                s = toSimpleName(s);
                                                s += "    ";
                                                coupleName.setText(s);
                                            } catch (IllegalObjectStateException ignored) {}

                                        }else {
                                            try {
                                                String s = couple.getAllGroup().getDiscipline();
                                                s = toSimpleName(s);
                                                s += "    ";
                                                coupleName.setText(s);
                                            } catch (IllegalObjectStateException ignored) {}
                                        }
                                    }else{
                                        coupleName.setText("---    ");
                                    }
                                    newRow.addView(coupleName);
                                }
                                timeTableLayout.addView(newRow);
                            }
                        }
                        else
                            timeTextView.setText("sorry");
                        firstRow.addView(timeTextView);
                        timeTableLayout.addView(firstRow);
                        scroll.addView(timeTableLayout);
                        rootView.addView(scroll);
                    }
                });
            }
        }).start();


/*
        final TextView m = (TextView)rootView.findViewById(R.id.FirstTime);

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });*/

        return rootView;
    }


}
