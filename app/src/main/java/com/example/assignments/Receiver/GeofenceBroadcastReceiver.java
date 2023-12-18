package com.example.assignments.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.assignments.Activity.crime_map;
import com.example.assignments.Helper.NotificationHelper;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {


    private static final String TAG = "GeofenceBroadcastReceiver";
    private GeofencingEvent geofencingEvent;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        NotificationHelper notificationHelper = new NotificationHelper(context);

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
                    notificationHelper.sendHighPriorityNotification("","You are ENTER a DANGEROUS AREA");
                    break;


                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    Toast.makeText(context, "You are EXIT a DANGEROUS AREA", Toast.LENGTH_SHORT).show();
                    notificationHelper.sendHighPriorityNotification("","You are EXIT a DANGEROUS AREA");
                    break;

            }

        }


    }


