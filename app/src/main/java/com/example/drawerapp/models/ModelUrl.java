package com.example.drawerapp.models;

import com.google.firebase.database.Exclude;

public class ModelUrl {

    private String imageUrl;
    private String mKey;

    public ModelUrl(){

    }

    public ModelUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
