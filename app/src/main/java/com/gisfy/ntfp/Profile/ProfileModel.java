package com.gisfy.ntfp.Profile;

public class ProfileModel {
    private String object;
    private String key;
    private int image;

    public ProfileModel(String object, String key, int image) {
        this.object = object;
        this.key = key;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
