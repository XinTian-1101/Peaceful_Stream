package com.example.myapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidUtil {
    public static void showToast (Context context , String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void logMsg (String tag , String msg) {
        Log.d(tag , msg);
    }

    public static void intentChg (Context curr , Class next) {
        Intent intent = new Intent(curr , next);
        curr.startActivity(intent);
    }

    public static void setToolbar (AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static boolean isValidEmailFormat(String email , Context context) {
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) return true;
        showToast(context , "Please enter a valid email address");
        return false;
    }

    public static boolean isValidPassword (String password , Context context) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        if (!password.isEmpty() && matcher.matches()) return true;
        showToast(context , "Password must contain at least 1 uppercase letter and 1 number");
        return false;
    }

    public static boolean isFieldFilled (String input , Context context) {
        if (input.isEmpty()) {
            showToast(context , "Please fill in all fields");
            return false;
        }
        return true;
    }
}
