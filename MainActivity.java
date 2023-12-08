package com.example.cncmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ButtonConfession = findViewById(R.id.ButtonConfession);
        Button ButtonChat = findViewById(R.id.ButtonChat);
        Button ButtonSchedule = findViewById(R.id.ButtonSchedule);

        ButtonConfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confession = new Intent(MainActivity.this, ConfessionActivity.class);
                startActivity(confession);
            }
        });

        ButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(chat);
            }
        });

        ButtonSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent schedule = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(schedule);
            }
        });

    }
}
