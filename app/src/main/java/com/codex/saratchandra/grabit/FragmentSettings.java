package com.codex.saratchandra.grabit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by SaratChandra on 6/21/2016.
 */
public class FragmentSettings extends Fragment {
    ListView setlist;
    String[] settingarray = {"Filters","Change account","change location","clear history","help","Privacy terms","Report"};
    Integer[] imageId = {
            R.drawable.ic_menu_gallery,
            R.drawable.ic_home_gray_24dp,
            R.drawable.ic_menu_send,
            R.drawable.ic_delete_forever_black_36dp,
            R.drawable.ic_help_black_36dp,
            R.drawable.ic_menu_manage,
            R.drawable.ic_report_problem_black_36dp,

    };
    public FragmentSettings getInstance(int position) {
        FragmentSettings settingsFragment = new FragmentSettings();
        Bundle args = new Bundle();
        args.putInt("position", position);
        settingsFragment.setArguments(args);
        return settingsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View settingview = inflater.inflate(R.layout.fragment_settings, container, false);
        setlist=(ListView)settingview.findViewById(R.id.settingslist);
        CustomSettingsListView settingsadapter = new
                CustomSettingsListView(getActivity(), settingarray, imageId);

        setlist.setAdapter(settingsadapter);
        setlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " +settingarray[+ position], Toast.LENGTH_SHORT).show();
                switch (position){
                    case 3:
                        SavedDatabaseHandler sdb=new SavedDatabaseHandler(getActivity());
                        sdb.removeSaved();
                        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
                        suggestions.clearHistory();
                        break;
                }

            }
        });
        return settingview;
    }

}
