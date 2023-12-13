//package com.example.myapplication;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.FirebaseDatabase;
//
//public class SongsUploadPage extends AppCompatActivity {
//    private EditText songName , artist , genre , link;
//    private Button addSongBtn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_songs_upload_page);
//        songName = findViewById(R.id.songNameInput);
//        artist = findViewById(R.id.artistInput);
//        genre = findViewById(R.id.genreInput);
//        link = findViewById(R.id.linkInput);
//        addSongBtn = findViewById(R.id.addSongBtn);
//
//        addSongBtn.setOnClickListener(view -> {
//            Song newSong = new Song(songName.getText().toString() , artist.getText().toString() ,
//                    genre.getText().toString() , link.getText().toString());
//            FirebaseDatabase.getInstance("https://mad-assignment-cb1c0-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("All Songs").
//                    child(songName.getText().toString()).setValue(newSong).addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) Toast.makeText(SongsUploadPage.this ,
//                                "Song added" , Toast.LENGTH_LONG).show();
//                    }).addOnFailureListener(e -> Toast.makeText(SongsUploadPage.this,"Failed to add song" , Toast.LENGTH_LONG).show());
//        });
//    }
//}