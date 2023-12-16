package com.example.group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MenuBtn extends AppCompatActivity {

    Button btnEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_btn);
        getSupportActionBar().setTitle("Emergency Assistance Module");

        btnEnter =(Button) findViewById(R.id.button);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmergencyMenu();
            }
        });
    }

    public void openEmergencyMenu(){
        Intent intent = new Intent(this, EmergencyMenu.class);
        startActivity(intent);
    }
}