package com.codex.saratchandra.grabit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by SaratChandra on 6/15/2016.
 */
public class Splash extends Activity {
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("cred", 0);
        Intent myIntent;
        if (pref.getString("user","").contentEquals(""))
        {
            myIntent = new Intent(Splash.this, Login.class);
            startActivity(myIntent);
        }
        else{
            myIntent = new Intent(Splash.this, MainActivity.class);
            startActivity(myIntent);
        }
        finish();

    }
}
