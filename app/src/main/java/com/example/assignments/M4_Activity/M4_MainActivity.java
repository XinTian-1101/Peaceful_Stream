package com.example.assignments.M4_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.assignments.R;


public class M4_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m4_activity_main);


        ImageButton mapBtn = findViewById(R.id.Map_Button);
        ImageButton voiceRecordingBtn = findViewById(R.id.Voice_Button);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M4_MainActivity.this, M4_crime_map.class);
                startActivity(intent);

            }
        });


        voiceRecordingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M4_MainActivity.this, M4_voice_recording.class);
                startActivity(intent);
            }
        });

    }
}