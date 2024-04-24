package com.gisfy.ntfp.VSS.Payment;

import java.io.Serializable;

public class Model_payment implements Serializable {
    private String uid;
    private String vss;
    private String division;
    private String range;
    private String amount;
    private String date;
    private String source;
    private String sourceName;
    private String paymentType;
    private String collector;
    private String product;
    private String measurement;
    private String quantity;
    private int synced;
    private int status;
    private boolean isSelected;

    public Model_payment(String uid, String vss, String division, String range, String amount, String date, String source, String sourceName, String paymentType, String collector, String product, String measurement, String quantity, int synced, int status) {
        this.uid = uid;
        this.vss = vss;
        this.division = division;
        this.range = range;
        this.amount = amount;
        this.date = date;
        this.source = source;
        this.sourceName = sourceName;
        this.paymentType = paymentType;
        this.collector = collector;
        this.product = product;
        this.measurement = measurement;
        this.quantity = quantity;
        this.synced = synced;
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}