package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Adapters.ModulesRecViewAdapter;
import com.example.myapplication.CommunicationModule.ConsultationModulePage;
import com.example.myapplication.Models.Module;
import com.example.myapplication.MusicModule.SongsDiscoverPage;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserLandingPage extends AppCompatActivity implements ModulesRecViewAdapter.OnModuleListener {
    private RecyclerView modulesRecView;
    private GridView gridView;
    private TextView username;
    private ImageView profilePic;
    private Button logoutBtn;
    private FirebaseAuth auth;
    private ArrayList<Intent> moduleIntent;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (!FirebaseUtil.isLoggedIn()) AndroidUtil.intentChg(this , LoginPage.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing_page);
        modulesRecView = findViewById(R.id.modulesRecView);
        gridView = findViewById(R.id.grid);
        username = findViewById(R.id.landingPageUsername);
        profilePic = findViewById(R.id.landingPageProfilePic);
        logoutBtn = findViewById(R.id.logoutBtn);
        auth = FirebaseAuth.getInstance();
        moduleIntent = new ArrayList<>();

        username.setText(FirebaseUtil.currentUserUsername());
        FirebaseUtil.setCurrentUserImage(profilePic);

        ArrayList<Module> list = new ArrayList<>();
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 1"));
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 2"));
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 3"));
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 4"));
        list.add(new Module("https://d33v4339jhl8k0.cloudfront.net/docs/assets/59b173692c7d3a73488cae07/images/623338d4c1e53608cf9e9ab8/file-TfxS1urxgB.png" , "Module 5"));

        moduleIntent.add(new Intent(UserLandingPage.this , SongsDiscoverPage.class));
        moduleIntent.add(new Intent(UserLandingPage.this , ConsultationModulePage.class));
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
