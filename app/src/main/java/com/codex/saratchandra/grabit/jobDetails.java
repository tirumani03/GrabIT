package com.codex.saratchandra.grabit;

/**
 * Created by SaratChandra on 6/20/2016.
 */
public class jobDetails {
    public int icon;
    public String title;
    public  String company;
    public String age;
    public String description;
    public String url;
    public String city;

    public jobDetails(){
        super();
    }

    public jobDetails(int icon, String title,String company,String age,String description,String url,String city) {
        super();
        this.icon = icon;
        this.title = title;
        this.age=age;
        this.company=company;
       this.url=url;
        this.city=city;
        this.description=description;
    }
}
