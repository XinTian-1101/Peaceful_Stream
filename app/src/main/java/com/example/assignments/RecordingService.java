package com.example.assignments;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;


public class RecordingService extends Service {


    MediaRecorder mediaRecorder;
    long StartingTimeMillis=0;
    long ElapseMillis=0;

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "MySoundRecPrefs";
    private static final String RECORDING_COUNT_KEY = "recordingCountKey";
    private int recordingCount;

    File file;
    String fileName;
    DBHelper dbHelper;

    @Override
    public void onCreate(){

        super.onCreate();
        dbHelper = new DBHelper(getApplicationContext());
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        recordingCount = prefs.getInt(RECORDING_COUNT_KEY, 0); // Retrieve the last recording count

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       startRecording();
       return START_STICKY;
    }


    public void startRecording(){

        recordingCount++; // Increment the count
        prefs.edit().putInt(RECORDING_COUNT_KEY, recordingCount).apply(); // Store the new count

        fileName = "MyRecording_" + recordingCount + ".mp3";

        file = new File(Environment.getExternalStorageDirectory() + "/MySoundRec/" + fileName);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);


        try{

            mediaRecorder.prepare();
            mediaRecorder.start();

            StartingTimeMillis= System.currentTimeMillis();

        }catch(IOException e){
            e.printStackTrace();

        }

    }



    private void stopRecording(){
        mediaRecorder.stop();
        ElapseMillis=(System.currentTimeMillis()-StartingTimeMillis);
        mediaRecorder.release();
        Toast.makeText(getApplicationContext(),"Recording saved"+file.getAbsolutePath(),Toast.LENGTH_LONG).show();

        //Add into database
        RecordingItem recordingItem = new RecordingItem(fileName,file.getAbsolutePath(),ElapseMillis,System.currentTimeMillis());
        dbHelper.addRecording(recordingItem);


    }

    @Override
    public void onDestroy() {

        if(mediaRecorder!=null){
            stopRecording();
        }
        super.onDestroy();
    }
}
