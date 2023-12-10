package com.example.assignments;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {

    private final DatabaseReference myDataBase;
    private ValueEventListener incidentListener;

    public FirebaseManager(){

        myDataBase = FirebaseDatabase.getInstance().getReference();

    }

    // Define an interface for callback
    public interface OnIncidentReadListener {
        void onIncidentRead(List<Incident> incidentList);
        void onError(DatabaseError error);
    }



    //Create an object and represent its data structure
    public static class Incident{

        public String type;
        public String description;
        public String date;
        public double latitude;
        public double longitude;
        public String place;



        //The comment above the default constructor in the User class indicates that Firebase Realtime Database's DataSnapshot method getValue() requires a no-argument (default) constructor to deserialize data from the database into a User object.
        //When Firebase retrieves data, it needs to instantiate the objects it's retrieving data into. If your class doesn't provide a no-argument constructor, Firebase won't know how to instantiate your object when it reads from the database.
        public Incident(){

        }


        public Incident(String type, String place, String date, String description, double latitude, double longitude){
            this.type=type;
            this.place=place;
            this.date=date;
            this.description=description;
            this.latitude=latitude;
            this.longitude=longitude;
        }


        //Below are the getters and setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPlace(){
            return place;
        }

        public void setPlace(String place){
            this.place=place;
        }

        public String getDate(){
            return date;
        }

        public void setDate(String date){
            this.date=date;
        }

        public String getDescription(){
            return description;
        }
        public void setDescription(String description){ this.description=description;}

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }


    }


    public void checkAndWriteIncident(final Incident incident, final String incidentKey) {
        DatabaseReference incidentsRef = myDataBase.child("incidents");

        incidentsRef.child(incidentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the incident already exists
                if (!snapshot.exists()) {
                    // If the incident does not exist, write it to the database
                    incidentsRef.child(incidentKey).setValue(incident);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FirebaseManager", "checkAndWriteIncident:onCancelled", error.toException());
            }
        });
    }



    public void readIncident(final OnIncidentReadListener incidentReadListener){

        DatabaseReference incidentRef = myDataBase.child("incidents");

        // An instance of ValueEventListener is created, which is an interface used by Firebase to read data from a database path continuously.
        incidentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Incident> incidentList = new ArrayList<>();
                // Get the data from the snapshot and convert it into  Incident object/class
                for(DataSnapshot incidentSnapShot: snapshot.getChildren()){

                    Incident incident = incidentSnapShot.getValue(Incident.class);

                    if (incident != null) {
                        incidentList.add(incident);
                    }
                }
                if (incidentReadListener != null) {
                    incidentReadListener.onIncidentRead(incidentList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Incident failed, log a message
                Log.w("FirebaseManager", "loadIncident:onCancelled", error.toException());
                if (incidentReadListener != null) {
                    incidentReadListener.onError(error);
                }
            }
        };

        incidentRef.addValueEventListener(incidentListener);
    }


}






