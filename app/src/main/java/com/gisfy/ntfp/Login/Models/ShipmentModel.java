package com.gisfy.ntfp.Login.Models;

import com.google.gson.annotations.SerializedName;

public class ShipmentModel {
    @SerializedName("ShipmentId")
    private int shipmentId;
    @SerializedName("VSS")
    private String vSS;
    @SerializedName("Division")
    private String division;
    @SerializedName("Range")
    private String range;
    @SerializedName("Product")
    private String product;
    @SerializedName("ProcessingCenter")
    private String processingCenter;
    @SerializedName("DateTime")
    private String dateTime;
    @SerializedName("TransitPass")
    private String transitPass;
    @SerializedName("Transportation")
    private String transportation;
    @SerializedName("VehicleNo")
    private String vehicleNo;
    @SerializedName("ShipmentNumber")
    private String shipmentNumber;
    @SerializedName("PcId")
    private int pcId;

    public ShipmentModel() {
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getvSS() {
        return vSS;
    }

    public void setvSS(String vSS) {
        this.vSS = vSS;
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

    public Object getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProcessingCenter() {
        return processingCenter;
    }

    public void setProcessingCenter(String processingCenter) {
        this.processingCenter = processingCenter;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTransitPass() {
        return transitPass;
    }

    public void setTransitPass(String transitPass) {
        this.transitPass = transitPass;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public int getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }
}
