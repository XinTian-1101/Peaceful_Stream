package com.example.assignments.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.assignments.R;
import com.example.assignments.Service.RecordingService;
import com.example.assignments.databinding.FragmentRecordBinding;
import java.io.File;
import android.Manifest;


public class RecordFragment extends Fragment {

    // Binding object instance with access to the views in the fragment_record.xml layout
    private FragmentRecordBinding binding;

    private Intent recordingServiceIntent;
    private boolean isStartRecord = true;
    private boolean isPause = true;
    long timeWhenPaused =0;

    private static final int REQUEST_EXTERNAL_STORAGE=200;

    private static String[]PERMISSION_STORAGE={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordingServiceIntent = new Intent(getActivity(), RecordingService.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnPause.setVisibility(View.GONE);
        binding.recordBtn.setColorPressed(R.color.record_pressed_color);

        binding.recordBtn.setOnClickListener(v -> {
            if (isStartRecord) {
                if (checkStoragePermissions()) {
                    startRecording();
                } else {
                    requestStoragePermissions();
                }
            } else {
                stopRecording();
            }
            isStartRecord = !isStartRecord;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Toast.makeText(getContext(), "Permission to access storage was denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return permission == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
        } else {
            requestPermissions(PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }





    private void startRecording() {

            // Start the service with the Intent when recording starts
            getActivity().startService(recordingServiceIntent);
            binding.recordBtn.setImageResource(R.drawable.ic_media_stop);
            Toast.makeText(getContext(),"Recroding started",Toast.LENGTH_LONG).show();

            File folder = new File(Environment.getExternalStorageDirectory()+"/MySoundRec");

            if(!folder.exists()){
                folder.mkdir();
            }

            binding.chronometer.setBase(SystemClock.elapsedRealtime());
            binding.chronometer.start();

            //let the screen on when the user record
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            binding.recordingStatusText.setText("Recording...");

        }


    private void stopRecording() {
        binding.recordBtn.setImageResource(R.drawable.ic_mic);
        binding.chronometer.stop();
        binding.chronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenPaused=0;
        binding.recordingStatusText.setText("Tap the button to start the recording");

        // Stop the service with the Intent when recording stops
        getActivity().stopService(recordingServiceIntent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (checkStoragePermissions()) {
                startRecording();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }


}
