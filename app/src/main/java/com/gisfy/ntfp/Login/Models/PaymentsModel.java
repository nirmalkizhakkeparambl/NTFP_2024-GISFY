package com.gisfy.ntfp.Login.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentsModel implements Serializable {

    @SerializedName("VSS")
    private String vSS;
    @SerializedName("Division")
    private String division;
    @SerializedName("Range")
    private String range;
    @SerializedName("Amount")
    private String amount;
    @SerializedName("DateTime")
    private String dateTime;
    @SerializedName("ReceivedFrom")
    private String receivedFrom;
    @SerializedName("SocietyName")
    private String societyName;
    @SerializedName("ThirdParty")
    private String thirdParty;
    @SerializedName("Random")
    private String random;
    @SerializedName("PaymentStatus")
    private String paymentStatus;
    @SerializedName("PaymentType")
    private String paymentType;
    @SerializedName("Product")
    private String product;
    @SerializedName("QuantityMeasurement")
    private String quantityMeasurement;
    @SerializedName("Quantity")
    private String quantity;

    public PaymentsModel() {
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantityMeasurement() {
        return quantityMeasurement;
    }

    public void setQuantityMeasurement(String quantityMeasurement) {
        this.quantityMeasurement = quantityMeasurement;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
