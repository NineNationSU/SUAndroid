package com.android.suapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.suapp.suapp.sdk.RegistrationUtility;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.responses.ErrorResponse;
import com.android.suapp.suapp.server.responses.OKResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class SignUpActivity extends AppCompatActivity {


    private EditText login;
    private EditText password;
    private SegmentedButtonGroup sex;
    private EditText phoneNumber;
    private SegmentedButtonGroup proffecion;
    private Button male;
    private Button female;
    private Button signUpButton;
    private Toolbar toolbar;
    private Student student;
    private TextView pickedDate;
    private String token;

    private String Sex = "Жен";
    private int Proff = 1;

    int myYear = 2011;
    int myMonth = 02;
    int myDay = 03;

    Calendar date=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pickedDate = findViewById(R.id.picked_date);
        pickedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
        setInitialDateTime();

        signUpButton = findViewById(R.id.button_sign_up);
        login = findViewById(R.id.sign_up_login);
        password = findViewById(R.id.sign_up_pass);
        sex = findViewById(R.id.segmentButtonGroup);
        phoneNumber = findViewById(R.id.sign_up_phone);
        proffecion = findViewById(R.id.segmentButtonGroup2);
        proffecion.setInterpolatorSelector(new LinearInterpolator());
        female = findViewById(R.id.sex_female);
        male = findViewById(R.id.sex_male);

        sex.setOnClickedButtonPosition(new SegmentedButtonGroup.OnClickedButtonPosition() {
            @Override
            public void onClickedButtonPosition(int position) {
                if(position == 0){
                    Sex = "Муж";
                }
                else{
                    Sex = "Жен";
                }
            }
        });

        proffecion.setOnClickedButtonPosition(new SegmentedButtonGroup.OnClickedButtonPosition() {
            @Override
            public void onClickedButtonPosition(int position) {
                Proff = position;
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void SignUp() {

        student = new Student();
        //token = login.getText().toString()+password.getText().toString();
        //token = DigestUtils.md5Hex(token);
        //System.out.print(token);
        student.setLogin(login.getText().toString());
        student.setPassword(password.getText().toString());
        student.setPhoneNumber(phoneNumber.getText().toString());




        myYear = date.get(Calendar.YEAR);
        myMonth = date.get(Calendar.MONTH)+1;
        myDay = date.get(Calendar.DAY_OF_MONTH);
        String mstr = "";
        String dstr = "";
        if(myMonth < 10){
            mstr = "0";
        }
        if(myDay < 10){
            dstr = "0";
        }
        String birthday = myYear + "-" + mstr + myMonth + "-" + dstr + myDay;
        student.setBirthday(birthday);
        student.setGender(Sex);

        if(Proff == 1){
            student.setGroupPresident(true);
            student.setGroupManager(false);
            student.setGroupProforg(false);
        }
        else if(Proff == 2){
            student.setGroupProforg(true);
            student.setGroupManager(false);
            student.setGroupPresident(false);
        }
        else if(Proff == 3){
            student.setGroupManager(true);
            student.setGroupPresident(false);
            student.setGroupProforg(false);
        }
        else{
            student.setGroupPresident(false);
            student.setGroupManager(false);
            student.setGroupProforg(false);
        }

        final Handler h = new Handler();


        new Thread(new Runnable() {
            @Override
            public void run() {
                String answer = null;
                OKResponse ok;
                ErrorResponse error;
                final String[] message = {"Что-то пошло не так..."};
                try {
                    answer = RegistrationUtility.registerANewStudent(student);
                } catch (URISyntaxException | IOException e) {
                    System.out.println("err");
                    message[0] = "косяк запроса";
                    // тут формируем сообщение пользователю
                }
                if (answer != null){
                    try{
                        ok = new Gson().fromJson(answer, OKResponse.class);
                        message[0] = "ok";
                        if (answer.contains("error"))
                            throw new Exception();
                        // тут формируем сообщение пользователю
                    }catch (Exception e){
                        try{
                            error = new Gson().fromJson(answer, ErrorResponse.class);
                            message[0] = error.getMessage();

                            // тут формируем сообщение пользователю
                        }catch (Exception ex){
                            message[0] = "Fatal Error";
                            System.out.println("err");

                            // тут формируем сообщение пользователю
                        }
                    }
                }
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!message[0].equals("")){
                            Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show(); // тут передаем сообщение пользователю
                        }
                        else{
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                }, 300);
            }
        }).start();




    }

    public void setDate(View v) {
        new DatePickerDialog(SignUpActivity.this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateTime() {

        pickedDate.setText(DateUtils.formatDateTime(this,
                date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}