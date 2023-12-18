package com.example.assignments.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.assignments.Interface.OnDatabaseChangedListener;
import com.example.assignments.Item.RecordingItem;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "saved_recording.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "saved_recording_table";


    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_TIME_ADDED= "time_added";
    private static OnDatabaseChangedListener onDatabaseChangedListener;


    private static final String COMA_SEP =",";

    private static final String SQLITE_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME + " ("+"id INTEGER PRIMARY KEY" +
            " AUTOINCREMENT"+COMA_SEP+
            COLUMN_NAME + " TEXT" +COMA_SEP+
            COLUMN_PATH + " TEXT"+COMA_SEP+
            COLUMN_LENGTH + " INTEGER"+COMA_SEP+
            COLUMN_TIME_ADDED + " INTEGER"+")";

    public DBHelper(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(SQLITE_CREATE_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

    }


    public static void setOnDatabaseChangedListener(OnDatabaseChangedListener listener){

        onDatabaseChangedListener = listener;


    }


    public long addRecording(RecordingItem recordingItem){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, recordingItem.getName());
        contentValues.put(COLUMN_PATH, recordingItem.getPath());
        contentValues.put(COLUMN_LENGTH, recordingItem.getLength());
        contentValues.put(COLUMN_TIME_ADDED, recordingItem.getTime_added());

        long newRowId = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        if (newRowId == -1) {
            Log.e("DBHelper", "Error with saving recording");
        } else {
            if (onDatabaseChangedListener != null) {
                recordingItem.setId(newRowId); // Set the ID of the recordingItem to the new row ID
                onDatabaseChangedListener.onNewDatabaseEntryAdded(recordingItem);
            }
        }
        return newRowId;
    }




    public boolean deleteRecording(long id) {
        SQLiteDatabase db = getWritableDatabase();

        int deletedRows = db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        if (deletedRows > 0) {
            if (onDatabaseChangedListener != null) {
                onDatabaseChangedListener.onDatabaseEntryDeleted(id);
            }
            return true;
        } else {
            Log.d("DBHelper", "No rows deleted. ID: " + id + " may not exist.");
            return false;
        }
    }




    public ArrayList<RecordingItem> getAllAudio() {
        ArrayList<RecordingItem> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id")); // Get the ID from the cursor
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATH));
                int length = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LENGTH));
                long time_added = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME_ADDED));

                RecordingItem recordingItem = new RecordingItem( name, path, length, time_added);
                arrayList.add(recordingItem);
            }
            cursor.close();
        } else {
            Log.d("DBHelper", "Cursor is null. Failed to fetch recordings.");
        }
        db.close(); // Remember to close the database once you're done
        return arrayList;
    }


    public boolean updateRecording(long id, String newName, String newPath) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, newName);
        contentValues.put(COLUMN_PATH, newPath);

        // Updating row
        int updatedRows = db.update(TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        // Check if the update was successful
        if (updatedRows > 0) {
            if (onDatabaseChangedListener != null) {
                onDatabaseChangedListener.onDatabaseEntryRenamed(id,newPath,newPath);
            }
            return true;
        } else {
            Log.e("DBHelper", "Failed to update recording with ID: " + id);
            return false;
        }
    }




}

