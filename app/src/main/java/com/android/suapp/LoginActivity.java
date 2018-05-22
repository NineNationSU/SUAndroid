package com.android.suapp;

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

import com.android.suapp.suapp.sdk.SUAppServer;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.responses.ServerResponse;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGN_UP = 0;
    public static SharedPreferences settings;
    private Student student;
    private int profession;
    public static final String APP_PREFERENCES = "log_pass_login";
    public static final String APP_PREFERENCES_LOG = "Login";
    public static final String APP_PREFERENCES_PASS = "Password";
    public static final String APP_PREFERENCES_STUDENT_DATA = "Data";
    public static final String APP_PROFESSION = "profession";


    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signUpLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _signUpLink = findViewById(R.id.link_signup);

        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SignUp activity
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
                final ServerResponse error;
                try {
                    answer = SUAppServer.authorize(email, password);
                    student =new Gson().fromJson(answer, Student.class);
                    if(student.isGroupManager()){
                        profession = 3;
                    }
                    else if(student.isGroupPresident()){
                        profession = 1;
                    }
                    else if(student.isGroupProforg()){
                        profession = 2;
                    }
                    else{
                        profession = 0;
                    }
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(APP_PREFERENCES_LOG, email);
                    editor.putString(APP_PREFERENCES_PASS, password);
                    editor.putString(APP_PREFERENCES_STUDENT_DATA, answer);
                    editor.putInt(APP_PROFESSION, profession);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Вход выполнен", Toast.LENGTH_SHORT).show();
                    onLoginSuccess();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    try{
                        error = new Gson().fromJson(answer, ServerResponse.class);
                        final String ERROR_MESSAGE = error.getErrorType();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch (Exception ex){
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Сервер не отвечает", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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