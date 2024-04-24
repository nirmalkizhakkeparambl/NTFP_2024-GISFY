package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

public class VSSNTFPModel {

    @SerializedName("total")
    public String total;
    @SerializedName("Grade1Qty")
    public String grade1Qty;
    @SerializedName("Grade2Qty")
    public String grade2Qty;
    @SerializedName("Grade3Qty")
    public String grade3Qty;
    @SerializedName("Grade1Cost")
    public String grade1Cost;
    @SerializedName("Grade2Cost")
    public String grade2Cost;
    @SerializedName("Grade3Cost")
    public String grade3Cost;
    @SerializedName("AckId")
    public int ackId;
    @SerializedName("NTFPName")
    public String nTFPName;
    @SerializedName("Quantity")
    public String quantity;
    @SerializedName("Unit")
    public String unit;
    @SerializedName("Wastage")
    public String wastage;
    @SerializedName("Grade1Qty1")
    public String grade1Qty1;
    @SerializedName("Grade2Qty1")
    public String grade2Qty1;
    @SerializedName("Grade3Qty1")
    public String grade3Qty1;

    public VSSNTFPModel() {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGrade1Qty() {
        return grade1Qty;
    }

    public void setGrade1Qty(String grade1Qty) {
        this.grade1Qty = grade1Qty;
    }

    public String getGrade2Qty() {
        return grade2Qty;
    }

    public void setGrade2Qty(String grade2Qty) {
        this.grade2Qty = grade2Qty;
    }

    public String getGrade3Qty() {
        return grade3Qty;
    }

    public void setGrade3Qty(String grade3Qty) {
        this.grade3Qty = grade3Qty;
    }

    public String getGrade1Cost() {
        return grade1Cost;
    }

    public void setGrade1Cost(String grade1Cost) {
        this.grade1Cost = grade1Cost;
    }

    public String getGrade2Cost() {
        return grade2Cost;
    }

    public void setGrade2Cost(String grade2Cost) {
        this.grade2Cost = grade2Cost;
    }

    public String getGrade3Cost() {
        return grade3Cost;
    }

    public void setGrade3Cost(String grade3Cost) {
        this.grade3Cost = grade3Cost;
    }

    public int getAckId() {
        return ackId;
    }

    public void setAckId(int ackId) {
        this.ackId = ackId;
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

    public String getWastage() {
        return wastage;
    }

    public void setWastage(String wastage) {
        this.wastage = wastage;
    }

    public String getGrade1Qty1() {
        return grade1Qty1;
    }

    public void setGrade1Qty1(String grade1Qty1) {
        this.grade1Qty1 = grade1Qty1;
    }

    public String getGrade2Qty1() {
        return grade2Qty1;
    }

    public void setGrade2Qty1(String grade2Qty1) {
        this.grade2Qty1 = grade2Qty1;
    }

    public String getGrade3Qty1() {
        return grade3Qty1;
    }

    public void setGrade3Qty1(String grade3Qty1) {
        this.grade3Qty1 = grade3Qty1;
    }
}
