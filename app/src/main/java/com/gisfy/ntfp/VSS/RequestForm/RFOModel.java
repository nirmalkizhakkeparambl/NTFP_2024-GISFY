package com.gisfy.ntfp.VSS.RequestForm;

import com.google.gson.annotations.SerializedName;

public class RFOModel {
    @SerializedName("RFOId")
    public int rFOId;
    @SerializedName("RFOName")
    public String rFOName;
    @SerializedName("FCMID")
    public String fCMID;


    public RFOModel() {
    }

    public RFOModel(int rFOId, String rFOName, String fCMID) {
        this.rFOId = rFOId;
        this.rFOName = rFOName;
        this.fCMID = fCMID;
    }

    public int getrFOId() {
        return rFOId;
    }

    public void setrFOId(int rFOId) {
        this.rFOId = rFOId;
    }

    public String getrFOName() {
        return rFOName;
    }

    public void setrFOName(String rFOName) {
        this.rFOName = rFOName;
    }

    public String getfCMID() {
        return fCMID;
    }

    public void setfCMID(String fCMID) {
        this.fCMID = fCMID;
    }
}
