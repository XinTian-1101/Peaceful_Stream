package com.example.myapplication.Models;

public class Module {
    private String imgSrc;
    private String moduleName , colour;

    public Module(String imgSrc, String moduleName, String colour) {
        this.imgSrc = imgSrc;
        this.moduleName = moduleName;
        this.colour = colour;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
