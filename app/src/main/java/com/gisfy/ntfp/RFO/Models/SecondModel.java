package com.gisfy.ntfp.RFO.Models;

import android.app.Activity;

public class SecondModel {
    private String title;
    private int image;
    private Activity activity;

    public SecondModel(String title, int image, Activity activity) {
        this.title = title;
        this.image = image;
        this.activity = activity;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    public Activity getActivity() {
        return activity;
    }
}
