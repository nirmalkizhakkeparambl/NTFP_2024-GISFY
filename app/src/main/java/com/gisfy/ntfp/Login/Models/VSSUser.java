package com.gisfy.ntfp.Login.Models;

import com.google.gson.annotations.SerializedName;

public class VSSUser {
    @SerializedName("Vid")
    public int vid;
    @SerializedName("RangeId")
    public int rangeId;
    @SerializedName("RangeName")
    public String rangeName;
    @SerializedName("DivisionId")
    public int divisionId;
    @SerializedName("divisionname")
    public String divisionname;
    @SerializedName("Village")
    public String village;
    @SerializedName("VSSName")
    public String vSSName;
    @SerializedName("VssHead")
    public String vssHead;
    @SerializedName("HeadPhoneNumber")
    public String headPhoneNumber;
    @SerializedName("Image")
    public String image;
    @SerializedName("FCMID")
    public String fCMID;

    public VSSUser() {
    }

    public int getVid() {
        return vid;
    }

    public int getRangeId() {
        return rangeId;
    }

    public String getRangeName() {
        return rangeName;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivisionname() {
        return divisionname;
    }

    public String getVillage() {
        return village;
    }

    public String getvSSName() {
        return vSSName;
    }

    public String getVssHead() {
        return vssHead;
    }

    public String getHeadPhoneNumber() {
        return headPhoneNumber;
    }

    public String getImage() {
        return image;
    }

    public String getfCMID() {
        return fCMID;
    }
}
