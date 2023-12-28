package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements rvInterface{

    RecyclerView rv;
    ImageButton playlist1, playlist2, playlist3, playlist4;
    FloatingActionButton addButton;

    MyDatabaseHelper mdh = new MyDatabaseHelper(MainActivity.this);
    ArrayList<PlaylistModel> pl_title = new ArrayList<>(); //arraylist to store data of each playlist
    Adapter adapter;

    Toolbar toolbar;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize data field
        rv = findViewById(R.id.playlistRV);
        addButton = findViewById(R.id.floatingActionButton);
        playlist1 = findViewById(R.id.ic_lofiMedi);
        playlist2 = findViewById(R.id.ic_peacefulMedi);
        playlist3 = findViewById(R.id.ic_peacefulPiano);
        playlist4 = findViewById(R.id.ic_mediVideo);
        toolbar = findViewById(R.id.toolBar);
        search = findViewById(R.id.search_bar);

        //set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        //onclick to go to search songs page
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, searchSong.class);
                startActivity(i);
            }
        });

        //for lofi meditation playlist
        playlist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlaylistInside.class);

                TextView tv = findViewById(R.id.title_lofiMedi);
                String playlist = tv.getText().toString();
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ICON",R.drawable.lofimedi_ic);

                //fetch playlist id from database
                int id = syncPlaylistData(playlist);
                intent.putExtra("PLAYLIST ID",id);

                startActivity(intent);
            }
        });

        //for peaceful meditation playlist
        playlist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlaylistInside.class);

                TextView tv = findViewById(R.id.title_peacefulMedi);
                String playlist = tv.getText().toString();
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ICON",R.drawable.peacefulmedi_i);

                //fetch playlist id from database
                int id = syncPlaylistData(playlist);
                intent.putExtra("PLAYLIST ID",id);

                startActivity(intent);
            }
        });

        //for peaceful piano playlist
        playlist3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlaylistInside.class);

                TextView tv = findViewById(R.id.title_peacefulPiano);
                String playlist = tv.getText().toString();
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ICON",R.drawable.peacefulpiano_ic);

                //fetch playlist id from database
                int id = syncPlaylistData(playlist);
                intent.putExtra("PLAYLIST ID",id);

                startActivity(intent);
            }
        });

        //for meditation videos playlist
        playlist4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoPlaylistInside.class);
                startActivity(intent);
            }
        });

//        search songs
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, searchSong.class);
//                startActivity(i);
//            }
//        });

        //store data to display
        storeDisplayData();

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
        int id = 0;

        while(cursor.moveToNext()){
            id = cursor.getInt(0);
        }

        return id;
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, PlaylistInside.class);

        String title = pl_title.get(position).getPlaylistTitle();
        intent.putExtra("PLAYLIST NAME",title);
        if(position==0) //liked song playlist
            intent.putExtra("PLAYLIST ICON",R.drawable.likedsong_ic);
        else if(position==1) //you are the best playlist
            intent.putExtra("PLAYLIST ICON",R.drawable.urthebest_ic);
        else //other new playlist
            intent.putExtra("PLAYLIST ICON",R.drawable.extra_pl_ic2);

        //fetch playlist id from database
        int id = syncPlaylistData(title);
        intent.putExtra("PLAYLIST ID",id);

        startActivity(intent);
    }
}