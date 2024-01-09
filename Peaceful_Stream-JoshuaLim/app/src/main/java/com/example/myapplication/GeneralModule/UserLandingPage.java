package com.example.myapplication.GeneralModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Adapters.ModulesRecViewAdapter;
import com.example.myapplication.CommunicationModule.ConsultationModulePage;
import com.example.myapplication.EmergencyModule.M3_EmergencyMenu;
import com.example.myapplication.EmergencyModule.utilities.Constants;
import com.example.myapplication.EmergencyModule.utilities.PreferenceManager;
import com.example.myapplication.HealthModule.M5_MainActivity;
import com.example.myapplication.Models.Module;
import com.example.myapplication.R;
import com.example.myapplication.SafetyModule.M4_Activity.M4_MainActivity;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;
import com.example.myapplication.WellnessModule.M1_MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class UserLandingPage extends AppCompatActivity implements ModulesRecViewAdapter.OnModuleListener {
    private RecyclerView modulesRecView;
    private PreferenceManager preferenceManager;
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
        username = findViewById(R.id.landingPageUsername);
        profilePic = findViewById(R.id.landingPageProfilePic);
        logoutBtn = findViewById(R.id.logoutBtn);
        auth = FirebaseAuth.getInstance();
        moduleIntent = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());

        username.setText(FirebaseUtil.currentUserUsername());
        FirebaseUtil.setCurrentUserImage(profilePic);

        ArrayList<Module> list = new ArrayList<>();
        list.add(new Module("https://firebasestorage.googleapis.com/v0/b/mad-assignment-cb1c0.appspot.com/o/NormalUser_Meditation.png?alt=media&token=1e6261a3-39a6-43b7-8ff3-bec4f4e42303" , "Wellness and\n\nRelaxation Module" , "#A3B4CB"));
        list.add(new Module("https://firebasestorage.googleapis.com/v0/b/mad-assignment-cb1c0.appspot.com/o/NormalUser_Counseling.png?alt=media&token=cdcc1229-449a-4e2f-8f6d-e8d4dccb836c" , "Communication \n\nand Consultation\n\nModule" , "#C8DDED"));
        list.add(new Module("https://firebasestorage.googleapis.com/v0/b/mad-assignment-cb1c0.appspot.com/o/NormalUser_Emergency.png?alt=media&token=f6114380-5908-41d6-a7b8-04293c16ede0" , "Emergency\n\nAssistance Module" , "#BBB5D4"));
        list.add(new Module("https://firebasestorage.googleapis.com/v0/b/mad-assignment-cb1c0.appspot.com/o/NormalUser_SafetySecurity.png?alt=media&token=acf699b4-beec-48ac-9546-e17d1705dba9" , "Safety and\n\nSecurity Module" , "#C2D7D0"));
        list.add(new Module("https://firebasestorage.googleapis.com/v0/b/mad-assignment-cb1c0.appspot.com/o/NormalUser_Health.png?alt=media&token=94930c8b-2beb-4b87-854d-3a773380a7a1" , "Health and\n\nWellbeing Module" , "#F4D3A9"));

        moduleIntent.add(new Intent(UserLandingPage.this , M1_MainActivity.class));
        moduleIntent.add(new Intent(UserLandingPage.this , ConsultationModulePage.class));
        moduleIntent.add(new Intent(UserLandingPage.this , M3_EmergencyMenu.class));
        moduleIntent.add(new Intent(UserLandingPage.this , M4_MainActivity.class));
        moduleIntent.add(new Intent(UserLandingPage.this , M5_MainActivity.class));

        ModulesRecViewAdapter adapter = new ModulesRecViewAdapter(this , this);
        adapter.setModuleList(list);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(this));

        logoutBtn.setOnClickListener(view -> {
            auth.signOut();
            signOut();
            startActivity(new Intent(UserLandingPage.this , LoginPage.class));
            finish();
        });
    }

    @Override
    public void onModuleClick(int position) {
        Intent intent = moduleIntent.get(position);
        startActivity(intent);
    }

    private void signOut(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                });
    }
}
