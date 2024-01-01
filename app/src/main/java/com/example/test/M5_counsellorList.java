package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.test.databinding.ActivityCounsellorListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class M5_counsellorList extends AppCompatActivity  {

    private ActivityCounsellorListBinding binding;
    private DatabaseReference database;
    private M5_CounsellorAdapter counsellorAdapter;
    private ArrayList<M5_Counsellor> list;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCounsellorListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = binding.counsellorList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Counsellor");

        list = new ArrayList<>();
        counsellorAdapter = new M5_CounsellorAdapter(this, list);
        recyclerView.setAdapter(counsellorAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    M5_Counsellor counsellor = dataSnapshot.getValue(M5_Counsellor.class);
                    list.add(counsellor);
                }
                loadImagesForCounsellors();
                counsellorAdapter.notifyDataSetChanged();
                counsellorAdapter.sortListByRating();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        ImageView backToMain = findViewById(R.id.back_arrow);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This will close the AbuseSurveyForm activity and go back to the MainActivity
            }
        });

    }

    private void loadImagesForCounsellors() {
        for (M5_Counsellor counsellor : list) {
            String imageLink = counsellor.getImageLink();
            if (imageLink != null && !imageLink.isEmpty()) {
                StorageReference imageRef = storage.getReferenceFromUrl(imageLink);
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    counsellor.setImageLink(uri.toString());
                    counsellorAdapter.notifyDataSetChanged();
                }).addOnFailureListener(exception -> {
                    // Handle any errors getting the download URL for images
                });
            }
        }
    }
}