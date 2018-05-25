package com.android.suapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.suapp.suapp.sdk.SUAppServer;
import com.android.suapp.suapp.server.database.exceptions.IllegalObjectStateException;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.responses.ServerResponse;
import com.android.suapp.suapp.server.timetable.Couple;
import com.android.suapp.suapp.server.timetable.Lesson;
import com.android.suapp.suapp.server.timetable.TimeTable;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.android.suapp.LoginActivity.APP_PREFERENCES;
import static com.android.suapp.LoginActivity.APP_PREFERENCES_STUDENT_DATA;


/**
 * Created by fokin on 10.04.2018.
 */

public class TableFragment extends Fragment {

    public static SharedPreferences sсhedule;
    public static SharedPreferences notes;
    public static final String TIME_TABLE_PREFERENCES = "JsonFile";
    public static final String LESSONS_PREFERENCES = "TimeTable";



    private String disciplines = "";
    private TextView parity;
    private HorizontalScrollView horizontalScrollView;
    private TableLayout timeTableLayout;
    private TableRow row1;
    private int parity_num_week = 0;


    @SuppressLint("StaticFieldLeak")
    private static TableFragment instance;
    public static TableFragment newInstance() {
        if (instance == null){
            instance = new TableFragment();
        }
        return instance;
    }


    final String[] Days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};


    public TableFragment(){}

    /**
     * Преобразует названия предметов
     */
    public static String toSimpleName(String str) {
        StringBuilder answer = new StringBuilder();
        if (str.length() > 8) {
            String[] array = str.split(" ");
            for (String s : array) {
                answer.append(s.charAt(0));
            }
            return answer.toString().toUpperCase();
        }
        return str;
    }


    static TimeTable t;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_table, container, false);

        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        row1 = rootView.findViewById(R.id.Row1);
        horizontalScrollView = rootView.findViewById(R.id.scroll);
        timeTableLayout = rootView.findViewById(R.id.table_layout_lessons);

        timeTableLayout.setOnClickListener(null);
        final Handler h = new Handler();




        //для работы с файлами юзаем активити*/
        final Handler h1 = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Student student;
                    SharedPreferences studentData = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    student =new Gson().fromJson(studentData.getString(APP_PREFERENCES_STUDENT_DATA, "Null"), Student.class);
                    SharedPreferences sp = getActivity().getSharedPreferences(TIME_TABLE_PREFERENCES, Context.MODE_PRIVATE);
                    if (sp.getBoolean("DownloadJson", false)) {
                        String timeTable = sp.getString(LESSONS_PREFERENCES, null);
                        t = new Gson().fromJson(timeTable, TimeTable.class);
                    } else {
                        String timeTable = SUAppServer.getTimeTable(student.getToken());
                        ServerResponse response = new Gson().fromJson(timeTable, ServerResponse.class);
                        if (response.getResponse() == null) {
                            t = new Gson().fromJson(timeTable, TimeTable.class);
                            sсhedule = getActivity().getSharedPreferences(TIME_TABLE_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sсhedule.edit();
                            editor.putString(LESSONS_PREFERENCES, timeTable);
                            editor.putBoolean("DownloadJson", true);
                            editor.apply();
                        } else {
                            h1.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(rootView.getContext(), "Ошибка при загрузке расписания", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    System.err.println(t.getTime().get(0));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                h.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (t != null) {
                            //timeTextView.setText(t.getTime().get(0));
                            fill_table();
                        }
                    }
                });
            }
        }).start();
        return rootView;
    }

    public void color_text(TextView textView, Lesson lesson) {
        String color = lesson.getType();

        if(color.contains("Лек")){
            textView.setBackgroundColor(Color.rgb(61, 168, 134));
        }
        else if(color.contains("Прак")){
            textView.setBackgroundColor(Color.rgb(170, 98, 90));
        }
        else{
            textView.setBackgroundColor(Color.rgb(78, 158, 182));
        }
    }

    public synchronized void fill_table(){
        row1.removeAllViews();
        timeTableLayout.removeAllViews();
        timeTableLayout.addView(row1);
        final TextView parity = new TextView(row1.getContext());
        parity.setPadding(0,0,20,0);
        if (parity_num_week == 0) {
            parity.setText("1-я Неделя");
        } else {
            parity.setText("2-я Неделя");
        }
        parity.setTextSize(20);
        row1.addView(parity);
        parity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parity_num_week == 0) {
                    parity.setText("1-я Неделя");
                    parity_num_week = 1;
                    fill_table();
                } else {
                    parity.setText("2-я Неделя");
                    parity_num_week = 0;
                    fill_table();
                }
            }
        });
        for (int i = 0; i < Days.length; i++) {
            TextView dayName = new TextView(row1.getContext());
            dayName.setText(Days[i]);
            dayName.setTextSize(20);
            dayName.setPadding(0,5,20,15);
            row1.addView(dayName);
        }
        //timeTableLayout.addView(row1);

        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 6; i++) {
                TableRow newRow = new TableRow(timeTableLayout.getContext());
                TextView time = new TextView(newRow.getContext());
                time.setText(" " + t.getTime().get(i) + "  ");
                newRow.addView(time);
                for (int j = 0; j < 6; j++) {

                    final Couple couple = t.getWeeks().get(k).getDays().get(j).getCouple().get(i);
                    TextView coupleName = new TextView(newRow.getContext());
                    coupleName.setTextSize(20);



                    coupleName.setPadding(10,10,20,10);

                    if (couple != null) {
                        coupleName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                builder.setItems(aboutCouple(couple), new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                builder.create().show();
                            }
                        });
                        String s = "";
                        if (couple.getBySubgroups()) {
                            try {
                                if(couple.getFirstSubgroup() != null){
                                    s += couple.getFirstSubgroup().getDiscipline();
                                    s = toSimpleName(s);
                                }else{
                                    s += "-";
                                }
                                s += "/";
                                if(couple.getSecondSubgroup() != null){
                                    s += toSimpleName(couple.getSecondSubgroup().getDiscipline());
                                }else {
                                    s += "-";
                                }
                                color_text(coupleName, couple.getFirstSubgroup());
                                coupleName.setText(s);
                            } catch (IllegalObjectStateException ignored) {
                            }

                        } else {
                            try {
                                s = couple.getAllGroup().getDiscipline();
                                s = toSimpleName(s);
                                coupleName.setText(s);
                                color_text(coupleName, couple.getAllGroup());
                            } catch (IllegalObjectStateException ignored) {
                            }
                        }
                    }
                    newRow.addView(coupleName);
                }
                if(i == 5){
                    newRow.setPadding(0,0,0,20);
                }
                timeTableLayout.addView(newRow);
            }
        }
    }

    private String[] aboutCouple(Couple couple){
        try {
            String answer = "";
            if(!couple.getBySubgroups()){
                Lesson lesson = couple.getAllGroup();
                answer += "Предмет: " + lesson.getDiscipline() + '\n';
                answer += "Вид: " + lesson.getType() + '\n';
                answer += "Преподаватель: " + lesson.getTeacher().getFullName() + '\n';
                String info = lesson.getSubgroupsInfo();
                if (info != null && !info.isEmpty()){
                    answer += info;
                }
            }else{
                if(couple.getFirstSubgroup() != null){
                    Lesson lesson = couple.getFirstSubgroup();
                    answer += "Предмет: " + lesson.getDiscipline() + '\n';
                    answer += "Вид: " + lesson.getType() + '\n';
                    answer += "Преподаватель: " + lesson.getTeacher().getFullName() + '\n';
                    String info = lesson.getSubgroupsInfo();
                    if (info != null && !info.isEmpty()){
                        answer += info;
                    }
                }
                if(couple.getSecondSubgroup() != null){
                    Lesson lesson = couple.getSecondSubgroup();
                    answer += "Предмет: " + lesson.getDiscipline() + '\n';
                    answer += "Вид: " + lesson.getType() + '\n';
                    answer += "Преподаватель: " + lesson.getTeacher().getFullName() + '\n';
                    String info = lesson.getSubgroupsInfo();
                    if (info != null && !info.isEmpty()){
                        answer += info;
                    }
                }
            }
            return answer.split("\n");
        }catch(Exception e){
            String[] t = {"Не удалось загрузить информацию"};
            return t;
        }
    }

}


