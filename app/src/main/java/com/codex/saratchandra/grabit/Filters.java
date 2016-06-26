package com.codex.saratchandra.grabit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by SaratChandra on 6/22/2016.
 */
public class Filters extends AppCompatActivity {
    ArrayAdapter<String>countryadapter,radiusadapter,jobtypesadapter,sortbyadapter;
    String[]countrynames;
    String[]jobtypes;
    String[]radii;
    String[]sort;
    String setcountry,setjobtype,setradius,setsort,setlocation,setstate,setcity,setquery;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_activity);
        countrynames=this.getResources().getStringArray(R.array.countries_array);
        jobtypes=this.getResources().getStringArray(R.array.JobTypesArray);
        radii=this.getResources().getStringArray(R.array.RadiusArray);
        setstate=getIntent().getExtras().getString("state");
        Toast.makeText(Filters.this,setstate, Toast.LENGTH_SHORT).show();
        setcountry=getIntent().getExtras().getString("country");
        Toast.makeText(getApplicationContext(),setcountry, Toast.LENGTH_SHORT).show();
        setquery=getIntent().getExtras().getString("query");
        Toast.makeText(getApplicationContext(),setquery, Toast.LENGTH_SHORT).show();
        sort=this.getResources().getStringArray(R.array.SortByArray);
        Spinner country=(Spinner)findViewById(R.id.countryname);

        countryadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,countrynames);
        countryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /*country.setSelection();*/
        country.setAdapter(countryadapter);
        int spinnerPosition = countryadapter.getPosition(setcountry);
        country.setSelection(spinnerPosition);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  setcountry=countrynames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        Spinner jobtype=(Spinner)findViewById(R.id.jobtype);
        jobtypesadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,jobtypes);
        jobtypesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobtype.setAdapter(jobtypesadapter);

        jobtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setjobtype=jobtypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setjobtype="full time";
            }
        });
        Spinner radius=(Spinner)findViewById(R.id.radius);
        radiusadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,radii);
        radiusadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radius.setAdapter(radiusadapter);
        radius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setradius=radii[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setradius="25";
            }
        });
        Spinner sortyby=(Spinner)findViewById(R.id.sortby);
        sortbyadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,sort){

        };
        sortbyadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortyby.setAdapter(sortbyadapter);
        sortyby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setsort=sort[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setsort="Date";
            }
        });
        final EditText getlocation=(EditText) findViewById(R.id.location);

        final EditText getcity=(EditText)findViewById(R.id.city);

        final EditText getstate=(EditText) findViewById(R.id.statename);

        final EditText getquery=(EditText) findViewById(R.id.query);
               getquery.setText(setquery);

        Button done=(Button)findViewById(R.id.done) ;

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setlocation= getlocation.getText().toString();
                setstate= getstate.getText().toString();
                if(setstate.equals(""))setstate=getIntent().getExtras().getString("state");
                Toast.makeText(getApplicationContext(),setstate, Toast.LENGTH_SHORT).show();
                setcity= getcity.getText().toString();
                setquery= getquery.getText().toString();
                if(setquery.equals(""))setquery=getIntent().getExtras().getString("query");
                Toast.makeText(getApplicationContext(),setquery, Toast.LENGTH_SHORT).show();
                Intent setsearchintent=new Intent(getApplicationContext(),MainActivity.class);
                setsearchintent.putExtra("country",setcountry);
                setsearchintent.putExtra("state",setstate);
                setsearchintent.putExtra("city",setcity);
                setsearchintent.putExtra("location",setlocation);
                setsearchintent.putExtra("jobtype",setjobtype);
                setsearchintent.putExtra("radius",setradius);
                setsearchintent.putExtra("sortby",setsort);
                setsearchintent.putExtra("query",setquery);
                 startActivity(setsearchintent);
                finish();
            }
        });

    }
}
