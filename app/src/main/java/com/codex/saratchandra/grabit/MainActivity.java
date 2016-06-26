package com.codex.saratchandra.grabit;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.Formatter;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    AlphaAnimation mainanimation= new AlphaAnimation(1F,0.1F);
    String ip,userAgent;
    public ViewPager mPager;
    public SlidingTabLayout mTabs;
    String searchquery,address;
    GPSTracker gps;
    double latitude,longitude;
    String countryName="null",stateName="null";
    String jobtype,radius,sort,location,city;
    public void getgps(){
        gps = new GPSTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
            Toast.makeText(this, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            gps.showSettingsAlert();}

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        List<Address> addresses = null;
        if(geocoder.isPresent()) {
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                stateName = addresses.get(0).getAddressLine(2);
                stateName=stateName.substring(0,stateName.lastIndexOf(" "));
                countryName = addresses.get(0).getAddressLine(3);
                Toast.makeText(this, "Your Location is - \nLat: " + stateName + "\nLong: " + countryName, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  countryName="null";stateName="null";
        Intent myIntent=getIntent();
        if(Intent.ACTION_SEARCH.equals(myIntent.getAction())){
            searchquery=myIntent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(searchquery, null);
            Toast.makeText(this,searchquery,Toast.LENGTH_SHORT).show();

        }
        try {
            jobtype =this.getIntent().getExtras().getString("jobtype");
        //    Toast.makeText(getApplicationContext(),jobtype,Toast.LENGTH_SHORT).show();

            stateName = getIntent().getExtras().getString("state");
            //stateName.concat(" dummy");
          //  Toast.makeText(getApplicationContext(),stateName,Toast.LENGTH_SHORT).show();
            countryName = getIntent().getExtras().getString("country");
       //     Toast.makeText(getApplicationContext(),countryName,Toast.LENGTH_SHORT).show();
            radius = getIntent().getExtras().getString("radius");
         //   Toast.makeText(getApplicationContext(),radius,Toast.LENGTH_SHORT).show();
            city = getIntent().getExtras().getString("city");
           // Toast.makeText(getApplicationContext(),city,Toast.LENGTH_SHORT).show();
            sort = getIntent().getExtras().getString("sortby");
            //Toast.makeText(getApplicationContext(),sort,Toast.LENGTH_SHORT).show();
            searchquery = getIntent().getExtras().getString("query");
          //  Toast.makeText(getApplicationContext(),searchquery,Toast.LENGTH_SHORT).show();

            location = getIntent().getExtras().getString("location");
           // Toast.makeText(getApplicationContext(),location,Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            e.printStackTrace();

        }


        if("null".equalsIgnoreCase(stateName) && "null".equalsIgnoreCase(countryName)){
            getgps();
        }

        mPager=(ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs=(SlidingTabLayout)findViewById(R.id.defaulttabs);
        mTabs.setCustomTabView(R.layout.customtabview,R.id.tabtext);
        mTabs.setDistributeEvenly(true);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.white));
        mTabs.setViewPager(mPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final SearchView searchView=(SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager=(SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
       return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

           switch(item.getItemId()){
            case R.id.setmain:
                        mPager.setCurrentItem(2);
                break;

               case R.id.filter:
                   Intent filterintent=new Intent(getApplicationContext(),Filters.class);
                   filterintent.putExtra("state",stateName);
                   filterintent.putExtra("country",countryName);
                   filterintent.putExtra("query",searchquery);
                   startActivity(filterintent);
                   break;

        }

       return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getCityName(){return city;}
    public String getLocationName(){return location;}
    public String getJobtype(){return jobtype;}
    public String getSearchquery(){return searchquery;}
    public String getRadius(){return radius;}
    public String getStateName(){ return stateName;}
    public String getCountryName(){ return countryName;}
    public String getSort(){return sort;}





    class MyPagerAdapter extends FragmentPagerAdapter{
          int tabimage[]={R.drawable.ic_home_white_24dp,R.drawable.ic_save_white_24dp,R.drawable.ic_settings_white_24dp,R.drawable.ic_account_circle_white_24dp};
         String[] tabstext;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabstext=getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            FragmentHome fdefault=new FragmentHome().getInstance(position);
            switch (position){
                case 0:
                    FragmentHome fhome=new FragmentHome().getInstance(position);

                    return fhome;
                case 1:
                    FragmentSaved fsaved=new FragmentSaved().getInstance(position);
                    return fsaved;
                case 2:
                    FragmentSettings fsettings=new FragmentSettings().getInstance(position);
                    return fsettings;
                case 3:
                    FragmentProfile fprofile=new FragmentProfile().getInstance(position);
                    return fprofile;
            }
                  return fdefault;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable draw=getResources().getDrawable(tabimage[position]);
            draw.setBounds(0,0,86,86);
            ImageSpan imageSpan=new ImageSpan(draw);
            SpannableString spannableString=new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
   /* public static class MyFragment extends Fragment{
        public  MyFragment getInstance(int position){
            MyFragment myFragment=new MyFragment();
            Bundle args=new Bundle();
            args.putInt("position",position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout=inflater.inflate(R.layout.mainfragment,container,false);
           // new FetchJob().execute();

            return layout;
        }
    }
*/
     /* class FetchJob extends AsyncTask<Void,Void,Void> {

    TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab Home");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Home");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Saved");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Saved");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Settings");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Settings");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Profile");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Profile");
        host.addTab(spec);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jobslist= new ArrayList<HashMap<String,String>>();
            pDialog=new ProgressDialog(MainActivity.this);
            pDialog.setTitle("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> paramgetjob = new ArrayList<NameValuePair>();
            paramgetjob.add(new BasicNameValuePair("v","2"));
            paramgetjob.add(new BasicNameValuePair("format","json"));
            paramgetjob.add(new BasicNameValuePair("publisher","2048240434418677"));
            paramgetjob.add(new BasicNameValuePair("co","in"));
            paramgetjob.add(new BasicNameValuePair("l","Delhi"));
            paramgetjob.add(new BasicNameValuePair("q","java"));
            paramgetjob.add(new BasicNameValuePair("userip","192.168.43.42"));
            paramgetjob.add(new BasicNameValuePair("useragent","Mozilla/%2F4.0"));
            JSONObject jjobObject=jparser.makeHttpRequest(URL,"GET",paramgetjob);
            try {

                //Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                jobs=jjobObject.getJSONArray("results");
                for(int i=0;i<jobs.length();i++) {
                    JSONObject job = jobs.getJSONObject(i);
                    String jTitle = job.getString("jobtitle");
                    String jCompany = job.getString("company");
                    String jWebsite = job.getString("url");
                    String jage=job.getString("formattedRelativeTime");
                    String jdescription=job.getString("snippet");
                    HashMap<String, String> jobmap = new HashMap<String, String>();
                    jobmap.put(TAG_JTITLE, jTitle);
                    jobmap.put(TAG_JCOMPANY, jCompany);
                    jobmap.put(TAG_JWEBSITE,jWebsite);
                    jobmap.put(TAG_JDESCRIPTION,jdescription);
                    jobmap.put(TAG_JAGE,jage);
                    jobslist.add(jobmap);
                    job_data=new jobDetails[jobs.length()];

                }
                for(int j=0;j<jobs.length();j++){
                     job_data[j] = new jobDetails(R.drawable.companylogo, jobslist.get(j).get(TAG_JTITLE),jobslist.get(j).get(TAG_JCOMPANY),jobslist.get(j).get(TAG_JAGE),jobslist.get(j).get(TAG_JDESCRIPTION));


                  data_id[j]  =jobslist.get(j).get(TAG_JTITLE).toString();
                    data_name[j]=jobslist.get(j).get(TAG_JCOMPANY).toString();
                    data_website[j]=jobslist.get(j).get(TAG_JWEBSITE).toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //           Toast.makeText(getApplicationContext(),"not added",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pDialog.isShowing()){
                pDialog.cancel();
            }
            String[] data={TAG_JID,TAG_JNAME,TAG_JWEBSITE};
            Integer[] image={R.drawable.country_ac,R.drawable.country_ad,R.drawable.country_ae};
            CustomListAdapter adapter = new
                    CustomListAdapter(MainActivity.this,data,image);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

                }
            });

            CustomListAdapter customListAdapter=new CustomListAdapter(MainActivity.this,R.layout.main_list,job_data);
            list.setAdapter(customListAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(),jobslist.get(position).get(TAG_JWEBSITE).toString(),Toast.LENGTH_SHORT).show();
                    Intent jobdetailsintent=new Intent(getApplicationContext(),Describe.class);
                    jobdetailsintent.putExtra("wurl",jobslist.get(position).get(TAG_JWEBSITE).toString());
                    startActivity(jobdetailsintent);
                }
            });
        }

    }
*/
}
