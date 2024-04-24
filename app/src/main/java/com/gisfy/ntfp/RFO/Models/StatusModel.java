package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

public class StatusModel {
    @SerializedName("Status")
    public String status;

    public StatusModel(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
