package com.gisfy.ntfp.VSS.Inventory;

import com.google.gson.annotations.SerializedName;

public class NTFPType {
    @SerializedName("ItemId")
    public int itemId;
    @SerializedName("NTFPId")
    public int nTFPId;
    @SerializedName("case")
    public String type;
    @SerializedName("Grade1Price")
    public double grade1Price;
    @SerializedName("Grade2Price")
    public double grade2Price;
    @SerializedName("Grade3Price")
    public double grade3Price;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getnTFPId() {
        return nTFPId;
    }

    public void setnTFPId(int nTFPId) {
        this.nTFPId = nTFPId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getGrade1Price() {
        return grade1Price;
    }

    public void setGrade1Price(double grade1Price) {
        this.grade1Price = grade1Price;
    }

    public double getGrade2Price() {
        return grade2Price;
    }

    public void setGrade2Price(double grade2Price) {
        this.grade2Price = grade2Price;
    }

    public double getGrade3Price() {
        return grade3Price;
    }

    public void setGrade3Price(double grade3Price) {
        this.grade3Price = grade3Price;
    }

    @Override
    public String toString() {
        return type;
    }
}
