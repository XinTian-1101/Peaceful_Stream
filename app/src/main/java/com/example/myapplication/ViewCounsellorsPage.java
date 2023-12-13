package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.Adapters.CounsellorRecViewAdapter;
import com.example.myapplication.Models.Counsellor;

import java.util.ArrayList;
import java.util.List;

public class ViewCounsellorsPage extends AppCompatActivity implements RecViewClickListener {
    private RecyclerView counsellorsRecyclerView;
    private List<Counsellor> counsellorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_counsellors_page);
        counsellorsRecyclerView = findViewById(R.id.counsellorsRecyclerView);
        counsellorList = new ArrayList<>();

        CounsellorRecViewAdapter adapter = new CounsellorRecViewAdapter(counsellorList , this , this);
        counsellorsRecyclerView.setAdapter(adapter);
        counsellorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {

    }
}