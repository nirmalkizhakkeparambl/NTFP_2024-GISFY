package com.gisfy.ntfp.Collectors;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CollectorStockModel implements Serializable {
    @SerializedName("MemberId")
    private int memberId;
    @SerializedName("MName")
    private String mName;
    @SerializedName("InventID")
    private int inventID;
    @SerializedName("NTFP")
    private String nTFP;
    @SerializedName("NTFPType")
    private String nTFPType;
    @SerializedName("NTFPId")
    private int nTFPId;
    @SerializedName("NTFPTypeId")
    private int nTFPTypeId;
    @SerializedName("Quantity")
    private String quantity;
    @SerializedName("LoseAmound")
    private String loseAmound;
    @SerializedName("CollectorID")
    private int collectorID;

    @SerializedName("CollectorName")
    private String collectorName;
    @SerializedName("DateTime")
    private String dateTime;
    @SerializedName("RequestStatus")
    private String requestStatus;
    @SerializedName("VSSStatus")
    private String vSSStatus;
    @SerializedName("Division")
    private String division;
    @SerializedName("VSS")
    private String vSS;
    @SerializedName("Range")
    private String range;
    @SerializedName("Random")
    private String random;
    @SerializedName("Unit")
    private String unit;
    @SerializedName("NTFPType1")
    private String nTFPType1;
    @SerializedName("NTFP1")
    private String NTFP1;

    public String getNTFP1() {
        return NTFP1;
    }

    public void setNTFP1(String NTFP1) {
        this.NTFP1 = NTFP1;
    }

    private boolean isSelected;

    public String getLoseAmound() {
        return loseAmound;
    }

    public void setLoseAmound(String loseAmound) {
        this.loseAmound = loseAmound;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getInventID() {
        return inventID;
    }

    public void setInventID(int inventID) {
        this.inventID = inventID;
    }

    public String getnTFP() {
        return nTFP;
    }

    public void setnTFP(String nTFP) {
        this.nTFP = nTFP;
    }

    public String getnTFPType() {
        return nTFPType;
    }

    public void setnTFPType(String nTFPType) {
        this.nTFPType = nTFPType;
    }

    public int getnTFPId() {
        return nTFPId;
    }

    public void setnTFPId(int nTFPId) {
        this.nTFPId = nTFPId;
    }

    public int getnTFPTypeId() {
        return nTFPTypeId;
    }

    public void setnTFPTypeId(int nTFPTypeId) {
        this.nTFPTypeId = nTFPTypeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(int collectorID) {
        this.collectorID = collectorID;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getvSSStatus() {
        return vSSStatus;
    }

    public void setvSSStatus(String vSSStatus) {
        this.vSSStatus = vSSStatus;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getvSS() {
        return vSS;
    }

    public void setvSS(String vSS) {
        this.vSS = vSS;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getnTFPType1() {
        return nTFPType1;
    }

    public void setnTFPType1(String nTFPType1) {
        this.nTFPType1 = nTFPType1;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("memberId- "+ memberId)
                .append("mName- "+ mName)
                .append("inventID- "+ inventID)
                .append("nTFP- "+ nTFP)
                .append("nTFPType- "+ nTFPType)
                .append("nTFPId- "+ nTFPId)
                .append("nTFPTypeId- "+ nTFPTypeId)
                .append("quantity- "+ quantity)
                .append("collectorID- "+ collectorID)
                .append("collectorName- "+ collectorName)
                .append("dateTime- "+ dateTime)
                .append("requestStatus- "+ requestStatus)
                .append("vSSStatus- "+ vSSStatus)
                .append("division- "+ division)
                .append("vSS- "+ vSS)
                .append("range- "+ range)
                .append("random- "+ random)
                .append("unit- "+ unit)
                .append("nTFPType1- "+ nTFPType1)
                .append("nTFP1- "+ NTFP1)
                .toString();
    }
}
