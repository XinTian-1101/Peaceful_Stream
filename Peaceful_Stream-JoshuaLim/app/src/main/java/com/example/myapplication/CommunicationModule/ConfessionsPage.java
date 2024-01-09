package com.example.myapplication.CommunicationModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.Adapters.PostRecViewAdapter;
import com.example.myapplication.Models.Post;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.EventListeners;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ConfessionsPage extends AppCompatActivity {
    private EditText confessionsPostTyping;
    private ImageButton confessionsPostAttachment , confessionsPostAdd;
    private ImageView confessionsPostImage;
    private Toolbar toolbar;
    private RecyclerView confessionsPageRecView;
    private Uri imageUri;
    private List<Post> postList;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null) {
            imageUri = data.getData();
            confessionsPostImage.setVisibility(View.VISIBLE);
            confessionsPostImage.setImageTintMode(null);
            confessionsPostImage.setImageURI(imageUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confessions_page);
        confessionsPostTyping = findViewById(R.id.confessionsPostTyping);
        confessionsPostAttachment = findViewById(R.id.confessionsPostAttachment);
        confessionsPostAdd = findViewById(R.id.confessionsPostAdd);
        confessionsPostImage = findViewById(R.id.confessionsPostImage);
        confessionsPageRecView = findViewById(R.id.confessionsPostRecView);
        toolbar = findViewById(R.id.confessionsToolbar);
        postList = new ArrayList<>();

        AndroidUtil.setToolbar(this , toolbar);
        PostRecViewAdapter adapter = new PostRecViewAdapter(this , postList);
        confessionsPageRecView.setAdapter(adapter);
        confessionsPageRecView.setLayoutManager(new LinearLayoutManager(this));
        EventListeners.PostEventChangeListener(postList , adapter);

        confessionsPostAdd.setOnClickListener(v -> {
            String content = confessionsPostTyping.getText().toString();
            if (!AndroidUtil.isFieldFilled(content , ConfessionsPage.this)) return;
            String userId = FirebaseUtil.currentUserUsername();
            Timestamp timestamp = Timestamp.now();
            String postId = userId + "_" + timestamp;
            FirebaseUtil.getPostReference().document(postId).set(new Post(postId , userId , content , timestamp))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            AndroidUtil.showToast(ConfessionsPage.this, "Post added");
                            confessionsPostTyping.setText("");
                            confessionsPostImage.setImageURI(null);
                            confessionsPostImage.setVisibility(View.GONE);
                        }
                    });
            if (imageUri != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + postId);
                storageReference.putFile(imageUri);
            }
        });

        confessionsPostAttachment.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent , 100);
        });
    }
}