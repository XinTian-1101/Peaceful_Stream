package com.example.assignments.Helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.assignments.Activity.crime_map;
import com.example.assignments.Database.FirebaseManager;
import com.example.assignments.GeofenceBroadcastReceiver;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class GeofenceHelper extends ContextWrapper {

    public static final String Tag = "GeofenceHelper";
    PendingIntent pendingIntent;


    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList) {

        return new GeofencingRequest.Builder()
                .addGeofences(geofenceList)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }


    public Geofence getGeofence(String ID, LatLng latlng, float radius, int transitionTypes){

        return new Geofence.Builder()
                .setCircularRegion(latlng.latitude, latlng.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }


    public PendingIntent getPendingIntent(){

        if(pendingIntent!=null){
            return  pendingIntent;
        }

        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,2607, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }




    }




