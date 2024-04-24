package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DFOACKModel {
    @SerializedName("PurchaseOrderNumber")
    private String purchaseOrderNumber;
    @SerializedName("StatusByRFO")
    private String statusByRFO;
    @SerializedName("Division")
    private String division;
    @SerializedName("Range")
    private String range;
    @SerializedName("VSS")
    private String vSS;
    @SerializedName("Date")
    private String date;
    @SerializedName("PCName")
    private String pCName;
    @SerializedName("NTFP")
    private List<DFONTFPModel> nTFP;

    public DFOACKModel() {
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
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

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getvSS() {
        return vSS;
    }

    public void setvSS(String vSS) {
        this.vSS = vSS;
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

    public List<DFONTFPModel> getnTFP() {
        return nTFP;
    }

    public void setnTFP(List<DFONTFPModel> nTFP) {
        this.nTFP = nTFP;
    }
}