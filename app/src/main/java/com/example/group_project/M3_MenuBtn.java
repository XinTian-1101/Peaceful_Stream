package com.example.group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class M3_MenuBtn extends AppCompatActivity {

    Button btnEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m3_activity_menu_btn);

        btnEnter = findViewById(R.id.button);
        btnEnter.setOnClickListener((View v) -> {
                openEmergencyMenu();
        });
    }

    public void openEmergencyMenu(){
        Intent intent = new Intent(this, M3_EmergencyMenu.class);
        startActivity(intent);
    }
}