package com.example.myapplication.SafetyModule.M4_Activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.SafetyModule.M4_Adapter.IncidentAdapter;
import com.example.myapplication.SafetyModule.M4_Database.M4_FirebaseManager;
import com.example.myapplication.SafetyModule.M4_Helper.M4_GeofenceHelper;
import com.example.myapplication.SafetyModule.M4_Item.M4_IncidentClusterItem;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class M4_crime_map extends AppCompatActivity implements OnMapReadyCallback {

    //For map
    private static final int REQUEST_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ClusterManager<M4_IncidentClusterItem> clusterManager;

    private List<M4_IncidentClusterItem> incidentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private IncidentAdapter incidentAdapter;
    private HashMap<Marker, M4_IncidentClusterItem> markerIncidentMap = new HashMap<>();

    private GoogleMap googleMap;
    private GeofencingClient geofencingClient;
    private static final int GEOFENCE_RADIUS = 200;
    private M4_GeofenceHelper m4GeofenceHelper;
    private static final String TAG = "crime_map";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.m4_crime_map);

        toolbar = findViewById(R.id.crimeToolbar);
        AndroidUtil.setToolbar(this,toolbar);


        recyclerView = findViewById(R.id.incident_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        incidentAdapter = new IncidentAdapter(incidentList); // Create the adapter with the empty list
        recyclerView.setAdapter(incidentAdapter); // Set the adapter to the RecyclerView

        // Initialize FirebaseManager and fetch incidents
        M4_FirebaseManager m4FirebaseManager = new M4_FirebaseManager();

        M4_FirebaseManager.Incident incident1 = new M4_FirebaseManager.Incident(
                "Malicios Injury",
                "Lamiso Bar, Seventeen Mall",
                "5 November 2023",
                "During a heated argument in a bar, an individual threw a glass bottle at another patron, causing severe lacerations to the victim's face. The perpetrator was arrested for maliciously injuring the victim and is facing charges for assault and causing grievous bodily harm. ",
                3.12866,
                101.63483


        );
        String incident1Key = "0001";
        m4FirebaseManager.checkAndWriteIncident(incident1, incident1Key);

        M4_FirebaseManager.Incident incident2 = new M4_FirebaseManager.Incident(
                "Stalking",
                "Hawau Street",
                "3, 13 Sept 2023 ",
                "The street was dimly lit, with sporadic streetlights casting faint pools of light. Despite the low illumination, a suspicious figure often lurked in the shadows, particularly targeting women who walked along that route. Their behavior seemed predatory, as they'd trail closely behind or loiter in the vicinity, making the female pedestrians feel uncomfortably watched and followed along the poorly lit street. ",
                3.13005,
                101.63676


        );
        String incident2Key = "0002";
        m4FirebaseManager.checkAndWriteIncident(incident2, incident2Key);


        M4_FirebaseManager.Incident incident3 = new M4_FirebaseManager.Incident(
                "Murder",
                "Time Street",
                "2 August 2023",
                "In a residential neighborhood, a tragic murder occurred last night. An individual was found deceased in their home, having sustaultiple stab wounds. Law enforcement is actively investigating the crime scene, seeking any leads or evidence to identify the perpetrator responsible for this heinous act. ",
                3.12619,
                101.63645


        );
        String incident3Key = "0003";
        m4FirebaseManager.checkAndWriteIncident(incident3, incident3Key);


        //For map (to obtain current location)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Initialize the GeofencingClient
        geofencingClient = LocationServices.getGeofencingClient(this);
        m4GeofenceHelper = new M4_GeofenceHelper(this);
        setUpGeoference(m4FirebaseManager);

        fetchLocation();
        fetchIncidents(m4FirebaseManager);


        //For setting specific text BOLD
        TextView briefDangerousArea = findViewById(R.id.brief);
        String sourceString = "Below alert location marker is <b>DANGEROUS AREA<b> near you :";
        briefDangerousArea.setText(Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY));

        //Find the image button
        ImageButton bell_button = findViewById(R.id.circle_bell);


        //Load the shake animation
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.m4_shake);


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
                animator.setDuration(30); // duration of the animation in milliseconds
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                    mapFragment.getMapAsync(M4_crime_map.this);

                }

            }
        });

    }


    private void updateButtonBackground(boolean isOn, ImageButton button) {
        if (isOn) {
            button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.m4_switchtoggle));
        } else {
            button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.m4_switchoff));
        }
    }


    public class IncidentRenderer extends DefaultClusterRenderer<M4_IncidentClusterItem> {

        public IncidentRenderer(Context context, GoogleMap map, ClusterManager<M4_IncidentClusterItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(M4_IncidentClusterItem item, MarkerOptions markerOptions) {

            BitmapDescriptor icon = resizeMapIcon("m4_alert", 220, 240);
            markerOptions.icon(icon);
            markerOptions.title(item.getTitle());
        }
    }


    //Add the OnMapReadyCallback here
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

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


        clusterManager = new ClusterManager<M4_IncidentClusterItem>(this, googleMap);
        clusterManager.setRenderer(new IncidentRenderer(this, googleMap, clusterManager));


        // Fetch and add incidents to the ClusterManager
        fetchAndClusterIncidents();


        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);


        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<M4_IncidentClusterItem>() {
            @Override
            public boolean onClusterItemClick(M4_IncidentClusterItem M4IncidentClusterItem) {
                updateRecyclerViewWithSelectedIncident(M4IncidentClusterItem);
                Marker marker = getMarkerFromClusterItem(M4IncidentClusterItem);
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

    private Marker getMarkerFromClusterItem(M4_IncidentClusterItem item) {
        Collection<Marker> markers = clusterManager.getMarkerCollection().getMarkers();
        for (Marker marker : markers) {
            if (marker.getPosition().equals(new LatLng(item.getPosition().latitude, item.getPosition().longitude))
                    && marker.getTitle().equals(item.getTitle())) {
                return marker;
            }
        }
        return null;
    }


    private void updateRecyclerViewWithSelectedIncident(final M4_IncidentClusterItem selectedIncident) {
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
        M4_FirebaseManager m4FirebaseManager = new M4_FirebaseManager();
        m4FirebaseManager.readIncident(new M4_FirebaseManager.OnIncidentReadListener() {
            @Override
            public void onIncidentRead(List<M4_FirebaseManager.Incident> incidentList) {

                for (M4_FirebaseManager.Incident incident : incidentList) {
                    M4_IncidentClusterItem clusterItem = new M4_IncidentClusterItem(
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


    private BitmapDescriptor resizeMapIcon(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);

    }



    private void setUpGeoference(M4_FirebaseManager m4FirebaseManager) {

        m4FirebaseManager.readIncident(new M4_FirebaseManager.OnIncidentReadListener() {
            @Override
            public void onIncidentRead(List<M4_FirebaseManager.Incident> incidentList) {

                if(ContextCompat.checkSelfPermission(M4_crime_map.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    List<Geofence> geofenceList = new ArrayList<>();
                    for (M4_FirebaseManager.Incident incident : incidentList) {
                        Geofence geofence = new Geofence.Builder()
                                .setRequestId(incident.getPlace())
                                .setCircularRegion(
                                        incident.getLatitude(),
                                        incident.getLongitude(),
                                        GEOFENCE_RADIUS
                                )
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT)
                                .setLoiteringDelay(60000)
                                .build();
                        geofenceList.add(geofence);
                        System.out.println("Added all geofence");
                    }


                    GeofencingRequest geofencingRequest = m4GeofenceHelper.getGeofencingRequest(geofenceList);
                    PendingIntent pendingIntent = m4GeofenceHelper.getPendingIntent();


                    geofencingClient.addGeofences(geofencingRequest, pendingIntent)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG,"Geofence List Added");

                                }

                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    String errorMessage = m4GeofenceHelper.getErrorString(e);
                                    Log.e(TAG, "Failed to add geofences: " + e.getMessage());

                                }
                            });

    }


            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FirebaseManager", "Error reading incidents", error.toException());
            }
        });
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


    private void fetchIncidents(M4_FirebaseManager m4FirebaseManager) {
        m4FirebaseManager.readIncident(new M4_FirebaseManager.OnIncidentReadListener() {
            @Override
            public void onIncidentRead(List<M4_FirebaseManager.Incident> incidents) {
                incidentList.clear(); // Clear the list to avoid duplicate entries
                for (M4_FirebaseManager.Incident incident : incidents) {
                    // Convert FirebaseManager.Incident to IncidentClusterItem and add to incidentList
                    M4_IncidentClusterItem item = new M4_IncidentClusterItem(
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




















