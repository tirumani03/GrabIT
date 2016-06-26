package com.codex.saratchandra.grabit;

/**
 * Created by SaratChandra on 6/24/2016.
 */
import android.content.ContentValues;

/**
 * Created by SaratChandra on 6/3/2016.
 */
public class User {
    int _Uid;
    String _username,_password,_email,_phone,_education,_interests,_first,_second;
    public User(){

    }
    public  User(String username,String password,String first,String second,String email,String phone,String education,String interests){
        this._username=username;
        this._password=password;
        this._email=email;
        this._phone=phone;
        this._education=education;
        this._interests=interests;
        this._first=first;
        this._second=second;
    }

    public User(int id, String username, String password){
        this._Uid=id;
        this._username=username;
        this._password=password;
    }
    public User(String username, String password){
        this._username=username;
        this._password=password;
    }
    public int getId(){
        return this._Uid;
    }
    public String getUsersName(){
        return this._username;
    }
    public String getPassword(){
        return this._password;
    }
    public String getemail(){ return  this._email;}
    public String getphone(){ return  this._phone;}
    public String geteducation(){ return  this._education;}
    public String getinterests(){ return  this._interests;}
    public String getfirst(){return this._first;}
    public String getsecond(){return this._second;}

    public void setpassword(String password){
        this._password=password;
    }
    public void setfirst(String first){
        this._first=first;
    }
    public void setsecond(String second){
        this._second=second;
    }
    public void setemail(String email){
        this._email=email;
    }
    public void setusername(String username){
        this._username=username;
    }
    public void setinterests(String interests){
        this._interests=interests;
    }
    public void setphone(String phone){
        this._phone=phone;
    }
    public void seteducation(String education){
        this._education=education;
    }


}