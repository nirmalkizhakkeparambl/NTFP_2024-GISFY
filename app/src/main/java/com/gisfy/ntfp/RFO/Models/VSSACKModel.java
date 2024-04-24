package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VSSACKModel {
    @SerializedName("ShipmentNumber")
    public String shipmentNumber;
    @SerializedName("StatusByRFO")
    public String statusByRFO;
    @SerializedName("Division")
    public String division;
    @SerializedName("DivisionId")
    public int divisionId;
    @SerializedName("Range")
    public String range;
    @SerializedName("RangeId")
    public int rangeId;
    @SerializedName("VSSId")
    public int vSSId;
    @SerializedName("VSSName")
    public String vSSName;
    @SerializedName("Date")
    public String date;
    @SerializedName("FCMID")
    public String fCMID;
    @SerializedName("NTFP")
    public List<VSSNTFPModel> nTFP;
    
    public VSSACKModel() {
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public String getStatusByRFO() {
        return statusByRFO;
    }

    public void setStatusByRFO(String statusByRFO) {
        this.statusByRFO = statusByRFO;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public int getvSSId() {
        return vSSId;
    }

    public void setvSSId(int vSSId) {
        this.vSSId = vSSId;
    }

    public String getvSSName() {
        return vSSName;
    }

    public void setvSSName(String vSSName) {
        this.vSSName = vSSName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getfCMID() {
        return fCMID;
    }

    public void setfCMID(String fCMID) {
        this.fCMID = fCMID;
    }

    public List<VSSNTFPModel> getnTFP() {
        return nTFP;
    }

    public void setnTFP(List<VSSNTFPModel> nTFP) {
        this.nTFP = nTFP;
    }
}
