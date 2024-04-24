package com.gisfy.ntfp.VSS.RequestForm;

import com.google.gson.annotations.SerializedName;

public class StocksModel {
    @SerializedName("StocksId")
    private int stocksId;
    @SerializedName("divisionname")
    private String divisionName;
    @SerializedName("RangeName")
    private String rangeName;
    @SerializedName("VSSName")
    private String vSSName;
    @SerializedName("NTFPmalayalamname")
    private String nTFPmalayalamname;
    @SerializedName("NTFPName")
    private String nTFPName;
    @SerializedName("Quantity")
    private String quantity;
    @SerializedName("Unit")
    private String unit;
    @SerializedName("DateandTime")
    private String dateandTime;
    @SerializedName("Random")
    private String random;
    @SerializedName("Collector")
    private String collector;
    @SerializedName("Amount")
    private String amount;
    @SerializedName("NTFPType")
    private String nTFPType;
    @SerializedName("NTFPTypeId")
    private int nTFPTypeId;
    @SerializedName("VSSId")
    private int vSSId;
    @SerializedName("DivisionId")
    private int divisionId;
    @SerializedName("RangeId")
    private int rangeId;
    @SerializedName("NTFPId")
    private int nTFPId;
    @SerializedName("CollectorID")
    private int collectorID;
    @SerializedName("MemberID")
    private int memberID;

    private boolean isSelected;

    public StocksModel() {
    }

    public int getStocksId() {
        return stocksId;
    }

    public void setStocksId(int stocksId) {
        this.stocksId = stocksId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getvSSName() {
        return vSSName;
    }

    public void setvSSName(String vSSName) {
        this.vSSName = vSSName;
    }

    public String getnTFPmalayalamname() {
        return nTFPmalayalamname;
    }

    public void setnTFPmalayalamname(String nTFPmalayalamname) {
        this.nTFPmalayalamname = nTFPmalayalamname;
    }

    public String getnTFPName() {
        return nTFPName;
    }

    public void setnTFPName(String nTFPName) {
        this.nTFPName = nTFPName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDateandTime() {
        return dateandTime;
    }

    public void setDateandTime(String dateandTime) {
        this.dateandTime = dateandTime;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public int getvSSId() {
        return vSSId;
    }

    public void setvSSId(int vSSId) {
        this.vSSId = vSSId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public int getnTFPId() {
        return nTFPId;
    }

    public void setnTFPId(int nTFPId) {
        this.nTFPId = nTFPId;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
