package com.example.pestpro;

import com.google.firebase.database.Exclude;

public class uploadinfo {

    private String imageName;
    private String imageURL;
    private String date;
    private String name;
    private String mKey;


    public uploadinfo(){}

    public uploadinfo(String imageName, String imageURL, String date, String name) {
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.date = date;
        this.name = name;
    }

    public uploadinfo(String name, String url) {
        this.imageName = name;
        this.imageURL = url;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
