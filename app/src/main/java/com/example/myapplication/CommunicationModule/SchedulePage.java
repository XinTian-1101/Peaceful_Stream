package com.example.myapplication.CommunicationModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.Models.Session;
import com.example.myapplication.R;
import com.example.myapplication.TimePickerFragment;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SchedulePage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {
    private Button scheduleDateBtn , scheduleTimeBtn , scheduleConfirmBtn;
    private DialogFragment datePickerFragment, timePickerFragment;
    private Spinner spinner;
    private List<String> counsellorNames;
    private String selectedCounsellor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_page);
        scheduleDateBtn = findViewById(R.id.scheduleDateBtn);
        scheduleTimeBtn = findViewById(R.id.scheduleTimeBtn);
        scheduleConfirmBtn = findViewById(R.id.scheduleConfirmBtn);
        spinner = findViewById(R.id.scheduleSpinner);
        counsellorNames = new ArrayList<>();
        datePickerFragment = new DatePickerFragment();
        timePickerFragment = new TimePickerFragment();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCounsellor = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCounsellor = (String) parent.getItemAtPosition(0);
            }
        });

        FirebaseUtil.getUserReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                    for (DocumentSnapshot doc : snapshots) {
                        if (doc.getBoolean("counsellor")) counsellorNames.add(doc.getString("name"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext() , android.R.layout.simple_spinner_item , counsellorNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }
        });

        scheduleDateBtn.setOnClickListener(v -> datePickerFragment.show(getSupportFragmentManager() , "date"));
        scheduleTimeBtn.setOnClickListener(v -> timePickerFragment.show(getSupportFragmentManager() , "time"));
        scheduleConfirmBtn.setOnClickListener(v -> {
            String date , time ;
            date = scheduleDateBtn.getText().toString();
            time = scheduleTimeBtn.getText().toString();
            String sessionId = FirebaseUtil.currentUserUsername() + "_" + selectedCounsellor + "_" + date + " " + time;
            Session session = new Session(sessionId ,FirebaseUtil.currentUserUsername() , selectedCounsellor , date , time);
            FirebaseUtil.getSessionReference().document(sessionId).set(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        AndroidUtil.showToast(SchedulePage.this , "Scheduled successfully");
                        AndroidUtil.intentChg(SchedulePage.this , SessionsPage.class);
                        finish();
                    }
                }
            });
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR , year);
        calendar.set(Calendar.MONTH , month);
        calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
        scheduleDateBtn.setText(currentDate);
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
        scheduleTimeBtn.setText(hString + ":" + mString + " " + ampm);
    }
}