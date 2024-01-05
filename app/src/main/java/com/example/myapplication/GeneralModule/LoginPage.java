package com.example.myapplication.GeneralModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.EmergencyModule.utilities.PreferenceManager;
import com.example.myapplication.databinding.M3ActivitySignInBinding;
import com.example.myapplication.EmergencyModule.utilities.Constants;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity{
    private TextView signUpTxt , fgtPass;
    private Button signInBtn;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private FirebaseAuth mAuth;
    private M3ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    SpannableString spanStr;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUtil.userAuth(LoginPage.this , this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        signUpTxt = findViewById(R.id.signUpTxt);
        fgtPass = findViewById(R.id.fgtPass);
        signInBtn = findViewById(R.id.signInBtn);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = M3ActivitySignInBinding.inflate(getLayoutInflater());
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
                AndroidUtil.intentChg(LoginPage.this , SignUpAsPage.class);
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
            if (!AndroidUtil.isValidEmailFormat(email , LoginPage.this)) return;
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            signIn();
                            FirebaseUtil.userAuth(LoginPage.this , this);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        fgtPass.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
            View fgtPassView = getLayoutInflater().inflate(R.layout.forgot_password_dialog , null);
            EditText fgtPassEmail = fgtPassView.findViewById(R.id.forgotPasswordEmail);

            builder.setView(fgtPassView);
            AlertDialog dialog = builder.create();
            dialog.show();

            fgtPassView.findViewById(R.id.forgotPasswordCancel).setOnClickListener(view -> dialog.dismiss());
            fgtPassView.findViewById(R.id.forgotPasswordConfirm).setOnClickListener(view -> {
                String email = fgtPassEmail.getText().toString();
                if (!AndroidUtil.isValidEmailFormat(email , LoginPage.this)) return;
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AndroidUtil.showToast(LoginPage.this , "Please check your email");
                            dialog.dismiss();
                        }
                        else AndroidUtil.showToast(LoginPage.this , "Failed to send password reset email");
                    }
                });
            });
        });
    }

    public void signIn(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,emailInput.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,passwordInput.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null
                            &&  task.getResult().getDocuments().size()>0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        AndroidUtil.logMsg("user id" , preferenceManager.getString(Constants.KEY_USER_ID));
                    }
                });
    }
}