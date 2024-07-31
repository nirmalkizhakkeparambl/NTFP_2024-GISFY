package com.gisfy.ntfp.SqliteHelper.Entity;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InventoryEntity {

    @PrimaryKey
    @NonNull
    @SerializedName("InventID")
    private String inventoryId;
    @SerializedName("CollectorID")
    private int collectorId;
    @SerializedName("MemberId")
    private int memberId;
    @SerializedName("NTFPId")
    private int ntfpId;
    @SerializedName("NTFPTypeId")
    private int typeId;
    private String vssname;
    private String vssnamesle;
    private String colectname;
    private String grade;
    @SerializedName("Unit")
    private String measurements;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("LoseAmound")
    private double loseAmound;
    private double price;
    @SerializedName("DateTime")
    private String date;
    @SerializedName("Random")
    private String random;
    private String locationName;
    @SerializedName("location_id")
    private int locationId;
    @SerializedName("isSynced")
    private boolean synced;

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public InventoryEntity(@NonNull String inventoryId, int collectorId, int memberId, int ntfpId, int typeId ,String vssname,String vssnamesle,String colectname,String grade, String measurements, double quantity,double loseAmound, double price, String date, String locationName , int locationId) {
        this.inventoryId = inventoryId;
        this.collectorId = collectorId;
        this.memberId = memberId;
        this.ntfpId = ntfpId;
        this.typeId = typeId;

        this.vssname=vssname;
        this.vssnamesle=vssnamesle;

        this.colectname=colectname;
        this.grade = grade;
        this.measurements = measurements;
        this.quantity = quantity;
        this.loseAmound = loseAmound;
        this.price = price;
        this.date = date;
        this.locationName = locationName;
        this.locationId = locationId;

    }

    public String getVssnamesle() {
        return vssnamesle;
    }

    public void setVssnamesle(String vssnamesle) {
        this.vssnamesle = vssnamesle;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public String getVssname() {
        return vssname;
    }

    public String getColectname() {
        return colectname;
    }

    public void setColectname(String colectname) {
        this.colectname = colectname;
    }

    public void setVssname(String vssname) {
        this.vssname = vssname;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(int collectorId) {
        this.collectorId = collectorId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getNtfpId() {
        return ntfpId;
    }

    public void setNtfpId(int ntfpId) {
        this.ntfpId = ntfpId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMeasurements() {
        return measurements;
    }

    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getLoseAmound() {
        return loseAmound;
    }

    public void setLoseAmound(double loseAmound) {
        this.loseAmound = loseAmound;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
