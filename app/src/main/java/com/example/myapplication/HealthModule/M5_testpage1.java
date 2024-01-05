package com.example.myapplication.HealthModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class M5_testpage1 extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m5_activity_testpage1);

        Button StressSurveyButton = findViewById(R.id.btnStart);
        StressSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M5_testpage1.this, M5_StressSurvey.class);
                startActivity(intent);
            }
        });

        ImageView backToMain = findViewById(R.id.back_arrow);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This will close the AbuseSurveyForm activity and go back to the MainActivity
            }
        });

    }

}