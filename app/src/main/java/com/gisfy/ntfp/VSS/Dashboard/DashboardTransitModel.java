package com.gisfy.ntfp.VSS.Dashboard;

import com.google.gson.annotations.SerializedName;

public class DashboardTransitModel {
    @SerializedName("TransitStatus")
    private String transitStatus;
    @SerializedName("Total")
    private int total;

    public DashboardTransitModel() {
    }

    public String getTransitStatus() {
        return transitStatus;
    }

    public void setTransitStatus(String transitStatus) {
        this.transitStatus = transitStatus;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
