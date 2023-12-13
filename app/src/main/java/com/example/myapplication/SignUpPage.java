package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.Playlist;
import com.example.myapplication.Models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SignUpPage extends AppCompatActivity{
    private TextView signInTxt;
    private TextInputEditText usernameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private SpannableString spanStr;
    private Button signUpBtn;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(SignUpPage.this , UserLandingPage.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        signInTxt = findViewById(R.id.signInTxt);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signUpBtn = findViewById(R.id.signUpBtn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        int startIndex = 25;
        int endIndex = 32;
        spanStr = new SpannableString(getString(R.string.sign_in));
        spanStr.setSpan(new ForegroundColorSpan(Color.WHITE) , startIndex , endIndex , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanStr.setSpan(new RelativeSizeSpan(1.5f) , startIndex , endIndex , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanStr.setSpan(new StyleSpan(Typeface.BOLD) , startIndex , endIndex , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ClickableSpan clickSignIn = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(SignUpPage.this , LoginPage.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
                ds.setUnderlineText(false);
            }
        };
        spanStr.setSpan(clickSignIn , startIndex , endIndex , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        signInTxt.setText(spanStr);
        signInTxt.setMovementMethod(LinkMovementMethod.getInstance());

        signUpBtn.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();
                            user.updateProfile(profileUpdates);
                            Toast.makeText(getApplicationContext() , "Register Successful" , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpPage.this , LoginPage.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpPage.this, "Failed to create account.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            DocumentReference doc = db.collection("Users").document(username);
            doc.set(new User(username , email));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormat.format(calendar.getTime());
            doc.collection("Playlists").document("Favourite Songs")
                    .set(new Playlist("Favourite Songs" , date , "Auto generated favourites playlist" , ""
                    , new ArrayList<>()));

        });
    }

    public String getCurrentDate () {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

}