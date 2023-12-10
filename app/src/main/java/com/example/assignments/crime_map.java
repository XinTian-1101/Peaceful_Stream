package com.example.assignments;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import android.animation.ObjectAnimator;



import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


//For map
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class crime_map extends AppCompatActivity implements OnMapReadyCallback {


    //For map
    private static final int REQUEST_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ClusterManager<IncidentClusterItem> clusterManager;

    private List<IncidentClusterItem> incidentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private IncidentAdapter incidentAdapter;
    private HashMap<Marker, IncidentClusterItem> markerIncidentMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.crime_map);


        //For Intent
        ImageView backarroe_map = findViewById(R.id.back_arrow_map);

        backarroe_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(crime_map.this,MainActivity.class);
                startActivity(intent);

            }
        });

        recyclerView = findViewById(R.id.incident_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        incidentAdapter = new IncidentAdapter(incidentList); // Create the adapter with the empty list
        recyclerView.setAdapter(incidentAdapter); // Set the adapter to the RecyclerView

        // Initialize FirebaseManager and fetch incidents
        FirebaseManager firebaseManager = new FirebaseManager();


        FirebaseManager.Incident incident1 = new FirebaseManager.Incident(
                "Malicios Injury",
                "Lamiso Bar, Lakin Street",
                "5 November 2023",
                "During a heated argument in a bar, an individual threw a glass bottle at another patron, causing severe lacerations to the victim's face. The perpetrator was arrested for maliciously injuring the victim and is facing charges for assault and causing grievous bodily harm. ",
                3.12841,
                101.63397


        );
        String incident1Key = "0001";
        firebaseManager.checkAndWriteIncident(incident1, incident1Key);

        FirebaseManager.Incident incident2 = new FirebaseManager.Incident(
                "Stalking",
                "Hawau Street",
                "3, 13 Sept 2023 ",
                "The street was dimly lit, with sporadic streetlights casting faint pools of light. Despite the low illumination, a suspicious figure often lurked in the shadows, particularly targeting women who walked along that route. Their behavior seemed predatory, as they'd trail closely behind or loiter in the vicinity, making the female pedestrians feel uncomfortably watched and followed along the poorly lit street. ",
                3.13005,
                101.63676


        );
        String incident2Key = "0002";
        firebaseManager.checkAndWriteIncident(incident2, incident2Key);


        FirebaseManager.Incident incident3 = new FirebaseManager.Incident(
                "Murder",
                "Time Street",
                "2 August 2023",
                "In a residential neighborhood, a tragic murder occurred last night. An individual was found deceased in their home, having sustained multiple stab wounds. Law enforcement is actively investigating the crime scene, seeking any leads or evidence to identify the perpetrator responsible for this heinous act. ",
                3.12619,
                101.63645


        );
        String incident3Key = "0003";
        firebaseManager.checkAndWriteIncident(incident3, incident3Key);


        //For map (to obtain current location)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        fetchIncidents(firebaseManager);

        //For setting specific text BOLD
        TextView briefDangerousArea = findViewById(R.id.brief);
        String sourceString = "Below alert location marker is <b>DANGEROUS AREA<b> near you :";
        briefDangerousArea.setText(Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY));

        //Find the image button
        ImageButton bell_button = findViewById(R.id.circle_bell);


        //Load the shake animation
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);


        //Find the components for switch notification
        ImageView circle_notification = findViewById(R.id.Button_forswitch);
        ImageButton button_notification = findViewById(R.id.switch_notification);

        //Situation (Switch on or off)
        Boolean[] isSwitchOn = {false};

        if (!isSwitchOn[0]) {
            bell_button.startAnimation(shake);
        }


        button_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                float positionStart = isSwitchOn[0] ? circle_notification.getWidth() : 0f;
                float positionEnd = isSwitchOn[0] ? 0f : circle_notification.getWidth();
                //isSwitchOn[0] (means on) checks the current state of the switch (on or off). If it is true (on), positionStart is set to the width of buttonForSwitch (indicating the switch is currently on the right side).
                // If false (off), positionStart is set to 0f (indicating the switch is on the left side).

                ObjectAnimator animator = ObjectAnimator.ofFloat(circle_notification, "translationX", positionStart, positionEnd);
                animator.setDuration(0); // duration of the animation in milliseconds
                animator.start();

                // Change the background drawable based on the state of the switch
                updateButtonBackground(isSwitchOn[0], button_notification);

                // Toggle the state of the switch
                isSwitchOn[0] = !isSwitchOn[0];

                if (!isSwitchOn[0]) {
                    bell_button.startAnimation(shake);
                }


            }
        });




    }

    // These method should be outside of your onCreate method

    private void fetchLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                    mapFragment.getMapAsync(crime_map.this);

                }

            }
        });

    }


    private void updateButtonBackground(boolean isOn, ImageButton button) {
        if (isOn) {
            button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.switchtoggle));
        } else {
            button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.switchoff));
        }
    }


    public class IncidentRenderer extends DefaultClusterRenderer<IncidentClusterItem> {

        public IncidentRenderer(Context context, GoogleMap map, ClusterManager<IncidentClusterItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(IncidentClusterItem item, MarkerOptions markerOptions) {

            BitmapDescriptor icon = resizeMapIcon("alert", 220, 240);
            markerOptions.icon(icon);
            markerOptions.title(item.getTitle());
        }
    }



    //Add the OnMapReadyCallback here
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Check for location permission before enabling the My Location layer
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true); // Enables the My Location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true); // Shows the My Location button on the map interface
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }


        LatLng UserlatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UserlatLng, 15));
        MarkerOptions userMarkerOptions = new MarkerOptions()
                .position(UserlatLng)
                .title("You are here !!");

        Marker userLocationMarker = googleMap.addMarker(userMarkerOptions);

        userLocationMarker.showInfoWindow();

        // Enabling zoom controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // enable pinch-to-zoom and double-tap zoom gestures
        googleMap.getUiSettings().setZoomGesturesEnabled(true);


        clusterManager = new ClusterManager<IncidentClusterItem>(this, googleMap);
        clusterManager.setRenderer(new IncidentRenderer(this, googleMap, clusterManager));

        // Fetch and add incidents to the ClusterManager
        fetchAndClusterIncidents();


        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);




        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<IncidentClusterItem>() {
            @Override
            public boolean onClusterItemClick(IncidentClusterItem incidentClusterItem) {
                updateRecyclerViewWithSelectedIncident(incidentClusterItem);
                Marker marker = getMarkerFromClusterItem(incidentClusterItem);
                if (marker != null) {
                    // Show the info window for the marker
                    marker.showInfoWindow();
                    // Center the map on the marker
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 250, null);
                }
                return true;
            }
        });
    }

    private Marker getMarkerFromClusterItem(IncidentClusterItem item) {
        Collection<Marker> markers = clusterManager.getMarkerCollection().getMarkers();
        for (Marker marker : markers) {
            if (marker.getPosition().equals(new LatLng(item.getPosition().latitude, item.getPosition().longitude))
                    && marker.getTitle().equals(item.getTitle())) {
                return marker;
            }
        }
        return null;
    }


    private void updateRecyclerViewWithSelectedIncident(final IncidentClusterItem selectedIncident) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                incidentList.clear(); // Clear the list to display only one incident
                incidentList.add(selectedIncident); // Add the selected incident
                incidentAdapter.notifyDataSetChanged(); // Notify the adapter
                recyclerView.scrollToPosition(0); // Scroll to the top as there's only one item
            }
        });
    }

    private void fetchAndClusterIncidents() {
        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.readIncident(new FirebaseManager.OnIncidentReadListener() {
            @Override
            public void onIncidentRead(List<FirebaseManager.Incident> incidentList) {

                for (FirebaseManager.Incident incident : incidentList) {
                    IncidentClusterItem clusterItem = new IncidentClusterItem(
                            incident.getLatitude(),
                            incident.getLongitude(),
                            incident.getType(),
                            incident.getPlace(),
                            incident.getDate(),
                            incident.getDescription()
                    );
                    clusterManager.addItem(clusterItem);
                }
                // This line will re-cluster the items
                clusterManager.cluster();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("crime_map", "Failed to read incidents:", error.toException());
            }
        });
    }



    private BitmapDescriptor resizeMapIcon(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName,"drawable",getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap,width,height,false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void fetchIncidents(FirebaseManager firebaseManager) {
        firebaseManager.readIncident(new FirebaseManager.OnIncidentReadListener() {
            @Override
            public void onIncidentRead(List<FirebaseManager.Incident> incidents) {
                incidentList.clear(); // Clear the list to avoid duplicate entries
                for (FirebaseManager.Incident incident : incidents) {
                    // Convert FirebaseManager.Incident to IncidentClusterItem and add to incidentList
                    IncidentClusterItem item = new IncidentClusterItem(
                            incident.getLatitude(),
                            incident.getLongitude(),
                            incident.getType(),
                            incident.getPlace(),
                            incident.getDate(),
                            incident.getDescription()
                    );
                    incidentList.add(item);
                }
                incidentAdapter.notifyDataSetChanged(); // Notify the adapter of the new data
            }

            @Override
            public void onError(DatabaseError error) {
                // Log the error or show an error message
                Log.e("crime_map", "Failed to read incidents:", error.toException());
            }
        });
    }


}




