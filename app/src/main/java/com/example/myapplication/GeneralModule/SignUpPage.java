package com.example.myapplication.GeneralModule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.Counsellor;
import com.example.myapplication.Models.Playlist;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUpPage extends AppCompatActivity{
    private TextView signInTxt;
    private TextInputEditText usernameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private SpannableString spanStr;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isCounsellor = false;
    private String counsellorName , counsellorPosition , counsellorDescription , workingTime;
    private List<String> workingDays;
    private ImageView profilePictureSet;
    private Uri profilePicUri;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUtil.userAuth(SignUpPage.this , this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isCounsellor = true;
            counsellorName = extras.getString("Counsellor Name");
            counsellorPosition = extras.getString("Counsellor Position");
            counsellorDescription = extras.getString("Counsellor Description");
            workingTime =extras.getString("Working Time");
            Bundle listExtra = getIntent().getBundleExtra("Working Days");
            workingDays = (List<String>) listExtra.getSerializable("Working Days");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null) {
            profilePicUri = data.getData();
            profilePictureSet.setImageTintMode(null);
            profilePictureSet.setImageURI(profilePicUri);
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
        profilePictureSet = findViewById(R.id.profilePictureSet);
        profilePicUri = Uri.parse("https://static.vecteezy.com/system/resources/previews/008/442/086/non_2x/illustration-of-human-icon-user-symbol-icon-modern-design-on-blank-background-free-vector.jpg");
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
                AndroidUtil.intentChg(SignUpPage.this , LoginPage.class);
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

        profilePictureSet.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent , 100);
        });

        signUpBtn.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();

                            AndroidUtil.logMsg("currentUser" , user.getEmail());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    AndroidUtil.showToast(SignUpPage.this , "Register Successful. Please verify your email address");
                                                    AndroidUtil.intentChg(SignUpPage.this , LoginPage.class);
                                                }
                                            }
                                        });
                                    }
                                    else AndroidUtil.showToast(SignUpPage.this , "Could not update name");
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpPage.this, "Failed to create account.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            DocumentReference doc = db.collection("Users").document(username);
            User newUser = new User(username , email , String.valueOf(profilePicUri) , false);
            if (!isCounsellor) {
                doc.set(newUser);
            }
            else doc.set(new Counsellor(newUser , counsellorName , counsellorPosition , counsellorDescription ,
                    workingTime , workingDays , true));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormat.format(calendar.getTime());
            doc.collection("Playlists").document("Favourite Songs")
                    .set(new Playlist("Favourite Songs" , date , "Auto generated favourites playlist" , ""
                            , new ArrayList<>()));

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + username);
            storageReference.putFile(profilePicUri);
        });
    }

    public String getCurrentDate () {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

}