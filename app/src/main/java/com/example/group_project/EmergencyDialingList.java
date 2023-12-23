package com.example.group_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

public class EmergencyDialingList extends AppCompatActivity {

    private static final int REQUEST_CALL=1;
    private ImageButton ambulanceBtn, policeBtn, fireBrigadeBtn, disasterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_dialing_list);

        getSupportActionBar().setTitle("Emergency Dialing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ambulanceBtn =  findViewById(R.id.callButton1);
        policeBtn = findViewById(R.id.callButton2);
        fireBrigadeBtn = findViewById(R.id.callButton3);
        disasterBtn = findViewById(R.id.callButton4);

        ambulanceBtn.setOnClickListener(v -> CallButton("113"));
        policeBtn.setOnClickListener(v -> CallButton("999"));
        fireBrigadeBtn.setOnClickListener(v -> CallButton("110"));
        disasterBtn.setOnClickListener(v -> CallButton("991"));

    }

    private void CallButton(String phoneNumber) {

        if(ContextCompat.checkSelfPermission(EmergencyDialingList.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(EmergencyDialingList.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        else {
            startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel: " + phoneNumber)));

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (getCurrentFocus() == ambulanceBtn) {
                    CallButton("113");
                } else if (getCurrentFocus() == policeBtn) {
                    CallButton("999");
                } else if (getCurrentFocus() == fireBrigadeBtn) {
                    CallButton("110");
                } else if (getCurrentFocus() == disasterBtn) {
                    CallButton("991");
                }
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}