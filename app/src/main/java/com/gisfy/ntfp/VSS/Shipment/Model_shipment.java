package com.gisfy.ntfp.VSS.Shipment;

import java.io.Serializable;

public class Model_shipment  implements Serializable {
    private String uid ;
    private int synced;
    private String vss ;
    private String division ;
    private String range ;
    private String processing_center ;
    private String date ;
    private String transitPass ;
    private String vehicleType ;
    private String vehicleNo ;
    private boolean isSelected ;

    public Model_shipment(String uid, int synced, String vss, String division, String range, String processing_center, String date, String transitPass, String vehicleType, String vehicleNo) {
        this.uid = uid;
        this.synced = synced;
        this.vss = vss;
        this.division = division;
        this.range = range;
        this.processing_center = processing_center;
        this.date = date;
        this.transitPass = transitPass;
        this.vehicleType = vehicleType;
        this.vehicleNo = vehicleNo;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
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


    public String getProcessing_center() {
        return processing_center;
    }

    public void setProcessing_center(String processing_center) {
        this.processing_center = processing_center;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransitPass() {
        return transitPass;
    }

    public void setTransitPass(String transitPass) {
        this.transitPass = transitPass;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
