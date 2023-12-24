package com.example.assignments.M4_Item;

import java.io.Serializable;

public class M4_RecordingItem implements Serializable {

    private String name,path;
    private long length,time_added;
    private long id;

    public M4_RecordingItem(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public M4_RecordingItem(String name, String path, long length, long time_added) {
        this.id=id;
        this.name = name;
        this.path = path;
        this.length = length;
        this.time_added = time_added;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getTime_added() {
        return time_added;
    }

    public void setTime_added(long time_added) {
        this.time_added = time_added;
    }
}
