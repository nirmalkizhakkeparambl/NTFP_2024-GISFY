package com.gisfy.ntfp.VSS.Inventory;

import java.io.Serializable;

public class Inventory implements Serializable {
    private String uid;
    private String vss;
    private String division;
    private String range;
    private String collector;
    private String ntfp;
    private String unit;
    private String quantity;
    private String loseAmound;
    private String date;
    private String amount;
    private String ntfpType;
    private String ntfpGrade;
    private int transit;
    private int payment;
    private int synced;
    private int memberId;
    private boolean isSelected;

    public Inventory(int memberId,String uid, String vss, String division, String range, String collector, String ntfp, String unit, String quantity,String loseAmound, String date, String amount,String ntfpType,String ntfpGrade, int transit, int payment, int synced) {
        this.memberId = memberId;
        this.uid = uid;
        this.vss = vss;
        this.division = division;
        this.range = range;
        this.collector = collector;
        this.ntfp = ntfp;
        this.unit = unit;
        this.quantity = quantity;
        this.loseAmound = loseAmound;

        this.date = date;
        this.amount = amount;
        this.transit = transit;
        this.payment = payment;
        this.synced = synced;
        this.ntfpType=ntfpType;
        this.ntfpGrade=ntfpGrade;
        this.isSelected = false;
    }

    public String getLoseAmound() {
        return loseAmound;
    }

    public void setLoseAmound(String loseAmound) {
        this.loseAmound = loseAmound;
    }

    public String getNtfpGrade() {
        return ntfpGrade;
    }

    public void setNtfpGrade(String ntfpGrade) {
        this.ntfpGrade = ntfpGrade;
    }

    public String getNtfpType() {
        return ntfpType;
    }

    public void setNtfpType(String ntfpType) {
        this.ntfpType = ntfpType;
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

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getNtfp() {
        return ntfp;
    }

    public void setNtfp(String ntfp) {
        this.ntfp = ntfp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getTransit() {
        return transit;
    }

    public void setTransit(int transit) {
        this.transit = transit;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}