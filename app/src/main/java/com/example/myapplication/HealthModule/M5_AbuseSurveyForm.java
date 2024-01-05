package com.example.myapplication.HealthModule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class M5_AbuseSurveyForm extends AppCompatActivity  {

    private EditText editTextAns2;
    private RadioGroup radioGroup, radioGroup3, radioGroup4, radioGroup5;
    private M5_SurveyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m5_activity_abuse_survey_form);

        // Initialize your views
        editTextAns2 = findViewById(R.id.editTextAns2);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup3 = findViewById(R.id.radioGroup3);
        radioGroup4 = findViewById(R.id.radioGroup4);
        radioGroup5 = findViewById(R.id.radioGroup5);

        dbHelper = new M5_SurveyDbHelper(this);

        dbHelper = new M5_SurveyDbHelper(this);
        dbHelper.openDatabase(); // Open the database here

        Button submitButton = findViewById(R.id.button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected values and text
                String answer1 = getSelectedRadioButtonText(radioGroup);
                String answer2 = editTextAns2.getText().toString();
                String answer3 = getSelectedRadioButtonText(radioGroup3);
                String answer4 = getSelectedRadioButtonText(radioGroup4);
                String answer5 = getSelectedRadioButtonText(radioGroup5);

                // Check if any question is unanswered
                if (answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty() || answer4.isEmpty() || answer5.isEmpty()) {
                    showToast("Please answer all questions before submitting.");
                } else {
                    // Save to database
                    saveSurveyResponse(answer1, answer2, answer3, answer4, answer5);

                    // Show a toast message or perform any other action
                    showToast("Survey response saved to database!");

                    // Navigate back to MainActivity
                    Intent intent = new Intent(M5_AbuseSurveyForm.this, M5_MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish(); // Finish the current activity to prevent it from staying in the back stack
                }
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

    // Method to get selected radio button text from a radio group
    private String getSelectedRadioButtonText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
        return "";
    }

    // Method to save survey response to the database
    private void saveSurveyResponse(String answer1, String answer2, String answer3, String answer4, String answer5) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question1", answer1);
        values.put("question2", answer2);
        values.put("question3", answer3);
        values.put("question4", answer4);
        values.put("question5", answer5);

        // Inserting data
        long newRowId = db.insert("SurveyResponses", null, values);

        if (newRowId != -1) {
            showToast("Survey response saved to database!");
        } else {
            showToast("Error saving survey response!");
        }

        db.close();
    }

    // Display a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}