package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class VideoPlaylistInside extends AppCompatActivity implements rvInterface{
    MyDatabaseHelper mdh = new MyDatabaseHelper(VideoPlaylistInside.this);
    ArrayList<VideolistModel> vm = new ArrayList<>();
    VideoAdapter adapter;
    Toolbar toolbar;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playlist_inside);

        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        rv = findViewById(R.id.recyclerview);

        displayData();

        adapter = new VideoAdapter(VideoPlaylistInside.this, vm, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(VideoPlaylistInside.this,LinearLayoutManager.VERTICAL,false));
    }

    void displayData(){
        Cursor cursor = mdh.readVideoPlaylist();

        if(cursor.getCount() == 0){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                int url = cursor.getInt(2);
                vm.add(new VideolistModel(title, artist, url));
            }
        }
        cursor.close();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(VideoPlaylistInside.this, VideoPlayer.class);

        intent.putExtra("VIDEO TITLE", vm.get(position).getVideoName());
        intent.putExtra("ARTIST NAME", vm.get(position).getArtistName());
        intent.putExtra("VIDEO URL", vm.get(position).getUrl());

        startActivity(intent);
        Toast.makeText(VideoPlaylistInside.this, "Pressed pressed", Toast.LENGTH_SHORT).show();
    }
}