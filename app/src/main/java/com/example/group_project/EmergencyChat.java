package com.example.group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.group_project.activties.ChatActivity;

public class EmergencyChat extends AppCompatActivity {
    private ImageButton ambulanceChatBtn, policeChatBtn, fireBrigadeChatBtn, disasterChatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_chat);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_darkblue)));
        getSupportActionBar().setTitle("Emergency Services Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ambulanceChatBtn =  findViewById(R.id.chatButton1);
        policeChatBtn = findViewById(R.id.chatButton2);
        fireBrigadeChatBtn = findViewById(R.id.chatButton3);
        disasterChatBtn = findViewById(R.id.chatButton4);

        ambulanceChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAmbulanceEmergencyChat();
            }
        });
    }
    public void openAmbulanceEmergencyChat(){
        Intent intentChat = new Intent(this, ChatActivity.class);
        startActivity(intentChat);
    }
}