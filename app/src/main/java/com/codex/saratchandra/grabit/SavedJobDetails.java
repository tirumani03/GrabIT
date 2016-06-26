package com.codex.saratchandra.grabit;

/**
 * Created by SaratChandra on 6/25/2016.
 */
public class SavedJobDetails {
    public int icon=R.drawable.no_image;
    public String title;
    public  String company;
    public String age;
    public String description;
    public String url;
    public String city;

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SavedJobDetails(){
        super();
    }

    public SavedJobDetails(String title,String company,String age,String description,String city,String url,int icon) {
        super();
        this.icon=icon;
        this.title = title;
        this.age=age;
        this.company=company;
        this.url=url;
        this.city=city;
        this.description=description;
    }
}
