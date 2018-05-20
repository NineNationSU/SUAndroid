package com.android.suapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.suapp.suapp.sdk.LoginUtility;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.responses.ErrorResponse;
import com.android.suapp.suapp.server.responses.OKResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static SharedPreferences settings;
    private Student student;
    private int Proffesion;
    public static final String APP_PREFERENCES = "log_pass_login";
    public static final String APP_PREFERENCES_LOG = "Login";
    public static final String APP_PREFERENCES_PASS = "Password";
    public static final String APP_PREFERENCES_STUDENT_DATA = "Data";
    public static String APP_PROFFESION = "Proffesion";


    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }



        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        final Handler h = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String answer = null;
                OKResponse ok;
                ErrorResponse error;
                final String[] message = {"Что-то пошло не так..."};
                try {
                    answer = LoginUtility.loginANewStudent(email, password);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                    System.out.println("err");
                    message[0] = "косяк запроса";
                    // тут формируем сообщение пользователю
                }
                if (answer != null){
                        message[0] = "ok";
                            final String ANSWER = answer;
                            h.post(new Runnable() {
                                @Override
                                public void run() {


                                    if (!ANSWER.contains("error")) {
                                        student =new Gson().fromJson(ANSWER, Student.class);
                                        if(student.isGroupManager()){
                                            Proffesion = 3;
                                        }
                                        else if(student.isGroupPresident()){
                                            Proffesion = 1;
                                        }
                                        else if(student.isGroupProforg()){
                                            Proffesion = 2;
                                        }
                                        else{
                                            Proffesion = 0;
                                        }
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString(APP_PREFERENCES_LOG, email);
                                        editor.putString(APP_PREFERENCES_PASS, password);
                                        editor.putString(APP_PREFERENCES_STUDENT_DATA, ANSWER);
                                        editor.putInt(APP_PROFFESION, Proffesion);
                                        editor.apply();
                                        Toast.makeText(getApplicationContext(), "Вход выполнен", Toast.LENGTH_SHORT).show();
                                        onLoginSuccess();
                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Неправильные данные!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                                Toast.makeText(getApplicationContext(), "Сервер не отвечает", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        }).start();
    }



    @Override
    public void onBackPressed() {
        // Disable going back to the MenuActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        settings.edit().putBoolean("FirstRun", false).apply();
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}