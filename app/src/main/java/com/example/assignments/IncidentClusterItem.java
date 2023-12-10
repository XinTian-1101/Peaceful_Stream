package com.example.assignments;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class IncidentClusterItem implements ClusterItem {

    private final LatLng position;
    private final String type;
    private final String place;
    private final String date;
    private final String description;


    public IncidentClusterItem(double lat, double lng, String type, String place, String date, String description) {
        this.position = new LatLng(lat, lng);
        this.type=type;
        this.place = place;
        this.date = date;
        this.description = description;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return place;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }

    @Override
    public String toString() {
        return "IncidentClusterItem{" +
                "position=" + position +
                ", title='" + getTitle() + '\'' +
                ", snippet='" + getSnippet() + '\'' +
                ", type='" + type + '\'' +
                ", place='" + place + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getPlace() {
        return place;
    }

    public String getType(){return type;}

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }


}