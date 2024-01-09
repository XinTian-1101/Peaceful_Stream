package com.example.myapplication.CommunicationModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Adapters.CounsellorRecViewAdapter;
import com.example.myapplication.Models.Counsellor;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.EventListeners;

import java.util.ArrayList;
import java.util.List;

public class ChatCounsellorsPage extends AppCompatActivity implements RecViewClickListener {
    private RecyclerView counsellorsRecyclerView;
    private List<Counsellor> counsellorList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_counsellors_page);
        counsellorsRecyclerView = findViewById(R.id.counsellorsRecyclerView);
        toolbar = findViewById(R.id.viewCounsellorsToolbar);
        AndroidUtil.setToolbar(this , toolbar);
        counsellorList = new ArrayList<>();

        CounsellorRecViewAdapter adapter = new CounsellorRecViewAdapter(counsellorList , this , this);
        counsellorsRecyclerView.setAdapter(adapter);
        counsellorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        EventListeners.CounsellorChatEventChangeListener(counsellorList , adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ChatCounsellorsPage.this , ChatPage.class);
        intent.putExtra("name" , counsellorList.get(position).getName());
        intent.putExtra("id" , counsellorList.get(position).getUser().getUsername());
        startActivity(intent);
    }
}