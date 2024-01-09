package com.example.myapplication.HealthModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class M5_StressSurvey extends AppCompatActivity {

    Toolbar toolbar;

    private RadioGroup[] radioGroups = new RadioGroup[13];
    private int stressLevel = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m5_activity_stress_survey);

        toolbar = findViewById(R.id.toolBar);

        //set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);


        // Initialize your radio groups
        for (int i = 0; i < 13; i++) {
            int resId = getResources().getIdentifier("radioGroup" + (i + 1), "id", getPackageName());
            radioGroups[i] = findViewById(resId);
        }

        Button submitButton = findViewById(R.id.btnsubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all questions are answered
                boolean allQuestionsAnswered = checkAllQuestionsAnswered();

                if (!allQuestionsAnswered) {
                    // Show a toast message indicating that all questions need to be answered
                    Toast.makeText(M5_StressSurvey.this, "Please answer all questions.", Toast.LENGTH_SHORT).show();
                } else {
                    // If all questions are answered, proceed to calculate stress level and navigate
                    calculateStressLevel();
                    Intent intent;
                    if (stressLevel <= 3) {
                        intent = new Intent(M5_StressSurvey.this, M5_congratulationPage.class);
                    } else {
                        intent = new Intent(M5_StressSurvey.this, M5_alertPage.class);
                    }
                    intent.putExtra("stressLevel", stressLevel);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean checkAllQuestionsAnswered() {
        for (RadioGroup radioGroup : radioGroups) {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == -1) {
                return false; // Return false if any question is unanswered
            }
        }
        return true; // Return true if all questions are answered
    }

    private void calculateStressLevel() {
        int totalYesAnswers = 0;

        for (RadioGroup radioGroup : radioGroups) {
            int checkedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null && radioButton.getText().toString().equals("Yes")) {
                    totalYesAnswers++;
                }
            }


        if (totalYesAnswers >= 0 && totalYesAnswers <= 2) {
            stressLevel = 1;
        } else if (totalYesAnswers >= 3 && totalYesAnswers <= 5) {
            stressLevel = 2;
        } else if (totalYesAnswers >= 6 && totalYesAnswers <= 8) {
            stressLevel = 3;
        } else if (totalYesAnswers >= 9 && totalYesAnswers <= 11) {
            stressLevel = 4;
        } else {
            stressLevel = 5;
        }


    }
}