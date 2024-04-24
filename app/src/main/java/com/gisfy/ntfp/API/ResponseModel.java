package com.gisfy.ntfp.API;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {
    @SerializedName("Random")
    public String random;
    @SerializedName("Status")
    public String status;

    public ResponseModel(){

    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
