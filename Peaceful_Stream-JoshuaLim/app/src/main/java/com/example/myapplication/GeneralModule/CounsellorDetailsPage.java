package com.example.myapplication.GeneralModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.myapplication.GeneralModule.SignUpPage;
import com.example.myapplication.GeneralModule.UserLandingPage;
import com.example.myapplication.R;
import com.example.myapplication.TimePickerFragment;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CounsellorDetailsPage extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , View.OnClickListener {

    private Button startTimeBtn , endTimeBtn , monday , tuesday , wednesday , thursday , friday ,
            saturday , sunday , nextBtn;
    private TextInputEditText counsellorName , counsellorDesc , counsellorPosition;
    private DialogFragment timePickerFragment = new TimePickerFragment();
    private List<String> workingDays;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (FirebaseUtil.isLoggedIn()) AndroidUtil.intentChg(this , UserLandingPage.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_details_page);

        startTimeBtn = findViewById(R.id.startTimeBtn);
        endTimeBtn = findViewById(R.id.endTimeBtn);
        counsellorName = findViewById(R.id.counsellorNameInput);
        counsellorPosition = findViewById(R.id.counsellorPositionInput);
        counsellorDesc = findViewById(R.id.counsellorDescInput);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        nextBtn = findViewById(R.id.nextBtn);
        counsellorName = findViewById(R.id.counsellorNameInput);
        counsellorPosition = findViewById(R.id.counsellorPositionInput);
        counsellorDesc = findViewById(R.id.counsellorDescInput);
        workingDays = new ArrayList<>();

        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);
        sunday.setOnClickListener(this);
        nextBtn.setOnClickListener(v -> {
            String name = counsellorName.getText().toString();
            String position = counsellorPosition.getText().toString();
            String description = counsellorDesc.getText().toString();
            if (!AndroidUtil.isFieldFilled(name , CounsellorDetailsPage.this)
                    || !AndroidUtil.isFieldFilled(position , CounsellorDetailsPage.this)
                    || !AndroidUtil.isFieldFilled(description , CounsellorDetailsPage.this)) return;
            Intent intent = new Intent(this , SignUpPage.class);
            intent.putExtra("Counsellor Name" , name);
            intent.putExtra("Counsellor Position" , position);
            intent.putExtra("Counsellor Description" , description);
            intent.putExtra("Working Time" , String.format("%s - %s" , startTimeBtn.getText() ,
                    endTimeBtn.getText()));
            Bundle listExtra = new Bundle();
            listExtra.putSerializable("Working Days" , (Serializable) workingDays);
            intent.putExtra("Working Days" , listExtra);
            startActivity(intent);
        });
        startTimeBtn.setOnClickListener(v -> timePickerFragment.show(getSupportFragmentManager() , "Start Time Picker"));
        endTimeBtn.setOnClickListener(v -> timePickerFragment.show(getSupportFragmentManager() , "End Time Picker"));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hString , mString , ampm;
        if (hourOfDay < 12) ampm = "AM";
        else ampm = "PM";
        if (hourOfDay > 12) hString = String.valueOf(hourOfDay - 12);
        else hString = String.valueOf(hourOfDay);
        if (minute < 10) mString = "0" + minute;
        else mString = String.valueOf(minute);
        if (timePickerFragment.getTag().equals("Start Time Picker")) startTimeBtn.setText(hString + ":" + mString + " " + ampm);
        else endTimeBtn.setText(hString + ":" + mString + " " + ampm);
    }

    @Override
    public void onClick(View v) {
        Button dayBtn = (Button)v;
        String dayName = dayBtn.getText().toString();
        if (!workingDays.contains(dayName)) {
            workingDays.add(dayName);
            dayBtn.setBackgroundResource(R.drawable.selected_circle_button_bg);
            dayBtn.setTextColor(ContextCompat.getColor(this , R.color.kindaDarkBlue));
        }
        else {
            workingDays.remove(dayBtn.getText().toString());
            dayBtn.setBackgroundResource(R.drawable.unselected_circle_button_bg);
            dayBtn.setTextColor(ContextCompat.getColor(this , R.color.white));
        }
    }
}