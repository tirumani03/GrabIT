package com.codex.saratchandra.grabit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by SaratChandra on 6/17/2016.
 */
public class Register extends AppCompatActivity {
    Spinner countryspinner,educationspinner,interestspinner;
    ArrayAdapter<String> countryadapter,educationadapter,interestadapter;
    TextView login2,signupaccess;
    EditText newuser,newpass,newreenterpass,first,second,email,phone,education,interests;
    String code,number,interest,educ;
    Button signin,skip3;
    UsersDatabaseHandler signinudb;
    private AlphaAnimation buttonclick3=new AlphaAnimation(1F,0.5F);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setTitle("Sign Up");
        setRefernces();
        signinudb=new UsersDatabaseHandler(this);

        final String[] countryCodes = this.getResources().getStringArray(R.array.CountryCodes);
        countryspinner=(Spinner)findViewById(R.id.countrycode);
        countryadapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_layout,countryCodes);
        countryspinner.setAdapter(countryadapter);
        countryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    code=countryCodes[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final String[] educationlist = this.getResources().getStringArray(R.array.EducationArray);
        educationspinner=(Spinner)findViewById(R.id.education);
        educationadapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_layout,educationlist);
        educationspinner.setAdapter(educationadapter);
        educationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
                educ=educationlist[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final String[] interestarray = this.getResources().getStringArray(R.array.InterestArray);
        interestspinner=(Spinner)findViewById(R.id.interests);
        interestadapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_layout,interestarray);
        interestspinner.setAdapter(interestadapter);
        interestspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           interest=interestarray[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        signin=(Button)findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonclick3);
                try{
                    if(!isEmailValid(email.getText().toString())) signupaccess.setText("email not valid");
                    else if(!isMobileValid(phone.getText().toString())) signupaccess.setText("phone number not valid");
                   else if(newpass.getText().toString().equals(newreenterpass.getText().toString())){
                    signinudb.addCredential(new User(newuser.getText().toString(),newpass.getText().toString(),first.getText().toString(),second.getText().toString(),email.getText().toString(),phone.getText().toString(),educ,interest));
                    Intent signinintent=new Intent(getApplicationContext(),Login.class);
                    startActivity(signinintent);
                        finish();
                    }
                    else
                        signupaccess.setText("Passwords doesn't match");
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_SHORT).show();
                }
            }
        });
        skip3=(Button)findViewById(R.id.skip3);
        skip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonclick3);
                Intent skip3intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(skip3intent);
            }
        });
        login2=(TextView)findViewById(R.id.login2);
        login2.setPaintFlags(login2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonclick3);
                Intent login2intent=new Intent(getApplicationContext(),Login.class);
                startActivity(login2intent);
            }
        });

    }

    public void gospinnercontrycode(View view) {
       countryspinner.performClick();
    }

    public void gospinnereducation(View view) {
        educationspinner.performClick();
    }

    public void gospinnerinterest(View view) {
        interestspinner.performClick();
    }
    public void setRefernces(){
        signupaccess=(TextView)findViewById(R.id.sigunupaccess);
        newpass=(EditText)findViewById(R.id.newpass);
        newreenterpass=(EditText)findViewById(R.id.newrenterpass);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        newuser=(EditText)findViewById(R.id.newuser);
        first=(EditText)findViewById(R.id.first);
        second=(EditText)findViewById(R.id.last);

    }
    private boolean isMobileValid(String mobile) {
        if(mobile.length()!=10)
            return false;
        for(int i = 1; i<mobile.length(); i++){
            if (mobile.charAt(i)<'0' || mobile.charAt(i)>'9')
                return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".com");
    }

}
