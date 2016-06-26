package com.codex.saratchandra.grabit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SaratChandra on 6/25/2016.
 */
public class SavedDatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME="Savedjobs";
    public static final String TABLE_NAME="Saved";
    public static final String KEY_ID="Sid";
    public static final String KEY_SAVED_TITLE="stitle";
    public static final String KEY_SAVED_COMPANY="scompany";
    public static final String KEY_SAVED_DESCRIPTION="sdescription";
    public static final String KEY_SAVED_AGE="sage";
    public static final String KEY_SAVED_CITY="scity";
    public static final String KEY_SAVED_URL="surl";
    public SavedDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+ KEY_ID+" INTEGER PRIMARY KEY, "+KEY_SAVED_TITLE+" TEXT, "+KEY_SAVED_COMPANY+" TEXT, "+KEY_SAVED_DESCRIPTION+" TEXT, "+KEY_SAVED_CITY+" TEXT, "+KEY_SAVED_AGE+" TEXT, "+KEY_SAVED_URL+" TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME+";");
        onCreate(db);
    }
    public void saveData(SavedJobDetails ud){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        // values.put(KEY_ID,ud.getId());
        values.put(KEY_SAVED_TITLE,ud.getTitle());
        values.put(KEY_SAVED_COMPANY,ud.getCompany());
        values.put(KEY_SAVED_DESCRIPTION,ud.getDescription());
        values.put(KEY_SAVED_CITY,ud.getCity());
        values.put(KEY_SAVED_AGE,ud.getAge());
        values.put(KEY_SAVED_URL,ud.getUrl());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    public void removeSaved(){
        this.getWritableDatabase().delete(TABLE_NAME,null,null);
    }
     public void listprofile(TextView get){
        Cursor cur= this.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME,null);
        get.setText("");
        while (cur.moveToNext()) {
            get.append(cur.getString(1)+" "+cur.getString(2)+"\n");
        }
    }
    public List<String> getAllNames(){
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
    public SavedJobDetails getSavedDetails(int i){
        SQLiteDatabase db = this.getReadableDatabase();
        SavedJobDetails saved = new SavedJobDetails();
        String SELECT_DETAILS = "SELECT * FROM " + TABLE_NAME+" WHERE "+KEY_ID+"='"+i+"'";
        Cursor cursor = db.rawQuery(SELECT_DETAILS, null);
        if (cursor.moveToFirst()){
            do {
                saved.setTitle(cursor.getString(1));
                saved.setCompany(cursor.getString(2));
                saved.setDescription(cursor.getString(3));
                saved.setCity(cursor.getString(4));
                saved.setAge(cursor.getString(5));
                saved.setUrl(cursor.getString(6));
                saved.setIcon(R.drawable.no_image);
            }while (cursor.moveToNext());
            cursor.close();
            return saved;
        }
        return null;
    }

    public int getNumsaved(){
        SQLiteDatabase db=this.getWritableDatabase();
        String SELECT_USERS ="SELECT * FROM "+TABLE_NAME;
        Cursor cur = db.rawQuery(SELECT_USERS,null);
        return cur.getCount();
    }

}
