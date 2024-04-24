package com.gisfy.ntfp.RFO.Models;

import java.util.List;

public class FirstModel {
    private String title;
    private int image;
    private List<SecondModel> list;

    public FirstModel(String title, int image, List<SecondModel> list) {
        this.title = title;
        this.image = image;
        this.list = list;
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

    public List<SecondModel> getList() {
        return list;
    }

    public void setList(List<SecondModel> list) {
        this.list = list;
    }
}
