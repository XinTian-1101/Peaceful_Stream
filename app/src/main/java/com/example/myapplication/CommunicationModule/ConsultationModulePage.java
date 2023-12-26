package com.example.myapplication.CommunicationModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;

public class ConsultationModulePage extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout confessionsModule , chatWithUsModule , scheduleSeesionModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_module_page);
        toolbar = findViewById(R.id.consultationModuleToolbar);
        confessionsModule = findViewById(R.id.confessionsModule);
        chatWithUsModule = findViewById(R.id.chatWithUsModule);
        scheduleSeesionModule = findViewById(R.id.scheduleSessionModule);
        AndroidUtil.setToolbar(this , toolbar);

        confessionsModule.setOnClickListener(v -> AndroidUtil.intentChg(this , ConfessionsPage.class));
        chatWithUsModule.setOnClickListener(v -> AndroidUtil.intentChg(this , ChatCounsellorsPage.class));
    }
}