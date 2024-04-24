package com.gisfy.ntfp.RFO.Models;

public class Model_inventory {
    private String uid ;
    private String vss ;
    private String division ;
    private String range ;
    private String product ;
    private String measurement ;
    private String quantity ;
    private String date ;
    boolean isSelected;
    private int status;

    public Model_inventory(String uid, String vss, String division, String range, String product, String measurement, String quantity, String date, int status) {
        this.uid = uid;
        this.vss = vss;
        this.division = division;
        this.range = range;
        this.product = product;
        this.measurement = measurement;
        this.quantity = quantity;
        this.date = date;
        this.status = status;
    }

    public Model_inventory() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVss() {
        return vss;
    }

    public void setVss(String vss) {
        this.vss = vss;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
