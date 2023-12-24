package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements rvInterface{

    RecyclerView rv;
    ImageButton pl1, pl2, pl3, pl4;
    FloatingActionButton addButton;

    MyDatabaseHelper mdh = new MyDatabaseHelper(MainActivity.this);
    ArrayList<PlaylistModel> pl_title = new ArrayList<>();
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.playlistRV);
        addButton = findViewById(R.id.floatingActionButton);
        pl1 = findViewById(R.id.ic_lofiMedi);
        pl2 = findViewById(R.id.ic_peacefulMedi);
        pl3 = findViewById(R.id.ic_peacefulPiano);
        pl4 = findViewById(R.id.ic_mediVideo);

        //create liked song playlist
//        Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show();

//        //for relaxation playlist
        pl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlaylistInside.class);

                TextView tv = findViewById(R.id.title_lofiMedi);
                String playlist = tv.getText().toString();
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ICON",R.drawable.lofi_icon);
                Toast.makeText(MainActivity.this, playlist, Toast.LENGTH_SHORT).show();
                int id = syncPlaylistData(playlist);
                intent.putExtra("PLAYLIST ID",id);

                startActivity(intent);
            }
        });

        //for good night playlist
        pl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlaylistInside.class);

                TextView tv = findViewById(R.id.title_peacefulMedi);
                String playlist = tv.getText().toString();
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ICON",R.drawable.peaceful_icon);
                int id = syncPlaylistData(playlist);
                intent.putExtra("PLAYLIST ID",id);

                startActivity(intent);
            }
        });

        //for good night playlist
        pl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlaylistInside.class);

                TextView tv = findViewById(R.id.title_peacefulPiano);
                String playlist = tv.getText().toString();
                int id = syncPlaylistData(playlist);
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ID",id);

                startActivity(intent);
            }
        });

        //for meditation vids playlist
        pl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoPlaylistInside.class);
                startActivity(intent);
            }
        });

//        search songs
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, searchSong.class);
                startActivity(i);
            }
        });

        storeDisplayData();

        Toast.makeText(this,"Here",Toast.LENGTH_SHORT).show();

        adapter = new Adapter(MainActivity.this, pl_title,this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mdh = new MyDatabaseHelper(MainActivity.this);
        pl_title = new ArrayList<>();

        storeDisplayData();

        adapter = new Adapter(MainActivity.this, pl_title,this);
        rv.setAdapter(adapter);
    }

    void storeDisplayData(){
        Cursor cursor = mdh.readPlaylistNameData();

        if(cursor.getCount() == 0){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
        else{
            cursor.moveToPosition(3);
            while(cursor.moveToNext()){
                String str = cursor.getString(0);
                pl_title.add(new PlaylistModel(str));
            }
        }
    }

    private int syncPlaylistData(String title){
        Cursor cursor = mdh.syncPlaylistID(title);
        int s = 0;

        while(cursor.moveToNext()){
            s = cursor.getInt(0);
        }

        return s;
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, PlaylistInside.class);

        String title = pl_title.get(position).getPlaylistTitle();
        intent.putExtra("PLAYLIST NAME",title);
        intent.putExtra("PLAYLIST ICON",R.drawable.lofi_icon);

        int id = syncPlaylistData(title);
        intent.putExtra("PLAYLIST ID",id);

        startActivity(intent);
    }
}