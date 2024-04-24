package com.gisfy.ntfp.VSS.Dashboard;

import com.google.gson.annotations.SerializedName;

public class PaymentsModel {
    @SerializedName("Purchase")
    private String purchase;
    @SerializedName("Sales")
    private String sales;
    @SerializedName("Group")
    private String group;

    public PaymentsModel() {

    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public String getPurchase() {
        return purchase;
    }

    public String getSales() {
        return sales;
    }
}
