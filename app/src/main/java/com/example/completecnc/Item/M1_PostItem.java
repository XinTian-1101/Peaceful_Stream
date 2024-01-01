package com.example.completecnc.Item;

public class M1_PostItem {
    String name;
    String imageUrl;

    public M1_PostItem() {
        //empty constructor needed
    }

    public M1_PostItem(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "";
        }

        this.name = name;
        this.imageUrl = imageUrl;
    }
    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
    }

}