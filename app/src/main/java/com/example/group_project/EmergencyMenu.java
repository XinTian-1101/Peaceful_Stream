package com.example.group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.group_project.activties.MainActivity;

public class EmergencyMenu extends AppCompatActivity {

    private Button btnSOS;
    private Button btnLocate;
    private Button btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_menu);

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_darkblue)));
//        getSupportActionBar().setTitle("Emergency Assistance Module");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSOS = (Button)findViewById(R.id.emergencyDailButton);
        btnLocate = (Button)findViewById(R.id.shareLocationButton);
        btnChat = (Button)findViewById(R.id.emergencyChatButton);

        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmergencyDialing();
            }
        });

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRealTimeLocation();
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmergencyChat();
            }
        });
    }

    public void openEmergencyDialing(){
        Intent intentDialing = new Intent(this, EmergencyDialingList.class);
        startActivity(intentDialing);
    }

    public void openRealTimeLocation(){
        Intent intentLocate = new Intent(this, RealTimeLocation.class);
        startActivity(intentLocate);
    }

    public void openEmergencyChat(){
        Intent intentChat = new Intent(this, MainActivity.class);
        startActivity(intentChat);
    }

}