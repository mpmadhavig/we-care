package com.project.wecare.models;

public class Image {
    private Integer defaultImage;
    private String imagePath;

    public Image(Integer defaultImage) {
        this.defaultImage = defaultImage;
    }

    public Integer getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(Integer defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
