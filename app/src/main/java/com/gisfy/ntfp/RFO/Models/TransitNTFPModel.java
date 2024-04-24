package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

public class TransitNTFPModel {

    @SerializedName("NTFPName")
    private String nTFPName;
    @SerializedName("NTFPType")
    private String nTFPType;
    @SerializedName("NTFPTypeId")
    private int nTFPTypeId;
    @SerializedName("NTFPId")
    private int nTFPId;
    @SerializedName("NTFPmalayalamname")
    private String nTFPmalayalamname;
    @SerializedName("Unit")
    private String unit;
    @SerializedName("Quantity")
    private String quantity;
    @SerializedName("StocksId")
    private int stocksId;
    @SerializedName("MemberID")
    private int memberID;


    public TransitNTFPModel() {
    }

    public TransitNTFPModel(String nTFPName, String nTFPmalayalamname, String unit, String quantity, int memberID) {
        this.nTFPName = nTFPName;
        this.nTFPmalayalamname = nTFPmalayalamname;
        this.unit = unit;
        this.quantity = quantity;
        this.memberID = memberID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getnTFPName() {
        return nTFPName;
    }

    public void setnTFPName(String nTFPName) {
        this.nTFPName = nTFPName;
    }

    public String getnTFPType() {
        return nTFPType;
    }

    public void setnTFPType(String nTFPType) {
        this.nTFPType = nTFPType;
    }

    public int getnTFPTypeId() {
        return nTFPTypeId;
    }

    public void setnTFPTypeId(int nTFPTypeId) {
        this.nTFPTypeId = nTFPTypeId;
    }

    public int getnTFPId() {
        return nTFPId;
    }

    public void setnTFPId(int nTFPId) {
        this.nTFPId = nTFPId;
    }

    public String getnTFPmalayalamname() {
        return nTFPmalayalamname;
    }

    public void setnTFPmalayalamname(String nTFPmalayalamname) {
        this.nTFPmalayalamname = nTFPmalayalamname;
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

    public int getStocksId() {
        return stocksId;
    }

    public void setStocksId(int stocksId) {
        this.stocksId = stocksId;
    }
}
