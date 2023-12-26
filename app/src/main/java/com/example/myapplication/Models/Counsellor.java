package com.example.myapplication.Models;

import android.net.Uri;

import java.util.List;

public class Counsellor extends User{
    private User user;
    private String name , position , description , workingTime;
    private List<String> workingDays;
    private boolean isCounsellor;

    public Counsellor() {
    }

    public Counsellor(User user, String name, String position, String description, String workingTime, List<String> workingDays, boolean isCounsellor) {
        this.user = user;
        this.name = name;
        this.position = position;
        this.description = description;
        this.workingTime = workingTime;
        this.workingDays = workingDays;
        this.isCounsellor = isCounsellor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public List<String> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(List<String> workingDays) {
        this.workingDays = workingDays;
    }

    public boolean isCounsellor() {
        return isCounsellor;
    }

    public void setCounsellor(boolean counsellor) {
        isCounsellor = counsellor;
    }
}
