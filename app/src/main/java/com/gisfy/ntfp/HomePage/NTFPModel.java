package com.gisfy.ntfp.HomePage;

import com.google.gson.annotations.SerializedName;

public class NTFPModel {
    @SerializedName("NTFPscientificname")
    public String nTFPscientificname;
    @SerializedName("NTFPmalayalamname")
    public String nTFPmalayalamname;
    @SerializedName("PurchaseCost")
    public String purchaseCost;
    @SerializedName("Unit")
    public String unit;
    @SerializedName("ItemType")
    public String ItemType;

    public NTFPModel() {
    }


    public NTFPModel(String nTFPscientificname, String nTFPmalayalamname, String purchaseCost, String unit, String itemType) {
        this.nTFPscientificname = nTFPscientificname;
        this.nTFPmalayalamname = nTFPmalayalamname;
        this.purchaseCost = purchaseCost;
        this.unit = unit;
        ItemType = itemType;
    }

    public String getnTFPscientificname() {
        return nTFPscientificname;
    }

    public void setnTFPscientificname(String nTFPscientificname) {
        this.nTFPscientificname = nTFPscientificname;
    }

    public String getnTFPmalayalamname() {
        return nTFPmalayalamname;
    }

    public void setnTFPmalayalamname(String nTFPmalayalamname) {
        this.nTFPmalayalamname = nTFPmalayalamname;
    }

    public String getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(String purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

}
