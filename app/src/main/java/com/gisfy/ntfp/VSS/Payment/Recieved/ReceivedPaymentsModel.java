package com.gisfy.ntfp.VSS.Payment.Recieved;

import com.google.gson.annotations.SerializedName;

public class ReceivedPaymentsModel {

    @SerializedName("ShipmentId")
    private String shipmentNumber;
    @SerializedName("PurchaseOrderNumber")
    private String purchaseordernumber;
    @SerializedName("pcpoid")
    private int pcpoid;
    @SerializedName("Division")
    private String division;
    @SerializedName("Range")
    private String range;
    @SerializedName("VSS")
    private String vSS;
    @SerializedName("PcName")
    private String pcName;
    @SerializedName("PayStatus")
    private String payStatus;
    @SerializedName("ChequeNo")
    private String chequeNo;
    @SerializedName("PaidDate")
    private String paidDate;
    @SerializedName("BankName")
    private String bankName;
    @SerializedName("IFSCcode")
    private String iFSCcode;
    @SerializedName("DFOName")
    private String dFOName;
    @SerializedName("TotalCost")
    private String amount;
    @SerializedName("ToVSS")
    private String toVSS;
    @SerializedName("ReceivedByVSS")
    private boolean receivedByVSS;
    @SerializedName("PayID")
    private int payID;

    private boolean isSelected;

    public ReceivedPaymentsModel() {
    }

    public ReceivedPaymentsModel(String shipmentNumber, String purchaseordernumber, int pcpoid, String division, String range, String vSS, String pcName, String payStatus, String chequeNo, String paidDate, String bankName, String iFSCcode, String dFOName, String amount, String toVSS, boolean receivedByVSS, int payID, boolean isSelected) {
        this.shipmentNumber = shipmentNumber;
        this.purchaseordernumber = purchaseordernumber;
        this.pcpoid = pcpoid;
        this.division = division;
        this.range = range;
        this.vSS = vSS;
        this.pcName = pcName;
        this.payStatus = payStatus;
        this.chequeNo = chequeNo;
        this.paidDate = paidDate;
        this.bankName = bankName;
        this.iFSCcode = iFSCcode;
        this.dFOName = dFOName;
        this.amount = amount;
        this.toVSS = toVSS;
        this.receivedByVSS = receivedByVSS;
        this.payID = payID;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public String getPurchaseordernumber() {
        return purchaseordernumber;
    }

    public void setPurchaseordernumber(String purchaseordernumber) {
        this.purchaseordernumber = purchaseordernumber;
    }

    public int getPcpoid() {
        return pcpoid;
    }

    public void setPcpoid(int pcpoid) {
        this.pcpoid = pcpoid;
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

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getiFSCcode() {
        return iFSCcode;
    }

    public void setiFSCcode(String iFSCcode) {
        this.iFSCcode = iFSCcode;
    }

    public String getdFOName() {
        return dFOName;
    }

    public void setdFOName(String dFOName) {
        this.dFOName = dFOName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToVSS() {
        return toVSS;
    }

    public void setToVSS(String toVSS) {
        this.toVSS = toVSS;
    }

    public boolean isReceivedByVSS() {
        return receivedByVSS;
    }

    public void setReceivedByVSS(boolean receivedByVSS) {
        this.receivedByVSS = receivedByVSS;
    }

    public int getPayID() {
        return payID;
    }

    public void setPayID(int payID) {
        this.payID = payID;
    }
}
