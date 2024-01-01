package com.example.test;

import com.google.firebase.database.PropertyName;

public class M5_Counsellor {

    private String Name;

    @PropertyName("Working Experience")
    private String workingExperience; // Use a different variable name

    private String University;
    private String Language;

    private int Star;

    private int ID;

    private String imageLink;

    public M5_Counsellor() {
        // Required empty public constructor
    }

    // Getter methods for retrieving data
    public String getName() {
        return Name;
    }

    @PropertyName("Working Experience")
    public String getWorkingExperience() {
        return workingExperience;
    }

    public String getUniversity() {
        return University;
    }

    public String getLanguage() {
        return Language;
    }

    public int getStar() {
        return Star;
    }

    public int getID() {
        return ID;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
