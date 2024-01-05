package com.example.myapplication.HealthModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;


public class M5_MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m5_activity_main);

        Button abuseSurveyButton = findViewById(R.id.button2);
        abuseSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M5_MainActivity.this, M5_AbuseSurveyForm.class);
                startActivity(intent);
            }
        });

        Button testStressButton = findViewById(R.id.button1);
        testStressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M5_MainActivity.this, M5_testpage1.class);
                startActivity(intent);
            }
        });
    }
}