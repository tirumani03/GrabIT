package com.codex.saratchandra.grabit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.text.format.Formatter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by SaratChandra on 6/20/2016.
 */
public class FragmentHome extends Fragment {
    AlphaAnimation loadmoreanimation=new AlphaAnimation(1F,0.6F);
    private static final String URL="http://api.indeed.com/ads/apisearch";
    ProgressDialog pDialog;
    ArrayList<HashMap<String,String>>jobslist;
    ListView listmain;
    HashMap<String, String> jobmap;
    JSONArray jobs=null;
    int totalresults=0;
    private static final String TAG_JTITLE="id";
    private static final String TAG_JCOMPANY="company";
    private static final String TAG_JWEBSITE="website";
    private static final String TAG_JAGE="age";
    private static final String TAG_JDESCRIPTION="description";
    private static final String TAG_CITY="city";
    String ip,userAgent;
    JSONArray savedresults;
    JSONObject jsonObject;
    JSONParser jparser= new JSONParser();
    String[] data_id,data_name,data_website;
    jobDetails job_data[],new_job_data[];
    String mainquery;
    GPSTracker gps;
    String connection="present";
    String countryName,stateName;
    int limit=25,start=0;
    CustomListAdapter customListAdapter;
    SavedDatabaseHandler sdb;
    SharedPreferences saved;
    double latitude,longitude;
    String city,radius="25",jobtype="fulltime",sortby="relevance",location;

    public FragmentHome getInstance(int position) {
        FragmentHome homeFragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putInt("position", position);
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.mainfragment, container, false);
        Intent searchIntent=getActivity().getIntent();
        if(Intent.ACTION_SEARCH.equals(searchIntent.getAction())){
            mainquery=searchIntent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(mainquery, null);
            Toast.makeText(getActivity(),mainquery,Toast.LENGTH_SHORT).show();
            MainActivity activity=(MainActivity)getActivity();
            stateName = activity.getStateName();
            countryName=activity.getCountryName();
            city = activity.getCityName();
            radius=activity.getRadius();
            jobtype=activity.getJobtype();
            sortby=activity.getSort();
            location=activity.getLocationName();
            mainquery=activity.getSearchquery();
            Toast.makeText(getActivity(), jobtype + stateName + radius + countryName+sortby+mainquery, Toast.LENGTH_LONG).show();

        }
        listmain=(ListView)layout.findViewById(R.id.listmain);
        gps = new GPSTracker(getActivity());
       /*           // footer.setVisibility(View.GONE);
        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
            Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            gps.showSettingsAlert();}
       */
        MainActivity activity=(MainActivity)getActivity();
        stateName = activity.getStateName();
        countryName=activity.getCountryName();
        city = activity.getCityName();
        radius=activity.getRadius();
        jobtype=activity.getJobtype();
        sortby=activity.getSort();
        location=activity.getLocationName();
        mainquery=activity.getSearchquery();
        Toast.makeText(getActivity(), jobtype + stateName + radius + countryName+sortby+mainquery, Toast.LENGTH_LONG).show();
        bindview();


        return layout;

    }
    public jobDetails[] concat(jobDetails[] a, jobDetails[] b) {
        int aLen = a.length;
        int bLen = b.length;
        jobDetails[] c= new jobDetails[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
    public void bindview()
    {

        new FetchJob(getActivity(),listmain ).execute();
    }
    class FetchJob extends AsyncTask<Void,Void,Void> {
        ListView mListView;
        Activity mContex;

        public  FetchJob(Activity contex, ListView lview)
        {
            this.mListView=lview;
            this.mContex=contex;
        }

        @Override
        protected void onPreExecute() {
            WifiManager wm = (WifiManager) mContex.getSystemService(mContex.WIFI_SERVICE);
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
           // Toast.makeText(mContex,ip,Toast.LENGTH_SHORT).show();
            userAgent = System.getProperty("http.agent");
           // Toast.makeText(mContex,userAgent,Toast.LENGTH_SHORT).show();
            super.onPreExecute();
            jobslist= new ArrayList<HashMap<String,String>>();
            pDialog=new ProgressDialog(mContex);
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
            paramgetjob.add(new BasicNameValuePair("co",countryName));
            paramgetjob.add(new BasicNameValuePair("l",stateName));
            paramgetjob.add(new BasicNameValuePair("jt",jobtype));
            paramgetjob.add(new BasicNameValuePair("sort",sortby));
            paramgetjob.add(new BasicNameValuePair("radius",radius));
            paramgetjob.add(new BasicNameValuePair("q",mainquery));
            paramgetjob.add(new BasicNameValuePair("userip",ip));
            paramgetjob.add(new BasicNameValuePair("useragent",userAgent));
            paramgetjob.add(new BasicNameValuePair("limit",Integer.toString(limit)));
            paramgetjob.add(new BasicNameValuePair("start",Integer.toString(start)));
            JSONObject jjobObject=jparser.makeHttpRequest(URL,"GET",paramgetjob);
            if(jjobObject!=null) {
                try {

                    jobs = jjobObject.getJSONArray("results");
                    if(jobs!=null){
                    totalresults = jjobObject.getInt("totalResults");
                    for (int i = 0; i < jobs.length(); i++) {
                        JSONObject job = jobs.getJSONObject(i);
                        String jTitle = job.getString("jobtitle");
                        String jCompany = job.getString("company");
                        String jWebsite = job.getString("url");
                        String jage = job.getString("formattedRelativeTime");
                        String jdescription = job.getString("snippet");
                        jdescription = Html.fromHtml(jdescription).toString();
                        String jcity = job.getString("city");
                        HashMap<String, String> jobmap = new HashMap<String, String>();
                        jobmap.put(TAG_JTITLE, jTitle);
                        jobmap.put(TAG_JCOMPANY, jCompany);
                        jobmap.put(TAG_JWEBSITE, jWebsite);
                        jobmap.put(TAG_JDESCRIPTION, jdescription);
                        jobmap.put(TAG_JAGE, jage);
                        jobmap.put(TAG_CITY, jcity);
                        jobslist.add(jobmap);
                        job_data = new jobDetails[jobs.length()];
                    }

                    }

                    for (int j = 0; j < jobs.length(); j++) {
                        job_data[j] = new jobDetails(R.drawable.no_image, jobslist.get(j).get(TAG_JTITLE), jobslist.get(j).get(TAG_JCOMPANY), jobslist.get(j).get(TAG_JAGE), jobslist.get(j).get(TAG_JDESCRIPTION), jobslist.get(j).get(TAG_JWEBSITE), jobslist.get(j).get(TAG_CITY));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    connection = "not present";
                }
            }
                return null;

            }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pDialog.isShowing()){
                pDialog.cancel();
            }

// Creating a button - Load More
            TextView Loadmore = new TextView(mContex);
            Loadmore.setGravity( Gravity.CENTER);
            Loadmore.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            if(jobs!=null) {
                if (job_data != null)
                    Loadmore.setText("Loading........");
                else
                    Loadmore.setText("No data found.....");

/*

// Adding button to listview at footer
            listmain.addFooterView(btnLoadMore);
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    start+=limit;
                    // Starting a new async task
                    new FetchNextJob(getActivity(),listmain).execute();

                }
            });
*/
                // Adding Load More button to lisview at bottom
                listmain.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        if (visibleItemCount != 0 && ((firstVisibleItem + visibleItemCount) >= (totalItemCount)) && start <= (totalresults - limit)) {

                            start += limit;
                            new FetchNextJob(getActivity(), listmain).execute();

                        }
                    }


                });
                if (job_data != null)
                    customListAdapter = new CustomListAdapter(mContex, R.layout.main_list, job_data);
                listmain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.startAnimation(loadmoreanimation);
                        Toast.makeText(mContex, job_data[position].url.toString(), Toast.LENGTH_SHORT).show();
                       /* savedresults = new JSONArray();
                        jsonObject = new JSONObject();
                        savedresults.put(job_data);
                        try {
                            jsonObject.put("savedresult", savedresults);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/
                       /* saved  = mContex.getSharedPreferences("saved", 0);
                        SharedPreferences.Editor savededit=saved.edit();
                        savededit.putString("title",job_data[position].title.toString());
                        savededit.putString("company",job_data[position].company.toString());
                        savededit.putString("url",job_data[position].url.toString());
                        savededit.putString("age",job_data[position].age.toString());
                        savededit.putString("description",job_data[position].description.toString());
                        savededit.putString("city",job_data[position].city.toString());
                        savededit.commit();
*/                      sdb=new SavedDatabaseHandler(mContex);
                        sdb.saveData(new SavedJobDetails(job_data[position].title.toString(),job_data[position].company.toString(),job_data[position].description.toString(),job_data[position].city.toString(),job_data[position].age.toString(),job_data[position].url.toString(),R.drawable.no_image));
                        Intent jobdetailsintent = new Intent(mContex, Describe.class);
                        if (job_data[position].url.toString() != null)
                            jobdetailsintent.putExtra("wurl", job_data[position].url.toString());
                        else
                            jobdetailsintent.putExtra("wurl", "http://img.sur.ly/thumbnails/620x343/p/pdftowordconverter.seoethic.it.png");

                        startActivity(jobdetailsintent);
                    }
                });
            }
                else Loadmore.setText("No internet connection");

                listmain.addFooterView(Loadmore);
                listmain.setAdapter(customListAdapter);


        }

    }
    class FetchNextJob extends AsyncTask<Void,Void,Void> {
        ListView mListView;
        Activity mContex;

        public  FetchNextJob(Activity contex, ListView lview)
        {
            this.mListView=lview;
            this.mContex=contex;
        }

        @Override
        protected void onPreExecute() {
            WifiManager wm = (WifiManager) mContex.getSystemService(mContex.WIFI_SERVICE);
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            // Toast.makeText(mContex,ip,Toast.LENGTH_SHORT).show();
            userAgent = System.getProperty("http.agent");
            // Toast.makeText(mContex,userAgent,Toast.LENGTH_SHORT).show();
            super.onPreExecute();
            jobslist= new ArrayList<HashMap<String,String>>();

        }

        @Override
        protected Void doInBackground(Void... params) {


            List<NameValuePair> paramgetjob = new ArrayList<NameValuePair>();
            paramgetjob.add(new BasicNameValuePair("v","2"));
            paramgetjob.add(new BasicNameValuePair("format","json"));
            paramgetjob.add(new BasicNameValuePair("publisher","2048240434418677"));
            paramgetjob.add(new BasicNameValuePair("co",countryName));
            paramgetjob.add(new BasicNameValuePair("l",stateName));
            paramgetjob.add(new BasicNameValuePair("q",mainquery));
            paramgetjob.add(new BasicNameValuePair("userip",ip));
            paramgetjob.add(new BasicNameValuePair("useragent",userAgent));
            paramgetjob.add(new BasicNameValuePair("limit",Integer.toString(limit)));
            paramgetjob.add(new BasicNameValuePair("start",Integer.toString(start)));
            JSONObject jjobObject=jparser.makeHttpRequest(URL,"GET",paramgetjob);
            try {

                jobs=jjobObject.getJSONArray("results");
                if(jobs!=null) {
                    for (int i = 0; i < jobs.length(); i++) {
                        JSONObject job = jobs.getJSONObject(i);
                        String jTitle = job.getString("jobtitle");
                        String jCompany = job.getString("company");
                        String jWebsite = job.getString("url");
                        String jage = job.getString("formattedRelativeTime");
                        String jdescription = job.getString("snippet");
                        String jcity = job.getString("city");
                        jdescription = Html.fromHtml(jdescription).toString();
                        HashMap<String, String> jobmap = new HashMap<String, String>();
                        jobmap.put(TAG_JTITLE, jTitle);
                        jobmap.put(TAG_JCOMPANY, jCompany);
                        jobmap.put(TAG_JWEBSITE, jWebsite);
                        jobmap.put(TAG_JDESCRIPTION, jdescription);
                        jobmap.put(TAG_JAGE, jage);
                        jobmap.put(TAG_CITY, jcity);
                        jobslist.add(jobmap);
                        new_job_data = new jobDetails[jobs.length()];

                    }
                    for (int j = 0; j < jobs.length(); j++) {
                        new_job_data[j] = new jobDetails(R.drawable.no_image, jobslist.get(j).get(TAG_JTITLE), jobslist.get(j).get(TAG_JCOMPANY), jobslist.get(j).get(TAG_JAGE), jobslist.get(j).get(TAG_JDESCRIPTION), jobslist.get(j).get(TAG_JWEBSITE), jobslist.get(j).get(TAG_CITY));

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pDialog.isShowing()){
                pDialog.cancel();
            }


            // Adding Load More button to lisview at bottom

             customListAdapter.notifyDataSetChanged();
             int currentPosition = listmain.getFirstVisiblePosition();
             job_data = concat(job_data, new_job_data);

             // Appending new data to menuItems ArrayList
             customListAdapter = new CustomListAdapter(mContex, R.layout.main_list, job_data);

             listmain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*position+=limit;*/
                     view.startAnimation(loadmoreanimation);
                     Toast.makeText(mContex, job_data[position].url.toString(), Toast.LENGTH_SHORT).show();
                     sdb=new SavedDatabaseHandler(mContex);
                     sdb.saveData(new SavedJobDetails(job_data[position].title.toString(),job_data[position].company.toString(),job_data[position].description.toString(),job_data[position].city.toString(),job_data[position].age.toString(),job_data[position].url.toString(),R.drawable.no_image));
                     Intent jobdetailsintent = new Intent(mContex, Describe.class);
                     if (job_data[position].url.toString() != null)
                         jobdetailsintent.putExtra("wurl", job_data[position].url.toString());
                     else
                         jobdetailsintent.putExtra("wurl", "http://img.sur.ly/thumbnails/620x343/p/pdftowordconverter.seoethic.it.png");

                     startActivity(jobdetailsintent);
                 }
             });

            listmain.setAdapter(customListAdapter);
            // Setting new scroll position
            listmain.setSelectionFromTop(currentPosition , 0);

        }

    }

}

