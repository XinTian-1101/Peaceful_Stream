package com.example.myapplication.Models;

public class Module {
    private String imgSrc;
    private String moduleName;

    public Module(String imgSrc, String moduleName) {
        this.imgSrc = imgSrc;
        this.moduleName = moduleName;
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
}
