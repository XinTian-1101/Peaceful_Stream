package com.example.myapplication.CommunicationModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.Adapters.SessionRecViewAdapter;
import com.example.myapplication.Models.Session;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.EventListeners;

import java.util.ArrayList;
import java.util.List;

public class SessionsPage extends AppCompatActivity {
    private RecyclerView sessionRecView;
    private List<Session> sessionList;
    private Button sessionScheduleBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_page);
        sessionRecView = findViewById(R.id.sessionRecView);
        sessionScheduleBtn = findViewById(R.id.sessionScheduleBtn);
        sessionList = new ArrayList<>();
        toolbar = findViewById(R.id.sessionsToolbar);

        AndroidUtil.setToolbar(this , toolbar);
        SessionRecViewAdapter adapter = new SessionRecViewAdapter(this , sessionList);
        sessionRecView.setAdapter(adapter);
        sessionRecView.setLayoutManager(new LinearLayoutManager(this));
        sessionScheduleBtn.setOnClickListener(v -> AndroidUtil.intentChg(SessionsPage.this , SchedulePage.class));
        EventListeners.SessionEventChangeListener(sessionList , adapter);
    }
}