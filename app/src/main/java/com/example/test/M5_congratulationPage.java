package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class M5_congratulationPage extends AppCompatActivity {

    private EditText stressLevelEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m5_activity_congratulation_page);

        // Retrieve stress level passed from the previous activity
        int stressLevel = getIntent().getIntExtra("stressLevel", 0); // Default value is 0

        // Display the stress level information
        TextView textViewStressLevel = findViewById(R.id.textStressLevel);
        if (stressLevel == 1) {
            textViewStressLevel.setText("Safety Level (Level " + stressLevel + ", Low)");
        } else if (stressLevel == 2){
            textViewStressLevel.setText("Safety Level (Level " + stressLevel + ", Mild)");
        }else if (stressLevel == 3){
            textViewStressLevel.setText("Moderate Level (Level " + stressLevel + ", Moderate)");
        }else {
            // Handle invalid or unexpected stress levels here
            textViewStressLevel.setText("Invalid Stress Level");
        }

        ImageButton backToMain = findViewById(R.id.cancelButton);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M5_congratulationPage.this, M5_MainActivity.class);
                startActivity(intent);
            }
        });
    }
}