package com.gisfy.ntfp.HomePage;

import com.google.gson.annotations.SerializedName;

public class PCModel {
    @SerializedName("PcId")
    public int pcId;
    @SerializedName("PcName")
    public String pcName;

    public PCModel() {
    }

    public PCModel(int pcId, String pcName) {
        this.pcId = pcId;
        this.pcName = pcName;
    }

    public int getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }
}
