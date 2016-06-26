package com.codex.saratchandra.grabit;

/**
 * Created by SaratChandra on 6/18/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<jobDetails>{

    Context context;
    int layoutResourceId;
    jobDetails data[] = null;

    public CustomListAdapter(Context context, int layoutResourceId, jobDetails[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        JobHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new JobHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.JobTitle = (TextView)row.findViewById(R.id.jobtitle);
            holder.JobCompany=(TextView)row.findViewById(R.id.company);
            holder.JobAge=(TextView)row.findViewById(R.id.age);
            holder.JobDescription=(TextView)row.findViewById(R.id.description);
            holder.favourite=(ImageView)row.findViewById(R.id.favourite);
            holder.city=(TextView)row.findViewById(R.id.ecity);
            row.setTag(holder);
        }
        else
        {
            holder = (JobHolder) row.getTag();
        }

        jobDetails jobdetails = data[position];
        holder.JobTitle.setText(jobdetails.title);
        holder.JobDescription.setText(jobdetails.description);
        holder.JobCompany.setText(jobdetails.company);
        holder.JobAge.setText(jobdetails.age);
        holder.imgIcon.setImageResource(jobdetails.icon);
        holder.city.setText(jobdetails.city);

        return row;
    }

    static class JobHolder
    {
        ImageView imgIcon;
        TextView JobTitle;
        TextView JobCompany;
        TextView JobAge;
        TextView JobDescription;
        ImageView favourite;
        TextView city;
    }
}
