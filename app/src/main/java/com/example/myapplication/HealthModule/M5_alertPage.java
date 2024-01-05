package com.example.myapplication.HealthModule;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CommunicationModule.ChatCounsellorsPage;
import com.example.myapplication.R;

public class M5_alertPage extends AppCompatActivity {

    private EditText stressLevelEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m5_activity_alert_page);

        TextView myTextView = findViewById(R.id.adviseUser);
        myTextView.setPaintFlags(myTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        myTextView.setTypeface(null, Typeface.BOLD);

        // Retrieve stress level passed from the previous activity
        int stressLevel = getIntent().getIntExtra("stressLevel", 0); // Default value is 0

        // Display the stress level information
        TextView textViewStressLevel = findViewById(R.id.textStressLevel2);
        if (stressLevel == 4) {
            textViewStressLevel.setText("Alert Level (Level " + stressLevel + ", High)");
        } else if (stressLevel == 5){
            textViewStressLevel.setText("Dangerous Level (Level " + stressLevel + ", Serious)");
        }else {
            // Handle invalid or unexpected stress levels here
            textViewStressLevel.setText("Invalid Stress Level");
        }



        ImageButton backToMain = findViewById(R.id.cancelButton2);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M5_alertPage.this, M5_MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public void navigateToNextPage(View view) {
        Intent intent = new Intent(this, ChatCounsellorsPage.class);
        startActivity(intent);
    }
}