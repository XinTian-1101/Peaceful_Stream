package com.example.test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class M5_SurveyDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SurveyDatabase";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database;

    // Constructor
    public M5_SurveyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your table with necessary columns
        String createTableQuery = "CREATE TABLE SurveyResponses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "question1 TEXT," +
                "question2 TEXT," +
                "question3 TEXT," +
                "question4 TEXT," +
                "question5 TEXT);";
        db.execSQL(createTableQuery);
    }

    public void openDatabase() {
        database = this.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS SurveyResponses");
        onCreate(db);
    }

    public Cursor getAllSurveyResponses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("SurveyResponses", null, null, null, null, null, null);
    }
}