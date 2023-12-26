package com.example.group_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.LatLng;

public class RealTimeLocation extends AppCompatActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    FrameLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_location);

        map = findViewById(R.id.google_map);

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        ImageView backarrow_menu = findViewById(R.id.back_arrow_location);

        backarrow_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RealTimeLocation.this, EmergencyMenu.class);
                startActivity(intent);

            }
        });

        ImageButton btnShareLocation = (ImageButton) findViewById(R.id.BtnshareLocation);
        btnShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLocation();
            }
        });
    }

    private void shareLocation() {
        // Check if the current location is available
        if (currentLocation != null) {
            // Create a message with a location link
            String locationLink = "https://maps.google.com/?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
            String message = "Check out my real-time location: " + locationLink;

            // Create an intent to share the message
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);

            // Launch the default messaging app
            startActivity(Intent.createChooser(shareIntent, "Share location via"));
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }

    }
    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
            Task<Location> task = fusedClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        currentLocation = location;
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                        supportMapFragment.getMapAsync(RealTimeLocation.this);
                    }
                }

            });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap){

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
                .title("Current Location!");

        Marker userLocationMarker = googleMap.addMarker(userMarkerOptions);

        userLocationMarker.showInfoWindow();

        // Enabling zoom controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // enable pinch-to-zoom and double-tap zoom gestures
        googleMap.getUiSettings().setZoomGesturesEnabled(true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLocation();
            }else{
                Toast.makeText(this,"Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }



}