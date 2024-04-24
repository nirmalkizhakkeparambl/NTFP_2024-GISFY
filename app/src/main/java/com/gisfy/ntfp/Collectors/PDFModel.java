package com.gisfy.ntfp.Collectors;

public class PDFModel {
    private int image;
    private String uri;
    private String Title;
    private String subtitle;

    public PDFModel(int image, String uri, String title, String subtitle) {
        this.image = image;
        this.uri = uri;
        Title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
