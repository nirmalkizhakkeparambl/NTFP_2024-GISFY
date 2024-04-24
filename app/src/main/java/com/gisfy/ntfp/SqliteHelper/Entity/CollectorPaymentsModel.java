package com.gisfy.ntfp.SqliteHelper.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class CollectorPaymentsModel {
    @SerializedName("ShipmentId")
    public String shipmentId;
    @SerializedName("collector")
    public String collector;
    @SerializedName("Date")
    public String date;
    @SerializedName("PayStatus")
    public String payStatus;
    @SerializedName("purchaseordernumber")
    public String purchaseOrderNumber;
    @SerializedName("NTFP")
    public ArrayList<NTFP> nTFP;

    public Object getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public ArrayList<NTFP> getnTFP() {
        return nTFP;
    }

    public void setnTFP(ArrayList<NTFP> nTFP) {
        this.nTFP = nTFP;
    }

    public class NTFP{
        @SerializedName("NTFP")
        public String nTFP;
        @SerializedName("Unit")
        public String unit;
        @SerializedName("TotalCost")
        public String totalCost;
        @SerializedName("Grade1Qty")
        public String grade1Qty;
        @SerializedName("Grade2Qty")
        public String grade2Qty;
        @SerializedName("Grade3Qty")
        public String grade3Qty;
        @SerializedName("Grade1Cost")
        public int grade1Cost;
        @SerializedName("Grade2Cost")
        public int grade2Cost;
        @SerializedName("Grade3Cost")
        public int grade3Cost;
        @SerializedName("ShipmentId")
        public Object shipmentId;
        @SerializedName("CollectorID")
        public String collectorID;
        @SerializedName("MemberID")
        public String memberID;
        @SerializedName("collector")
        public String collector;
        @SerializedName("Date")
        public String date;
        @SerializedName("Range")
        public String range;
        @SerializedName("VSS")
        public String vSS;
        @SerializedName("PcName")
        public String pcName;

        public String getnTFP() {
            return nTFP;
        }

        public void setnTFP(String nTFP) {
            this.nTFP = nTFP;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(String totalCost) {
            this.totalCost = totalCost;
        }

        public String getGrade1Qty() {
            return grade1Qty;
        }

        public void setGrade1Qty(String grade1Qty) {
            this.grade1Qty = grade1Qty;
        }

        public String getGrade2Qty() {
            return grade2Qty;
        }

        public void setGrade2Qty(String grade2Qty) {
            this.grade2Qty = grade2Qty;
        }

        public String getGrade3Qty() {
            return grade3Qty;
        }

        public void setGrade3Qty(String grade3Qty) {
            this.grade3Qty = grade3Qty;
        }

        public int getGrade1Cost() {
            return grade1Cost;
        }

        public void setGrade1Cost(int grade1Cost) {
            this.grade1Cost = grade1Cost;
        }

        public int getGrade2Cost() {
            return grade2Cost;
        }

        public void setGrade2Cost(int grade2Cost) {
            this.grade2Cost = grade2Cost;
        }

        public int getGrade3Cost() {
            return grade3Cost;
        }

        public void setGrade3Cost(int grade3Cost) {
            this.grade3Cost = grade3Cost;
        }

        public Object getShipmentId() {
            return shipmentId;
        }

        public void setShipmentId(Object shipmentId) {
            this.shipmentId = shipmentId;
        }

        public String getCollectorID() {
            return collectorID;
        }

        public void setCollectorID(String collectorID) {
            this.collectorID = collectorID;
        }

        public String getMemberID() {
            return memberID;
        }

        public void setMemberID(String memberID) {
            this.memberID = memberID;
        }

        public String getCollector() {
            return collector;
        }

        public void setCollector(String collector) {
            this.collector = collector;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRange() {
            return range;
        }

        public void setRange(String range) {
            this.range = range;
        }

        public String getvSS() {
            return vSS;
        }

        public void setvSS(String vSS) {
            this.vSS = vSS;
        }

        public String getPcName() {
            return pcName;
        }

        public void setPcName(String pcName) {
            this.pcName = pcName;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("shipmentId-"+ shipmentId)
                .append("collector-"+ collector)
                .append("date-"+ date)
                .append("payStatus-"+ payStatus)
                .append("purchaseOrderNumber-"+ purchaseOrderNumber)
                .append("nTFP-"+ nTFP)
                .toString();
    }
}
