package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class M1_VideoPlaylistInside extends AppCompatActivity implements M1_rvInterface {
    M1_DatabaseHelper mdh = new M1_DatabaseHelper(M1_VideoPlaylistInside.this);
    ArrayList<M1_VideolistModel> vm = new ArrayList<>();
    M1_VideoAdapter adapter;
    Toolbar toolbar;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_activity_video_playlist_inside);

        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

        rv = findViewById(R.id.recyclerview);

        displayData();

        adapter = new M1_VideoAdapter(M1_VideoPlaylistInside.this, vm, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(M1_VideoPlaylistInside.this,LinearLayoutManager.VERTICAL,false));
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
                vm.add(new M1_VideolistModel(title, artist, url));
            }
        }
        cursor.close();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(M1_VideoPlaylistInside.this, M1_VideoPlayer.class);

        intent.putExtra("VIDEO TITLE", vm.get(position).getVideoName());
        intent.putExtra("ARTIST NAME", vm.get(position).getArtistName());
        intent.putExtra("VIDEO URL", vm.get(position).getUrl());

        startActivity(intent);
        Toast.makeText(M1_VideoPlaylistInside.this, "Pressed pressed", Toast.LENGTH_SHORT).show();
    }
}