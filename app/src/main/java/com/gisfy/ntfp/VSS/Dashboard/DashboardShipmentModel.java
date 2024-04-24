package com.gisfy.ntfp.VSS.Dashboard;

import com.google.gson.annotations.SerializedName;

public class DashboardShipmentModel {

    @SerializedName("Month")
    private String month;
    @SerializedName("Purchase")
    private int purchase;

    public DashboardShipmentModel(){

    }

    public DashboardShipmentModel(String month, int purchase) {
        this.month = month;
        this.purchase = purchase;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getPurchase() {
        return purchase;
    }

    public void setPurchase(int purchase) {
        this.purchase = purchase;
    }
}
