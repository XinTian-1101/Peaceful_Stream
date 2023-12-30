package com.example.myapplication.GeneralModule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.CommunicationModule.ChatUsersPage;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;

public class CounsellorLandingPage extends AppCompatActivity {
    private ImageView counsellorProfilePic;
    private TextView counsellorName;
    private RelativeLayout counsellorModule;
    private Button counsellorLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_landing_page);
        counsellorProfilePic = findViewById(R.id.counsellorLandingPageProfilePic);
        counsellorName = findViewById(R.id.counsellorLandingPageUsername);
        counsellorModule = findViewById(R.id.counsellorModule);
        counsellorLogoutBtn = findViewById(R.id.counsellorLogoutBtn);

        counsellorName.setText(FirebaseUtil.currentUserUsername());
        FirebaseUtil.setImage(counsellorProfilePic , FirebaseUtil.currentUserUsername());

        counsellorLogoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AndroidUtil.intentChg(this , LoginPage.class);
            finish();
        });

        counsellorModule.setOnClickListener(v -> {
            AndroidUtil.intentChg(this , ChatUsersPage.class);
        });
    }
}