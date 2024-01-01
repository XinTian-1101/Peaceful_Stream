package com.example.completecnc.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.completecnc.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ButtonConfession = findViewById(R.id.ButtonConfession);
        Button ButtonChat = findViewById(R.id.ButtonChat);

        ButtonConfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confession = new Intent(MainActivity.this, M1_Confession.class);
                startActivity(confession);
            }
        });

        ButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confession = new Intent(MainActivity.this, M2_ChatWUs.class);
                startActivity(confession);
            }
        });
    }
}