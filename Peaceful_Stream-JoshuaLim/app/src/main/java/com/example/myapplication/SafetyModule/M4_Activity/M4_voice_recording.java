package com.example.myapplication.SafetyModule.M4_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.example.myapplication.SafetyModule.M4_Adapter.M4_MyTabAdapter;
import com.example.myapplication.R;
import com.example.myapplication.R.id;

public class M4_voice_recording extends AppCompatActivity {

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m4_voice_recording);

        PagerSlidingTabStrip  pagerSlidingTabStrip = findViewById(id.pagerSliding);
        ViewPager  viewpager = findViewById(id.pager);
        toolbar = findViewById(R.id.toolBar);
        FrameLayout frameLayout = findViewById(id.container);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

        viewpager.setAdapter(new M4_MyTabAdapter(getSupportFragmentManager()));
        pagerSlidingTabStrip.setViewPager(viewpager);

    }

}
