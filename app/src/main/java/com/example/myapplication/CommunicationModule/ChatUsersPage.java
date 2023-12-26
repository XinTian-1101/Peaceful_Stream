package com.example.myapplication.CommunicationModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Adapters.UserRecViewAdapter;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.EventListeners;

import java.util.ArrayList;
import java.util.List;

public class ChatUsersPage extends AppCompatActivity implements RecViewClickListener {
    private RecyclerView recyclerView;
    private List<User> userList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users_page);
        userList = new ArrayList<>();
        recyclerView = findViewById(R.id.chatUserRecView);
        toolbar = findViewById(R.id.chatUsersToolbar);

        AndroidUtil.setToolbar(this , toolbar);
        UserRecViewAdapter adapter = new UserRecViewAdapter(ChatUsersPage.this , userList , this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EventListeners.UserChatEventChangeListener(userList , adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ChatUsersPage.this , ChatPage.class);
        intent.putExtra("name" , userList.get(position).getUsername());
        intent.putExtra("id" , userList.get(position).getUsername());
        startActivity(intent);
    }
}