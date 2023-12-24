package com.example.assignments.M4_Activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.example.assignments.M4_Adapter.M4_MyTabAdapter;
import com.example.assignments.R;
import com.example.assignments.R.id;

public class M4_voice_recording extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m4_voice_recording);



        PagerSlidingTabStrip  pagerSlidingTabStrip = findViewById(id.pagerSliding);
        ViewPager  viewpager = findViewById(id.pager);
        Toolbar toolbar = findViewById(id.voice_toolbar);
        FrameLayout frameLayout = findViewById(id.container);
        ImageView backArrow_voice =findViewById(id.back_arrow_voice);


        viewpager.setAdapter(new M4_MyTabAdapter(getSupportFragmentManager()));
        pagerSlidingTabStrip.setViewPager(viewpager);

        setSupportActionBar(toolbar);

        // Disable the default title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Hide the default back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        backArrow_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M4_voice_recording.this, M4_MainActivity.class);
                startActivity(intent);
            }
        });






    }






}
