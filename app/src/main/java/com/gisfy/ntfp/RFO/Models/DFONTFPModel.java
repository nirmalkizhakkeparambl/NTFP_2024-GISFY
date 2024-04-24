package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

public class DFONTFPModel {
    @SerializedName("TotalCost")
    private String totalCost;
    @SerializedName("Grade1Qty")
    private String grade1Qty;
    @SerializedName("Grade2Qty")
    private String grade2Qty;
    @SerializedName("Grade3Qty")
    private String grade3Qty;
    @SerializedName("Grade1Cost")
    private int grade1Cost;
    @SerializedName("Grade2Cost")
    private int grade2Cost;
    @SerializedName("Grade3Cost")
    private int grade3Cost;
    @SerializedName("ackId")
    private int ackId;
    @SerializedName("NTFP")
    private String nTFP;
    @SerializedName("Unit")
    private String unit;
    @SerializedName("Quantity")
    private String quantity;
    @SerializedName("CostperUnit")
    private String costperUnit;
    @SerializedName("TotalCost1")
    private String totalCost1;

    public DFONTFPModel(){

    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
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

    public int getGrade1Cost() {
        return grade1Cost;
    }

    public void setGrade1Cost(int grade1Cost) {
        this.grade1Cost = grade1Cost;
    }

    public int getGrade2Cost() {
        return grade2Cost;
    }

    public void setGrade2Cost(int grade2Cost) {
        this.grade2Cost = grade2Cost;
    }

    public int getGrade3Cost() {
        return grade3Cost;
    }

    public void setGrade3Cost(int grade3Cost) {
        this.grade3Cost = grade3Cost;
    }

    public int getAckId() {
        return ackId;
    }

    public void setAckId(int ackId) {
        this.ackId = ackId;
    }

    public String getnTFP() {
        return nTFP;
    }

    public void setnTFP(String nTFP) {
        this.nTFP = nTFP;
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

    public String getCostperUnit() {
        return costperUnit;
    }

    public void setCostperUnit(String costperUnit) {
        this.costperUnit = costperUnit;
    }

    public String getTotalCost1() {
        return totalCost1;
    }

    public void setTotalCost1(String totalCost1) {
        this.totalCost1 = totalCost1;
    }
}
