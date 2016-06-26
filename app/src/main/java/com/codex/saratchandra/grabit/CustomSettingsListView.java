package com.codex.saratchandra.grabit;

/**
 * Created by SaratChandra on 6/21/2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSettingsListView extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public CustomSettingsListView(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.list_single_setting, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_setting, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.settingtxt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.settingimg);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
