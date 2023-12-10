package com.example.assignments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import  android.Manifest;

import java.util.ArrayList;
import java.util.List;


public class geofencing {

    private static final int GEOFENCE_RADIUS = 100; // Radius in meters
    private Context context;

    public geofencing(Context context) {
        this.context = context;
    }



    private void setupGeofences(){

        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.readIncident(new FirebaseManager.OnIncidentReadListener() {
            @Override
            public void onIncidentRead(List<FirebaseManager.Incident> incidentList) {

                List<Geofence> geofenceList = new ArrayList<>();

                for(FirebaseManager.Incident incident:incidentList){

                    //Create a geofence for each dangerous area
                    Geofence geofence = new Geofence.Builder()
                            .setRequestId(incident.getPlace())
                            .setCircularRegion(
                                    incident.getLatitude(),
                                    incident.getLongitude(),
                                    GEOFENCE_RADIUS
                            )
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                            .build();

                    geofenceList.add(geofence);
                }

              if (!geofenceList.isEmpty()) {
                addGeofencesToClient(geofenceList);
            }
        }

            @Override
            public void onError(DatabaseError error) {
            Log.e("FirebaseManager", "Error reading incidents", error.toException());

            }
        });

}

    private void addGeofencesToClient(List<Geofence> geofenceList) {
        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(geofenceList)
                .build();

        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        PendingIntent geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() { // Replaced lambda with anonymous class
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Geofences added successfully
                            Log.d("Geofence", "Geofences added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Geofence", "Failed to add geofences", e);
                        }
                    });

        } else {
            // Handle the lack of permission
            Log.e("Geofence", "Location permission not granted");
        }
    }


    }


