package com.android.suapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.suapp.suapp.sdk.SUAppServer;
import com.android.suapp.suapp.server.database.objects.Student;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.android.suapp.LoginActivity.APP_PREFERENCES;

import static com.android.suapp.LoginActivity.APP_PREFERENCES_STUDENT_DATA;
import static com.android.suapp.LoginActivity.settings;
import static com.android.suapp.TableFragment.LESSONS_PREFERENCES;
import static com.android.suapp.TableFragment.TIME_TABLE_PREFERENCES;
import static com.android.suapp.TableFragment.toSimpleName;
import static com.android.suapp.suapp.sdk.SUAppServer.loadClassmates;

/**
 * Created by fokin on 10.04.2018.
 */

public class UserFragment extends Fragment {

    private ImageButton buttonSettings;
    private TextView textName;
    private Button buttonClassMates;
    private TextView textGroup;
    private TextView textPhoneNumber;
    private TextView textImage;
    private TextView textMoney;
    private SharedPreferences sp;
    private SharedPreferences dataStudent;
    private SharedPreferences lessons;
    private Student student;
    private String salary;
    private SharedPreferences classMates;
    private String[] classmates_array;
    public static final String listOfClassMates = "List";

    @SuppressLint("StaticFieldLeak")
    private static UserFragment instance;
    public static UserFragment newInstance() {
        if (instance == null){
            instance = new UserFragment();
        }
        return instance;
    }

    public UserFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        textName = rootView.findViewById(R.id.sign_up_name);
        textGroup = rootView.findViewById(R.id.user_student_group);
        textPhoneNumber = rootView.findViewById(R.id.user_phone_number);
        textImage = rootView.findViewById(R.id.text_view_user_image);
        textMoney = rootView.findViewById(R.id.text_money);

        final Handler h = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    dataStudent = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    String studentJSONString = dataStudent.getString(APP_PREFERENCES_STUDENT_DATA, "null");
                    if(studentJSONString.equals("null")){
                        throw new Exception();
                    }
                    student =new Gson().fromJson(studentJSONString, Student.class);
                    String salaryString = dataStudent.getString("salary", "null");
                    if(salaryString.equals("null")) {
                        salary = SUAppServer.loadStudentSalary(student.getToken());
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit();
                        editor.putString("salary", salary);
                        editor.apply();
                    }else{
                        salary = salaryString;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                h.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        try {
                            String s;
                            textName.setText(student.getLastName() + " " + student.getFirstName());
                            textGroup.setText(student.getGroupNumber());
                            textPhoneNumber.setText(student.getPhoneNumber());
                            s = student.getFirstName() + " " + student.getLastName();
                            textImage.setText(toSimpleName(s));
                            System.out.println(salary);
                            if (salary != null) {
                                textMoney.setText(salary);
                            } else {
                                Toast.makeText(getContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception ignored){}


                    }
                });
            }
        }).start();


        buttonSettings = rootView.findViewById(R.id.user_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lessons = getActivity().getSharedPreferences(TIME_TABLE_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = lessons.edit();
                editor1.clear();
                sp = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
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
        });

        buttonClassMates = rootView.findViewById(R.id.list_classmates);
        buttonClassMates.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final Handler h1 = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        classMates = getActivity().getSharedPreferences(listOfClassMates, Context.MODE_PRIVATE);

                    }
                }).start();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String s;
                            s = loadClassmates(student.getToken());
                            h1.post(new Runnable() {
                                @Override
                                public void run() {
                                    classmates_array = s.split("\n");
                                    builder.setTitle(R.string.my_classMates).setItems(classmates_array, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String phone = classmates_array[i].split(" ")[2];
                                            dialogContactPhone(phone);
                                        }

                                        private void dialogContactPhone(String phoneNum) {
                                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNum, null)));
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        } catch (IOException e) {
                            h1.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Не удалось загрузить список одногруппников", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                }).start();




            }
        });

        return rootView;
    }
}