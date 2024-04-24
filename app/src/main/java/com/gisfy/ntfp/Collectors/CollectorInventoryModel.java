package com.gisfy.ntfp.Collectors;

public class CollectorInventoryModel {

    private String UID;
    private String VSS;
    private String Division;
    private String Range;
    private String Name;
    private String NTFP;
    private String NTFPType;
    private String Unit;
    private String Quantity;
    private String loseAmound="";
    private String Date;
    private int PayStatus;
    private int VSSStatus;
    private int Synced;
    private boolean isSelected=false;
    private int memberId;

    public CollectorInventoryModel() {
    }

    public CollectorInventoryModel(int memberId,String UID, String VSS, String division, String range, String name, String NTFP, String NTFPType, String unit, String quantity, String date, int payStatus, int synced) {
        this.UID = UID;
        this.memberId = memberId;
        this.VSS = VSS;
        Division = division;
        Range = range;
        Name = name;
        this.NTFP = NTFP;
        this.NTFPType = NTFPType;
        Unit = unit;
        Quantity = quantity;
        Date = date;
        PayStatus = payStatus;
        Synced = synced;
    }

    public int getVSSStatus() {
        return VSSStatus;
    }

    public void setVSSStatus(int VSSStatus) {
        this.VSSStatus = VSSStatus;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(int payStatus) {
        PayStatus = payStatus;
    }

    public int getSynced() {
        return Synced;
    }

    public void setSynced(int synced) {
        Synced = synced;
    }

    public String getNTFPType() {
        return NTFPType;
    }

    public void setNTFPType(String NTFPType) {
        this.NTFPType = NTFPType;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getVSS() {
        return VSS;
    }

    public void setVSS(String VSS) {
        this.VSS = VSS;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getRange() {
        return Range;
    }

    public void setRange(String range) {
        Range = range;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNTFP() {
        return NTFP;
    }

    public void setNTFP(String NTFP) {
        this.NTFP = NTFP;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}
