package com.example.testdb;

import static com.google.common.reflect.Reflection.getPackageName;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "PlaylistDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "playlist";
    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table playlist (playlist_id INTEGER primary key autoincrement, title TEXT)");
        db.execSQL("create Table song (song_id INTEGER primary key autoincrement, song_title TEXT, artist_name TEXT, url INTEGER)");
        db.execSQL("create Table song_playlist (playlist_id INTEGER, song_title TEXT, artist_name TEXT, url INTEGER)");
        db.execSQL("create Table video_playlist (video_id INTEGER primary key autoincrement, video_title TEXT, artist_name TEXT, url INTEGER)");

        createInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists playlist");
        db.execSQL("drop Table if exists song");
        db.execSQL("drop Table if exists song_playlist");
        db.execSQL("drop Table if exists video_playlist");

        onCreate(db);
    }

    //create initial fixed data
    public void createInitialData(SQLiteDatabase db){
        db.execSQL("insert into playlist (title) VALUES ('Peaceful Piano')"); //1
        db.execSQL("insert into playlist (title) VALUES ('lofi meditation')"); //2
        db.execSQL("insert into playlist (title) VALUES ('Peaceful Meditation')"); //3
        db.execSQL("insert into playlist (title) VALUES ('All Songs')"); //4
        db.execSQL("insert into playlist (title) VALUES ('Liked Song')"); //5
        db.execSQL("insert into playlist (title) VALUES ('You are the BEST!')"); //6

        //set songs
        db.execSQL("insert into song (song_title, artist_name, url) VALUES ('After Dark', 'Take Me Far Away', "+R.raw.after_dark+")");
        db.execSQL("insert into song (song_title, artist_name, url) VALUES ('Calm World', 'little Circuits', "+R.raw.calm_world+")");
        db.execSQL("insert into song (song_title, artist_name, url) VALUES ('Deep and Hollow', 'Thoas Galla', "+R.raw.deep_and_hollow+")");
        db.execSQL("insert into song (song_title, artist_name, url) VALUES ('Reverie Lofi', 'Juanita Garcia', "+R.raw.reverie_lofi+")");
        db.execSQL("insert into song (song_title, artist_name, url) VALUES ('Grow', 'Natana Bach', "+R.raw.grow+")");

        //set songs into playlist
        //playlist id 4 for liked songs
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (6, 'After Dark', 'Take Me Far Away', "+R.raw.after_dark+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (6, 'Calm World', 'little Circuits', "+R.raw.calm_world+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (6, 'Deep and Hollow', 'Thoas Galla', "+R.raw.deep_and_hollow+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (1, 'Calm World', 'little Circuits', "+R.raw.calm_world+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (1, 'Deep and Hollow', 'Thoas Galla', "+R.raw.deep_and_hollow+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (2, 'Reverie Lofi', 'Juanita Garcia', "+R.raw.reverie_lofi+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (2, 'After Dark', 'Take Me Far Away', "+R.raw.after_dark+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (3, 'Grow', 'Natana Bach', "+R.raw.grow+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (4, 'Calm World', 'little Circuits', "+R.raw.calm_world+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (4, 'Deep and Hollow', 'Thoas Galla', "+R.raw.deep_and_hollow+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (4, 'Reverie Lofi', 'Juanita Garcia', "+R.raw.reverie_lofi+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (4, 'After Dark', 'Take Me Far Away', "+R.raw.after_dark+")");
        db.execSQL("insert into song_playlist (playlist_id, song_title, artist_name, url) VALUES (4, 'Grow', 'Natana Bach', "+R.raw.grow+")");


        db.execSQL("insert into video_playlist (video_title, artist_name, url) VALUES ('How To Meditate 1', 'Artist 1', "+R.raw.how_to_meditate_1+")");
        db.execSQL("insert into video_playlist (video_title, artist_name, url) VALUES ('How To Meditate 2', 'Artist 2', "+R.raw.how_to_meditate_2+")");
    }

    //to help sync data with playlist click MainActivity
    Cursor syncPlaylistID(String playlistTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select playlist_id from playlist where title = '"+playlistTitle+"'";
        //select if same name
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    //when click button to create playlist
    void createPlaylist(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title",title);

        long result = db.insert("playlist",null,cv);

        if(result==-1){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Successful",Toast.LENGTH_SHORT).show();
        }
    }

    void addIntoLiked(String title, String artist, int url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("song_title",title);
        cv.put("artist_name",artist);
        cv.put("url",url);
        cv.put("playlist_id",5);

        long result = db.insert("song_playlist",null,cv);

        if(result==-1){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"added to Liked",Toast.LENGTH_SHORT).show();
        }
    }

    void removeFromLiked(String title, String artist, int url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String selection = "song_title = ? and playlist_id = ?";
        String[] selectionArgs = {title, String.valueOf(5)};

        long result = db.delete("song_playlist",selection,selectionArgs);

        if(result==-1){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Removed",Toast.LENGTH_SHORT).show();
        }
    }

//    void addSongToGoodNightPlaylist(String song_title, String artist_name, int song_url){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put("song_title",song_title);
//        cv.put("artist_name",artist_name);
//        cv.put("song_url",song_url);
//
//        db.insert("goodnight_playlist",null,cv);
//    }

    //read playlist title from database
    Cursor readPlaylistNameData(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery("select title from playlist",null);
        }
        return cursor;
    }

    Cursor readPlaylistData(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery("select playlist_id, title from playlist",null);
        }
        return cursor;
    }

    Cursor readVideoPlaylist(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery("select video_title, artist_name, url from video_playlist",null);
        }
        return cursor;
    }

    void addSongIntoPlaylist(String title, String artist, int url, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("song_title",title);
        cv.put("artist_name",artist);
        cv.put("url",url);
        cv.put("playlist_id", id);

        db.insert("song_playlist",null,cv);
    }

    Cursor readAllSongs(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery("select song_title, artist_name, url from song",null);
        }

        return cursor;
    }

    //read playlist to store and fetch song info
    Cursor readPlaylist(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery("select song_title, artist_name, url from song_playlist where playlist_id = "+id,null);
        }

        return cursor;
    }

    boolean checkLikedSong(String title){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery("select * from song_playlist where song_title = '"+title+"' and playlist_id = "+5,null);
        }

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }
}
