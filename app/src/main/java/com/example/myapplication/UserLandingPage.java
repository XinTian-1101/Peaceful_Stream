package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.myapplication.Adapters.ModulesRecViewAdapter;
import com.example.myapplication.Models.Module;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserLandingPage extends AppCompatActivity implements ModulesRecViewAdapter.OnModuleListener {
    private RecyclerView modulesRecView;
    private GridView gridView;
    private TextView username;
    private Button logoutBtn;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ArrayList<Intent> moduleIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing_page);
        modulesRecView = findViewById(R.id.modulesRecView);
        gridView = findViewById(R.id.grid);
        username = findViewById(R.id.username);
        logoutBtn = findViewById(R.id.logoutBtn);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        username.setText(user.getEmail());
        moduleIntent = new ArrayList<>();

        ArrayList<Module> list = new ArrayList<>();
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 1"));
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 2"));
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 3"));
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 4"));
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 5"));

        moduleIntent.add(new Intent(UserLandingPage.this , SongsDiscoverPage.class));
        moduleIntent.add(new Intent(UserLandingPage.this , SongsDiscoverPage.class));
        moduleIntent.add(new Intent(UserLandingPage.this , SongsDiscoverPage.class));
        moduleIntent.add(new Intent(UserLandingPage.this , SongsDiscoverPage.class));
        moduleIntent.add(new Intent(UserLandingPage.this , SongsDiscoverPage.class));

        ModulesRecViewAdapter adapter = new ModulesRecViewAdapter(this , this);
        adapter.setModuleList(list);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(this));

        String [] name = {"Emergency Dial" , "Chats" , "Careline" , "Profile"};
        int [] img = {R.drawable.bird_only_removed_bg , R.drawable.bird_only_removed_bg , R.drawable.bird_only_removed_bg , R.drawable.bird_only_removed_bg};
        GridAdapter gridAdapter = new GridAdapter(UserLandingPage.this , img , name);
        gridView.setAdapter(gridAdapter);

        logoutBtn.setOnClickListener(view -> {
            auth.signOut();
            startActivity(new Intent(UserLandingPage.this , LoginPage.class));
            finish();
        });
    }

    @Override
    public void onModuleClick(int position) {
        Intent intent = moduleIntent.get(position);
        startActivity(intent);
    }
}
