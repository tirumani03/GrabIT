package com.codex.saratchandra.grabit;

/**
 * Created by SaratChandra on 6/18/2016.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by SaratChandra on 6/3/2016.
 */
public class Describe extends AppCompatActivity implements View.OnClickListener {
    WebView web;
    GestureDetector gs=null;
    Button applynow;
    ProgressBar bar;
    String url;
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public String CheckNet() {
        String html=null;
        if (!haveNetworkConnection()) {
            // File f= new File(Environment.getExternalStorageDirectory(),"MyFolder");
            //String filename ="image.png";
            // String imagePath = "file://"+ f.getAbsolutePath() + File.separator + filename;
            /*String imagePath = "mipmap://" + R.mipmap.nointernet;
            html = "<html><head></head><body><Marquee><img src=\"" + imagePath + "\">My Scroll Text</Marquee></body></html>";
*/
            // web.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
        }
        return html;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.describe);

       /* Toolbar d_toolbar=(Toolbar)findViewById(R.id.dtoolbar);
        d_toolbar.setTitle("Details");
        setSupportActionBar(d_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
       /* Intent dmyIntent=getIntent();
        if(Intent.ACTION_SEARCH.equals(dmyIntent.getAction())){
            String query=dmyIntent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
        }
       */ url=getIntent().getExtras().getString("wurl");
        // getWindow().requestFeature(Window.FEATURE_PROGRESS);
        web=(WebView)findViewById(R.id.web);
        String code=CheckNet();
        web.getSettings().setBuiltInZoomControls(true);
        if(!haveNetworkConnection()&& code!=null){
            web.loadDataWithBaseURL("", code, "text/html", "utf-8", "");}
        else
            web.loadUrl(url);
        WebSettings ws=web.getSettings();
        ws.setJavaScriptEnabled(true);
        bar =(ProgressBar)findViewById(R.id.bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                bar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                bar.setVisibility(View.GONE);
            }

        });
        web.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // super.onProgressChanged(view, newProgress);
                bar.setProgress(newProgress);
            }
        });
        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

    }

    @Override
    public void onBackPressed() {

        if(web.canGoBack()){
            web.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.describe_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.copy:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("URL",url);
                clipboard.setPrimaryClip(clip);
              break;
            case R.id.email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Check this Job postiong");
                emailIntent.putExtra(Intent.EXTRA_TEXT, url);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Describe.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.message:
                Intent smsLauncher = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
                smsLauncher.putExtra("sms_body",url);
                try{
                    startActivity(smsLauncher);
                }catch(ActivityNotFoundException e){

                }
               break;
            case R.id.reload:
                web.reload();
                break;
            case R.id.whatsaap:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        String code=CheckNet();
        if(!haveNetworkConnection()&& code!=null){
            web.loadDataWithBaseURL("", code, "text/html", "utf-8", "");}
        else
        {


        }
    }
}
