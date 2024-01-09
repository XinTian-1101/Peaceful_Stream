package com.example.myapplication.SafetyModule.M4_Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.SafetyModule.M4_Helper.M4_NotificationHelper;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class M4_GeofenceBroadcastReceiver extends BroadcastReceiver {


    private static final String TAG = "GeofenceBroadcastReceiver";
    private GeofencingEvent geofencingEvent;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        M4_NotificationHelper m4NotificationHelper = new M4_NotificationHelper(context);

         geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent == null) {
            Log.d(TAG, "onReceive: GeofencingEvent is null");
            return;
        }

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }
        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();

        for(Geofence geofence:geofenceList) {

            Log.d(TAG,"onReceive: "+geofence.getRequestId());

        }

            int transactionType = geofencingEvent.getGeofenceTransition();

            switch (transactionType) {

                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    Toast.makeText(context, "You are ENTER a DANGEROUS AREA", Toast.LENGTH_SHORT).show();
                    m4NotificationHelper.sendHighPriorityNotification("","You are ENTER a DANGEROUS AREA");
                    break;


                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    Toast.makeText(context, "You are EXIT a DANGEROUS AREA", Toast.LENGTH_SHORT).show();
                    m4NotificationHelper.sendHighPriorityNotification("","You are EXIT a DANGEROUS AREA");
                    break;

            }

        }


    }


