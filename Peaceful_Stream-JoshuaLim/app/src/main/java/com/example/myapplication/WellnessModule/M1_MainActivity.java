package com.example.myapplication.WellnessModule;

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

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class M1_MainActivity extends AppCompatActivity implements M1_rvInterface {

    RecyclerView rv;
    ImageButton playlist1, playlist2, playlist3, playlist4;
    FloatingActionButton addButton;

    M1_DatabaseHelper mdh = new M1_DatabaseHelper(M1_MainActivity.this);
    ArrayList<M1_PlaylistModel> pl_title = new ArrayList<>(); //arraylist to store data of each playlist
    M1_Adapter adapter;

    Toolbar toolbar;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_activity_main);

        //initialize data field
        rv = findViewById(R.id.playlistRV);
        playlist1 = findViewById(R.id.ic_lofiMedi);
        playlist2 = findViewById(R.id.ic_peacefulMedi);
        playlist3 = findViewById(R.id.ic_peacefulPiano);
        playlist4 = findViewById(R.id.ic_mediVideo);
        toolbar = findViewById(R.id.toolBar);
        search = findViewById(R.id.search_bar);

        //set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

        //onclick to go to search songs page
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(M1_MainActivity.this, M1_SearchSong.class);
                startActivity(i);
            }
        });

        //for lofi meditation playlist
        playlist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M1_MainActivity.this, M1_PlaylistInside.class);

                TextView tv = findViewById(R.id.title_lofiMedi);
                String playlist = tv.getText().toString();
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ICON",R.drawable.m1_lofimedi_ic);

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
                Intent intent = new Intent(M1_MainActivity.this, M1_PlaylistInside.class);

                TextView tv = findViewById(R.id.title_peacefulMedi);
                String playlist = tv.getText().toString();
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ICON",R.drawable.m1_peacefulmedi_ic);

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
                Intent intent = new Intent(M1_MainActivity.this, M1_PlaylistInside.class);

                TextView tv = findViewById(R.id.title_peacefulPiano);
                String playlist = tv.getText().toString();
                intent.putExtra("PLAYLIST NAME",playlist);
                intent.putExtra("PLAYLIST ICON",R.drawable.m1_peacefulpiano_ic);

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
                Intent intent = new Intent(M1_MainActivity.this, M1_VideoPlaylistInside.class);
                startActivity(intent);
            }
        });

        //store data to display
        storeDisplayData();

        adapter = new M1_Adapter(M1_MainActivity.this, pl_title,this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(M1_MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mdh = new M1_DatabaseHelper(M1_MainActivity.this);
        pl_title = new ArrayList<>();

        storeDisplayData();

        adapter = new M1_Adapter(M1_MainActivity.this, pl_title,this);
        rv.setAdapter(adapter);
    }

    void storeDisplayData(){
        Cursor cursor = mdh.readPlaylistNameData();

        if(cursor.getCount() == 0){

        }
        else{
            cursor.moveToPosition(3);
            while(cursor.moveToNext()){
                String str = cursor.getString(0);
                pl_title.add(new M1_PlaylistModel(str));
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
        Intent intent = new Intent(M1_MainActivity.this, M1_PlaylistInside.class);

        String title = pl_title.get(position).getPlaylistTitle();
        intent.putExtra("PLAYLIST NAME",title);
        if(position==0) //liked song playlist
            intent.putExtra("PLAYLIST ICON",R.drawable.m1_likedsong_ic);
        else if(position==1) //Be Peaceful playlist
            intent.putExtra("PLAYLIST ICON",R.drawable.m1_urthebest_ic);
        else //other new playlist
            intent.putExtra("PLAYLIST ICON",R.drawable.m1_extra_pl_ic2);

        //fetch playlist id from database
        int id = syncPlaylistData(title);
        intent.putExtra("PLAYLIST ID",id);

        startActivity(intent);
    }
}