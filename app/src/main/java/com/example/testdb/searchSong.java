package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class searchSong extends AppCompatActivity implements saInterface {

    SearchAdapter adapter;
    RecyclerView rv;
    ArrayList<SonglistModel> allSong = new ArrayList<>();
    ArrayList<SonglistModel> filteredSong = new ArrayList<>();

    MyDatabaseHelper mdh = new MyDatabaseHelper(searchSong.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_song);

        rv = findViewById(R.id.rview);

        rv.setLayoutManager(new LinearLayoutManager(searchSong.this));

        storeData();

        adapter = new SearchAdapter(searchSong.this, this, allSong);
        rv.setAdapter(adapter);

        EditText et = findViewById(R.id.search_bar);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filteredSong = filter(s.toString());
            }
        });
    }

    private ArrayList<SonglistModel> filter(String text){
        ArrayList<SonglistModel> filteredList = new ArrayList<>();

        for(SonglistModel song : allSong){
            if(song.getSongName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(song);
            }
        }

        adapter.filterList(filteredList);

        return filteredList;   }

    void storeData(){
        Cursor cursor = mdh.readAllSongs();

        if(cursor.getCount() == 0){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                int url = cursor.getInt(2);
                allSong.add(new SonglistModel(title, artist, url));
            }
        }
        cursor.close();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(searchSong.this, MusicPlayer.class);

        intent.putExtra("SONG TITLE", filteredSong.get(position).getSongName());
        intent.putExtra("ARTIST NAME", filteredSong.get(position).getArtistName());
        intent.putExtra("SONG URL", filteredSong.get(position).getUrl());
        intent.putExtra("PLAYLIST ID", 4);

        startActivity(intent);
    }

    @Override
    public void onLikeButtonClick(int position) {
        String title = allSong.get(position).getSongName();
        String artist = allSong.get(position).getArtistName();
        int url = allSong.get(position).getUrl();

        if(mdh.checkLikedSong(title)){
            mdh.removeFromLiked(title, artist, url);
//            Toast.makeText(PlaylistInside.this,"Ada!",Toast.LENGTH_SHORT).show();
        }
        else{
            mdh.addIntoLiked(title, artist, url);
//            Toast.makeText(PlaylistInside.this,"Tiada!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAdpButtonClick(int position) {
        Intent intent = new Intent(searchSong.this, AddSongToPlaylist.class);

        //fetch song data
        intent.putExtra("SONG TITLE", allSong.get(position).getSongName());
        intent.putExtra("ARTIST NAME", allSong.get(position).getArtistName());
        intent.putExtra("SONG URL", allSong.get(position).getUrl());

        startActivity(intent);
    }
}