package com.example.myapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
}
