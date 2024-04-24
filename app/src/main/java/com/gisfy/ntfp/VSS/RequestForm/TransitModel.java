package com.gisfy.ntfp.VSS.RequestForm;

public class TransitModel {
   private int transId;
   private String transUniqueId;
   private String division;
   private String range;
   private String vSS;
   private String nTFPName;
   private String unit;
   private String quantity;
   private String processingCenter;
   private String transitStatus;
   private String dateTime;
   private String rFOName;
   private int stocksId;
   private Object remarks;
   private String fCMID;

    public TransitModel(){

    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public String getTransUniqueId() {
        return transUniqueId;
    }

    public void setTransUniqueId(String transUniqueId) {
        this.transUniqueId = transUniqueId;
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

    public String getnTFPName() {
        return nTFPName;
    }

    public void setnTFPName(String nTFPName) {
        this.nTFPName = nTFPName;
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

    public String getProcessingCenter() {
        return processingCenter;
    }

    public void setProcessingCenter(String processingCenter) {
        this.processingCenter = processingCenter;
    }

    public String getTransitStatus() {
        return transitStatus;
    }

    public void setTransitStatus(String transitStatus) {
        this.transitStatus = transitStatus;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getrFOName() {
        return rFOName;
    }

    public void setrFOName(String rFOName) {
        this.rFOName = rFOName;
    }

    public int getStocksId() {
        return stocksId;
    }

    public void setStocksId(int stocksId) {
        this.stocksId = stocksId;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public String getfCMID() {
        return fCMID;
    }

    public void setfCMID(String fCMID) {
        this.fCMID = fCMID;
    }
}
