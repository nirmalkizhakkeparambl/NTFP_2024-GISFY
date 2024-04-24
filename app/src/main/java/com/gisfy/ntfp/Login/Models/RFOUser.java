package com.gisfy.ntfp.Login.Models;

import com.google.gson.annotations.SerializedName;

public class RFOUser {

    @SerializedName("RFOId")
    public int rFOId;
    @SerializedName("DivisionId")
    public int divisionId;
    @SerializedName("divisionname")
    public String divisionname;
    @SerializedName("RangeName")
    public String rangeName;
    @SerializedName("RangeId")
    public int rangeId;
    @SerializedName("RFOName")
    public String rFOName;
    @SerializedName("RFOIncharge")
    public String rFOIncharge;
    @SerializedName("RFOInchargeNum")
    public String rFOInchargeNum;
    @SerializedName("Image")
    public String image;
    @SerializedName("FCMID")
    public String fCMID;

    public RFOUser() {
    }

    public int getrFOId() {
        return rFOId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivisionname() {
        return divisionname;
    }

    public String getRangeName() {
        return rangeName;
    }

    public int getRangeId() {
        return rangeId;
    }

    public String getrFOName() {
        return rFOName;
    }

    public String getrFOIncharge() {
        return rFOIncharge;
    }

    public String getrFOInchargeNum() {
        return rFOInchargeNum;
    }

    public String getImage() {
        return image;
    }

    public String getfCMID() {
        return fCMID;
    }
}
