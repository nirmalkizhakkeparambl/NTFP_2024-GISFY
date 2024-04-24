package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

public class VSSModel {
    @SerializedName("Vid")
    public int vid;
    @SerializedName("VSSName")
    public String vSSName;
    @SerializedName("Village")
    public String village;
    @SerializedName("Range")
    public String range;
    @SerializedName("CollectorsCount")
    public String collectorsCount;
    @SerializedName("VssHead")
    public String vssHead;
    @SerializedName("HeadPhoneNumber")
    public String headPhoneNumber;
    @SerializedName("Remarks")
    public String remarks;
    @SerializedName("Address")
    public String address;
    @SerializedName("UserName")
    public String userName;
    @SerializedName("Lat")
    public String lat;
    @SerializedName("Long")
    public String lng;
    @SerializedName("Division")
    public String division;
    @SerializedName("Password")
    public String password;
    @SerializedName("FCMID")
    public String fCMID;

    public VSSModel() {
    }

    public VSSModel(int vid, String vSSName, String village, String range, String collectorsCount, String vssHead, String headPhoneNumber, String remarks, String address, String userName, String lat, String lng, String division, String password, String fCMID) {
        this.vid = vid;
        this.vSSName = vSSName;
        this.village = village;
        this.range = range;
        this.collectorsCount = collectorsCount;
        this.vssHead = vssHead;
        this.headPhoneNumber = headPhoneNumber;
        this.remarks = remarks;
        this.address = address;
        this.userName = userName;
        this.lat = lat;
        this.lng = lng;
        this.division = division;
        this.password = password;
        this.fCMID = fCMID;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getvSSName() {
        return vSSName;
    }

    public void setvSSName(String vSSName) {
        this.vSSName = vSSName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getCollectorsCount() {
        return collectorsCount;
    }

    public void setCollectorsCount(String collectorsCount) {
        this.collectorsCount = collectorsCount;
    }

    public String getVssHead() {
        return vssHead;
    }

    public void setVssHead(String vssHead) {
        this.vssHead = vssHead;
    }

    public String getHeadPhoneNumber() {
        return headPhoneNumber;
    }

    public void setHeadPhoneNumber(String headPhoneNumber) {
        this.headPhoneNumber = headPhoneNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfCMID() {
        return fCMID;
    }

    public void setfCMID(String fCMID) {
        this.fCMID = fCMID;
    }
}