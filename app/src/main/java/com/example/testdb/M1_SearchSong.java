package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class M1_SearchSong extends AppCompatActivity implements M1_saInterface {

    M1_SearchAdapter adapter;
    RecyclerView rv;
    ArrayList<M1_SonglistModel> allSong = new ArrayList<>();
    ArrayList<M1_SonglistModel> filteredSong = new ArrayList<>();

    Toolbar toolbar;

    M1_DatabaseHelper mdh = new M1_DatabaseHelper(M1_SearchSong.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_activity_search_song);

        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

        rv = findViewById(R.id.rview);

        rv.setLayoutManager(new LinearLayoutManager(M1_SearchSong.this));

        storeData();

        adapter = new M1_SearchAdapter(M1_SearchSong.this, this, allSong);
        rv.setAdapter(adapter);

        EditText et = findViewById(R.id.search_bar);

        //detect change in edit text
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

    private ArrayList<M1_SonglistModel> filter(String text){
        ArrayList<M1_SonglistModel> filteredList = new ArrayList<>();

        //filter songs
        for(M1_SonglistModel song : allSong){
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
                allSong.add(new M1_SonglistModel(title, artist, url));
            }
        }
        cursor.close();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(M1_SearchSong.this, M1_MusicPlayer.class);

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
        }
        else{
            mdh.addIntoLiked(title, artist, url);
        }
    }

    @Override
    public void onAdpButtonClick(int position) {
        Intent intent = new Intent(M1_SearchSong.this, M1_AddSongToPlaylist.class);

        //fetch song data
        intent.putExtra("SONG TITLE", allSong.get(position).getSongName());
        intent.putExtra("ARTIST NAME", allSong.get(position).getArtistName());
        intent.putExtra("SONG URL", allSong.get(position).getUrl());

        startActivity(intent);
    }
}