package com.codex.saratchandra.grabit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SaratChandra on 6/21/2016.
 */
public class FragmentSaved extends Fragment {
    SharedPreferences saved;
    SavedDatabaseHandler sdb;
    int i;
    SavedJobDetails sjd[];
    ListView savedlist;
    jobDetails[] jd;
    AlphaAnimation anim=new AlphaAnimation(1F,0.6F);
    String stitle,scompany,sdescription,sage,surl,scity;
    public FragmentSaved getInstance(int position) {
        FragmentSaved savedFragment = new FragmentSaved();
        Bundle args = new Bundle();
        args.putInt("position", position);
        savedFragment.setArguments(args);
        return savedFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentsaved = inflater.inflate(R.layout.fragment_saved, container, false);
        savedlist=(ListView)fragmentsaved.findViewById(R.id.listsaved);
          sdb=new SavedDatabaseHandler(getActivity());


        i=sdb.getNumsaved();
        sjd=new SavedJobDetails[i+1];
       Toast.makeText(getActivity(),Integer.toString(i),Toast.LENGTH_SHORT).show();
        jd=new jobDetails[i];

        for(int j=0;j<i;j++){
        sjd[j]=sdb.getSavedDetails(j+1);
            if(sjd!=null){

        jd[j]=new jobDetails(sjd[j].icon,sjd[j].title,sjd[j].company,sjd[j].description,sjd[j].age,sjd[j].url,sjd[j].city);
                Toast.makeText(getActivity(),jd[j].title,Toast.LENGTH_SHORT).show();
        }}
        CustomListAdapter cla=new CustomListAdapter(getActivity(),R.layout.main_list,jd);
          savedlist.setAdapter(cla);
        savedlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.startAnimation(anim);
                Toast.makeText(getActivity(), jd[position].url.toString(), Toast.LENGTH_SHORT).show();
                Intent jobdetailsintent = new Intent(getActivity(), Describe.class);
                if (jd[position].url.toString() != null)
                    jobdetailsintent.putExtra("wurl", jd[position].url.toString());
                else
                    jobdetailsintent.putExtra("wurl", "http://img.sur.ly/thumbnails/620x343/p/pdftowordconverter.seoethic.it.png");

                startActivity(jobdetailsintent);
            }
        });
        return fragmentsaved;
    }
}
