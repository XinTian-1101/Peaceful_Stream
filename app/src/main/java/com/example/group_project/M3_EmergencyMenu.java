package com.example.group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.group_project.activties.M3_MainActivity;

public class M3_EmergencyMenu extends AppCompatActivity {

    private Button btnSOS;
    private Button btnLocate;
    private Button btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m3_emergency_menu);

        ImageView backarrow_menu = findViewById(R.id.back_arrow_menu);

        backarrow_menu.setOnClickListener((View v) -> {

            Intent intent = new Intent(M3_EmergencyMenu.this, M3_MenuBtn.class);
            startActivity(intent);

        });

        btnSOS = findViewById(R.id.emergencyDailButton);
        btnLocate = findViewById(R.id.shareLocationButton);
        btnChat = findViewById(R.id.emergencyChatButton);

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