package com.codex.saratchandra.grabit;

/**
 * Created by SaratChandra on 6/24/2016.
 */
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SaratChandra on 6/7/2016.
 */
public class UsersDatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME="UserCredentials";
    public static final String TABLE_NAME="Users";

    public static final String KEY_ID="Uid";    //rememeber that Id is created there not KEY_ID.
    public static final String KEY_USER_NAME="username";
    public static final String KEY_PASS_WORD="password";
    public  static final String KEY_EMAIL="email";
    public static final String KEY_PHONE="phone";
    public static final String KEY_EDUCATION="education";
    public static final String KEY_INTERESTS="interest";
    public static final String KEY_FIRST="first";
    public static final String KEY_SECOND="second";
    public UsersDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+ KEY_ID+" INTEGER PRIMARY KEY, "+KEY_USER_NAME+" TEXT NOT NULL UNIQUE, "+KEY_PASS_WORD+" TEXT NOT NULL, "+KEY_FIRST+" TEXT, "+KEY_SECOND+" TEXT, "+KEY_EMAIL+" TEXT, "+KEY_PHONE+" TEXT, "+KEY_EDUCATION+" TEXT, "+KEY_INTERESTS+" TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME+";");
        onCreate(db);
    }
    public void addCredential(User ud){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        // values.put(KEY_ID,ud.getId());
        values.put(KEY_FIRST,ud.getfirst());
        values.put(KEY_SECOND,ud.getsecond());
        values.put(KEY_EMAIL,ud.getemail());
        values.put(KEY_EDUCATION,ud.geteducation());
        values.put(KEY_PHONE,ud.getphone());
        values.put(KEY_INTERESTS,ud.getinterests());
        values.put(KEY_USER_NAME,ud.getUsersName());
        values.put(KEY_PASS_WORD,ud.getPassword());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    public void removeCredential(User ud){
        this.getWritableDatabase().delete(TABLE_NAME,KEY_USER_NAME+"='"+ud.getUsersName()+"'",null);
    }
    public void updatefirst(String old_name,String new_num){
        this.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+KEY_FIRST+"='"+new_num+"' WHERE "+KEY_FIRST+"='"+old_name+"'");
    }
    public void updateemail(String old_name,String new_num){
        this.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+KEY_EMAIL+"='"+new_num+"' WHERE "+KEY_EMAIL+"='"+old_name+"'");
    }
    public void updatephone(String old_name,String new_num){
        this.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+KEY_PHONE+"='"+new_num+"' WHERE "+KEY_PHONE+"='"+old_name+"'");
    }
    public void updateeducation(String old_name,String new_num){
        this.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+KEY_EDUCATION+"='"+new_num+"' WHERE "+KEY_EDUCATION+"='"+old_name+"'");
    }
    public void updateinterest(String old_name,String new_num){
        this.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+KEY_INTERESTS+"='"+new_num+"' WHERE "+KEY_INTERESTS+"='"+old_name+"'");
    }
    public void listprofile(TextView get){
        Cursor cur= this.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME,null);
        get.setText("");
        while (cur.moveToNext()) {
            get.append(cur.getString(1)+" "+cur.getString(2)+"\n");
        }
    }
    public boolean fetchCredential(String u,String p){
        SQLiteDatabase db=this.getWritableDatabase();
        this.getReadableDatabase().execSQL("SELECT "+KEY_USER_NAME+","+KEY_PASS_WORD+" FROM "+TABLE_NAME+" WHERE "+KEY_USER_NAME+"='"+u+"'",null);
        if(KEY_PASS_WORD==p){
            return true;
        }
        else
            return false;
    }
    public int userLogin(String u,String p){
        SQLiteDatabase db=this.getWritableDatabase();
        String SELECT_USERS ="SELECT * FROM users WHERE username= '"+u+"' AND password='"+p+"'";
        Cursor cursor=db.rawQuery(SELECT_USERS,null);
        cursor.moveToFirst();
        int i=cursor.getCount();
        cursor.close();
        return i;
    }
    public void updateCred(User ud){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_USER_NAME,ud.getUsersName());
        values.put(KEY_PASS_WORD,ud.getPassword());
        //  db.update(TABLE_NAME,values,KEY_ID+);
    }
    public List<String>getAllNames(){
        SQLiteDatabase db=this.getWritableDatabase();
        String SELECT_CONTACTS ="SELECT * FROM "+TABLE_NAME;
        Cursor cur=db.rawQuery(SELECT_CONTACTS,null);
        List<String> contactList=new ArrayList<String>();
        if(cur.moveToNext()){
            do{
                contactList.add(cur.getString(1));

            }while(cur.moveToNext());
        }
        cur.close();
        return contactList;
    }
    public void updateDetails(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST, user.getfirst());
        values.put(KEY_SECOND, user.getsecond());
        values.put(KEY_EMAIL, user.getemail());
        values.put(KEY_PHONE, user.getphone());
        values.put(KEY_EDUCATION, user.geteducation());
        db.update(TABLE_NAME,values,KEY_USER_NAME+" = ? ", new String[]{user.getUsersName()});
        db.close();
    }
    public User getDetails(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        String SELECT_DETAILS = "SELECT * FROM " + TABLE_NAME+" WHERE "+KEY_USER_NAME+"='"+username+"'";
        Cursor cursor = db.rawQuery(SELECT_DETAILS, null);
        if (cursor.moveToFirst()){
            do {
                user.setusername(cursor.getString(1));
                user.setpassword(cursor.getString(2));
                user.setfirst(cursor.getString(3));
                user.setsecond(cursor.getString(4));
                user.setemail(cursor.getString(5));
                user.setphone(cursor.getString(6));
                user.seteducation(cursor.getString(7));
                user.setinterests(cursor.getString(8));

            }while (cursor.moveToNext());
            cursor.close();
            return user;
        }
        return null;
    }

    public int getNumContacts(){
        SQLiteDatabase db=this.getWritableDatabase();
        String SELECT_USERS ="SELECT * FROM "+TABLE_NAME;
        Cursor cur = db.rawQuery(SELECT_USERS,null);
        return cur.getCount();
    }

}