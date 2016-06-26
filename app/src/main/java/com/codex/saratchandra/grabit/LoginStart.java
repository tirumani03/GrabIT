package com.codex.saratchandra.grabit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by SaratChandra on 6/17/2016.
 */
public class LoginStart extends Activity {
    Button login,register;
    TextView skip1;
    private AlphaAnimation buttonclick=new AlphaAnimation(1F,0.5F);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginstart);
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonclick);
                Intent login1=new Intent(getApplicationContext(),Login.class);
                startActivity(login1);
            }
        });
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonclick);
                Intent register=new Intent(getApplicationContext(),Register.class);
                startActivity(register);
            }
        });
        skip1=(TextView)findViewById(R.id.skip1);
        skip1.setPaintFlags(skip1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        skip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonclick);
                Intent skip1intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(skip1intent);
            }
        });
    }
}
