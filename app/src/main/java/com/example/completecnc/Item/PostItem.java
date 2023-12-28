package com.example.completecnc.Item;

public class PostItem {
    String name;
    String imageUrl;

    public PostItem() {
        //empty constructor needed
    }

    public PostItem(String name, String imageUrl) {
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