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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity{
    private TextView signUpTxt;
    private Button signInBtn;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private FirebaseAuth mAuth;
    SpannableString spanStr;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginPage.this , UserLandingPage.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        signUpTxt = findViewById(R.id.signUpTxt);
        signInBtn = findViewById(R.id.signInBtn);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        mAuth = FirebaseAuth.getInstance();
        int startIndex = 23;
        int endIndex = 30;
        spanStr = new SpannableString(getString(R.string.sign_up));
        spanStr.setSpan(new ForegroundColorSpan(Color.WHITE), startIndex , endIndex, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        spanStr.setSpan(new RelativeSizeSpan(1.5f) , startIndex , endIndex , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanStr.setSpan(new StyleSpan(Typeface.BOLD) , startIndex , endIndex , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ClickableSpan clickSignUp = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(LoginPage.this , CounsellorDetailsPage.class);
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
        spanStr.setSpan(clickSignUp , startIndex , endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        signUpTxt.setText(spanStr);
        signUpTxt.setMovementMethod(LinkMovementMethod.getInstance());

        signInBtn.setOnClickListener(view -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(LoginPage.this , UserLandingPage.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}