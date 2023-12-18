package com.example.assignments.Helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.example.assignments.Receiver.GeofenceBroadcastReceiver;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GeofenceHelper extends ContextWrapper {


    private static final String Tag = "GeofenceHelper";
    PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList){

        return new GeofencingRequest.Builder()
                .addGeofences(geofenceList)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();


    }


    public Geofence getGeofence(String ID, LatLng latLng, float radius, int transitionTypes){


        return  new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude,radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000) //5 seconds
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }



    //PendingIntent provides a way for the geofencing service to communicate back to your application when a geofence event happens.

    public PendingIntent getPendingIntent(){

      if(pendingIntent!=null){
          return pendingIntent;
      }

      Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
      pendingIntent = PendingIntent.getBroadcast(this,2600,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);

      return  pendingIntent;


    }


    public String getErrorString(Exception e){

        if(e instanceof ApiException){
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()){
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS:";
            }
        }

        return e.getLocalizedMessage();


    }

}
