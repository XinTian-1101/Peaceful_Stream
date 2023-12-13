package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBName = "userData.db";

    public DBHelper(@Nullable Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table users(username TEXT primary key, email TEXT , password TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists users");
    }

    public boolean addUser (String username , String email , String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues info = new ContentValues();
        info.put("username" , username);
        info.put("email" , email);
        info.put("password" , password);
        info.put("loggedIn" , false);
        long result = db.insert("users" , null , info);
        if (result == -1) return false;
        else return true;
    }

    public boolean doesUsernameExist (String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where username = ?" , new String [] {username});
        if (cursor.getCount() > 0) return true;
        else return false;
    }
    public boolean doesEmailExist (String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where email = ?" , new String [] {email});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean auth (String email , String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where email = ? and password = ?" , new String[]{email , password});
        if (cursor.getCount() > 0) {
            return true;
        }
        else return false;
    }
}
