package com.example.myapplication.EmergencyModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.EmergencyModule.activties.M3_MainActivity;
import com.example.myapplication.GeneralModule.UserLandingPage;
import com.example.myapplication.R;

public class M3_EmergencyMenu extends AppCompatActivity {

    private Button btnSOS;
    private Button btnLocate;
    private Button btnChat;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m3_emergency_menu);

        toolbar = findViewById(R.id.toolBar);
        btnSOS = findViewById(R.id.emergencyDailButton);
        btnLocate = findViewById(R.id.shareLocationButton);
        btnChat = findViewById(R.id.emergencyChatButton);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

        btnSOS.setOnClickListener((View v) -> {
                openEmergencyDialing();
        });

        btnLocate.setOnClickListener((View v) -> {
                openRealTimeLocation();
        });

        btnChat.setOnClickListener((View v)-> {
                openEmergencyChat();
        });
    }

    public void openEmergencyDialing(){
        Intent intentDialing = new Intent(this, M3_EmergencyDialingList.class);
        startActivity(intentDialing);
    }

    public void openRealTimeLocation(){
        Intent intentLocate = new Intent(this, M3_RealTimeLocation.class);
        startActivity(intentLocate);
    }

    public void openEmergencyChat(){
        Intent intentChat = new Intent(this, M3_MainActivity.class);
        startActivity(intentChat);
    }

}