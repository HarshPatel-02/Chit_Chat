package com.example.instagram;

public class DataClass {
    String ImageURL;

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public DataClass(String imageURL) {
        ImageURL = imageURL;
    }
}
