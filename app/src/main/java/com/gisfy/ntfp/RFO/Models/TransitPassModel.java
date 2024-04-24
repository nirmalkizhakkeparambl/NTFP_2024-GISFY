package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransitPassModel {
    @SerializedName("TransUniqueId")
    private String transUniqueId;
    @SerializedName("divisionname")
    private String divisionName;
    @SerializedName("DivisionId")
    private int divisionId;
    @SerializedName("RangeName")
    private String rangeName;
    @SerializedName("RangeId")
    private int rangeId;
    @SerializedName("RFOName")
    private String rFOName;
    @SerializedName("VSSName")
    private String vSSName;
    @SerializedName("VSSId")
    private int vSSId;
    @SerializedName("Date")
    private String date;
    @SerializedName("PCName")
    private String pCName;
    @SerializedName("PcId")
    private int pcId;
    @SerializedName("TransitStatus")
    private String transitStatus;
    @SerializedName("ShipmentStatus")
    private String shipmentStatus;
    @SerializedName("FCMID")
    private String fCMID;
    @SerializedName("CollectorName")
    private String collectorName;
    @SerializedName("CollectorID")
    private int collectorID;
    @SerializedName("MemberID")
    private int memberID;
    @SerializedName("NTFP")
    private List<TransitNTFPModel> nTFP;

    public TransitPassModel() {
    }

    public String getTransUniqueId() {
        return transUniqueId;
    }

    public void setTransUniqueId(String transUniqueId) {
        this.transUniqueId = transUniqueId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public String getrFOName() {
        return rFOName;
    }

    public void setrFOName(String rFOName) {
        this.rFOName = rFOName;
    }

    public String getvSSName() {
        return vSSName;
    }

    public void setvSSName(String vSSName) {
        this.vSSName = vSSName;
    }

    public int getvSSId() {
        return vSSId;
    }

    public void setvSSId(int vSSId) {
        this.vSSId = vSSId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getpCName() {
        return pCName;
    }

    public void setpCName(String pCName) {
        this.pCName = pCName;
    }

    public int getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }

    public String getTransitStatus() {
        return transitStatus;
    }

    public void setTransitStatus(String transitStatus) {
        this.transitStatus = transitStatus;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getfCMID() {
        return fCMID;
    }

    public void setfCMID(String fCMID) {
        this.fCMID = fCMID;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public int getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(int collectorID) {
        this.collectorID = collectorID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public List<TransitNTFPModel> getnTFP() {
        return nTFP;
    }

    public void setnTFP(List<TransitNTFPModel> nTFP) {
        this.nTFP = nTFP;
    }
}