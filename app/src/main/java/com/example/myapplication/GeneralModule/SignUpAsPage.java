package com.example.myapplication.GeneralModule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;

public class SignUpAsPage extends AppCompatActivity {
    private RelativeLayout signUpAsUser , signUpAsCounsellor;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUtil.userAuth(SignUpAsPage.this , this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_page);
        signUpAsUser = findViewById(R.id.signUpAsUser);
        signUpAsCounsellor = findViewById(R.id.signUpAsCounsellor);
        signUpAsUser.setOnClickListener(v -> AndroidUtil.intentChg(this , SignUpPage.class));
        signUpAsCounsellor.setOnClickListener(v -> AndroidUtil.intentChg(this , CounsellorDetailsPage.class));
    }
}