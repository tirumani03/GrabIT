package com.codex.saratchandra.grabit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SaratChandra on 6/17/2016.
 */
public class Login extends AppCompatActivity {
    EditText username, pass;
    TextView access;
    SharedPreferences pref;
    UsersDatabaseHandler udb;
    CheckBox remember;
    String remus, rempa, remstatus;
    int prefill = 0;
    TextView skip2, forgotpassword;
    Button loginfinal, sigunup2;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
    public  static final String LOG_TAG="NO INTERNET";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("GrabIt-Login");
        username = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        access=(TextView)findViewById(R.id.access);
        remember=(CheckBox)findViewById(R.id.remember);
        udb = new UsersDatabaseHandler(this);
        pref = getApplicationContext().getSharedPreferences("cred", 0);
       loginfinal = (Button) findViewById(R.id.finallogin);
       loginfinal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    if (username.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                        access.setText("Username or Password field is empty");
                        username.setText("");
                        pass.setText("");

                    } else if (udb.userLogin(username.getText().toString(), pass.getText().toString()) != 0) {
                        Toast.makeText(getApplicationContext(), "welcome " + username.getText().toString(), Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("user", username.getText().toString());
                        edit.putString("pass", pass.getText().toString());
                        edit.commit();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    } else if (udb.userLogin(username.getText().toString(), pass.getText().toString()) == 0)
                        access.setText("No user found");
                    else
                        access.setText("Something is wrong");
                }
            });
            skip2 = (TextView) findViewById(R.id.skip2);
            skip2.setPaintFlags(skip2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            skip2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent skip2intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(skip2intent);
                    finish();
                }
            });
            forgotpassword = (TextView) findViewById(R.id.forgotpassword);
            forgotpassword.setPaintFlags(forgotpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent forgotpasswordintent = new Intent(getApplicationContext(), Forgot.class);
                    startActivity(forgotpasswordintent);
                    finish();
                }
            });
            sigunup2 = (Button) findViewById(R.id.signup2);
            sigunup2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent signup2intent = new Intent(getApplicationContext(), Register.class);
                    startActivity(signup2intent);
                    finish();
                }
            });
        }
    /*class Retreive extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {

                    try {
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                        if(urlc.getResponseCode() == 200) return "null";
                       else return "not null";
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error checking internet connection", e);
                    }
                return "not null";
            }
} */

    }

