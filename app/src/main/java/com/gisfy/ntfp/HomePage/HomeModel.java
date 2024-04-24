package com.gisfy.ntfp.HomePage;

public class HomeModel {
    private String title;
    private int image;
    private Class activity;
    private boolean selected;
    private String iddata;

    public HomeModel(String title, int image, Class activity, boolean selected,String iddata) {
        this.title = title;
        this.image = image;
        this.activity = activity;
        this.selected = selected;
        this.iddata = iddata;
    }

    public String getIddata() {
        return iddata;
    }

    public void setIddata(String iddata) {
        this.iddata = iddata;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
