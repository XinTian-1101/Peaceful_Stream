package com.example.myapplication.WellnessModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;

public class M1_AddSongToPlaylist extends AppCompatActivity implements M1_rvInterface {

    RecyclerView rv;
    Button createPlaylistBtn;
    M1_DatabaseHelper mdh = new M1_DatabaseHelper(M1_AddSongToPlaylist.this);
    ArrayList<M1_PlaylistModel> playlist = new ArrayList<>();
    M1_ATPAdapter adapter;

    String title, artist;
    Toolbar toolbar;
    int url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_activity_add_song_to_playlist);

        toolbar = findViewById(R.id.toolBar);

        //set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

        rv = findViewById(R.id.recyclerview5);

        //retrieve data from previous activity
        title = getIntent().getStringExtra("SONG TITLE");
        artist = getIntent().getStringExtra("ARTIST NAME");
        url = getIntent().getIntExtra("SONG URL",100);

        createPlaylistBtn = findViewById(R.id.button2);

        //onclick to create new playlist
        createPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M1_AddSongToPlaylist.this, M1_AddPlaylist.class);

                intent.putExtra("PLAYLIST ID 1",playlist.size()+1);
                intent.putExtra("SONG TITLE 1", title);
                intent.putExtra("ARTIST NAME 1", artist);
                intent.putExtra("SONG URL 1", url);

                startActivity(intent);
            }
        });

        //store data to display
        displayData();

        adapter = new M1_ATPAdapter(M1_AddSongToPlaylist.this, playlist,this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(M1_AddSongToPlaylist.this,LinearLayoutManager.VERTICAL,false));
    }

    //apply back to up button
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //method to store data for display
    void displayData(){
        Cursor cursor = mdh.readPlaylistData();

        if(cursor.getCount() == 0){

        }
        else{
            cursor.moveToPosition(4);
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                playlist.add(new M1_PlaylistModel(title, id));
            }
        }
        cursor.close();
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(M1_AddSongToPlaylist.this, M1_MainActivity.class);
        M1_DatabaseHelper mdh = new M1_DatabaseHelper(this);

        int id = playlist.get(position).getId();
        mdh.addSongIntoPlaylist(title,artist,url,id); //add song to existing playlist

        startActivity(intent);
        Toast.makeText(M1_AddSongToPlaylist.this,title+" is added to "+playlist.get(position).getPlaylistTitle(),Toast.LENGTH_SHORT).show();
    }
}