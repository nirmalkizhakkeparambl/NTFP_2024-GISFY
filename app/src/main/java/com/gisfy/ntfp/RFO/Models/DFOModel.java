package com.gisfy.ntfp.RFO.Models;

import com.google.gson.annotations.SerializedName;

public class DFOModel {
    @SerializedName("DFOId")
    public int dFOId;
    @SerializedName("DFOName")
    public String dFOName;
    @SerializedName("DFOIncharge")
    public String dFOIncharge;
    @SerializedName("DFOInchargeNum")
    public String dFOInchargeNum;
    @SerializedName("DFOInchargeEmail")
    public String dFOInchargeEmail;
    @SerializedName("DFOLat")
    public String dFOLat;
    @SerializedName("DFOLong")
    public String dFOLong;
    @SerializedName("Address")
    public String address;
    @SerializedName("DFONotes")
    public String dFONotes;
    @SerializedName("UserName")
    public String userName;
    @SerializedName("Password")
    public String password;
    @SerializedName("Division")
    public String division;


    public DFOModel() {
    }

    public int getdFOId() {
        return dFOId;
    }

    public void setdFOId(int dFOId) {
        this.dFOId = dFOId;
    }

    public String getdFOName() {
        return dFOName;
    }

    public void setdFOName(String dFOName) {
        this.dFOName = dFOName;
    }

    public String getdFOIncharge() {
        return dFOIncharge;
    }

    public void setdFOIncharge(String dFOIncharge) {
        this.dFOIncharge = dFOIncharge;
    }

    public String getdFOInchargeNum() {
        return dFOInchargeNum;
    }

    public void setdFOInchargeNum(String dFOInchargeNum) {
        this.dFOInchargeNum = dFOInchargeNum;
    }

    public String getdFOInchargeEmail() {
        return dFOInchargeEmail;
    }

    public void setdFOInchargeEmail(String dFOInchargeEmail) {
        this.dFOInchargeEmail = dFOInchargeEmail;
    }

    public String getdFOLat() {
        return dFOLat;
    }

    public void setdFOLat(String dFOLat) {
        this.dFOLat = dFOLat;
    }

    public String getdFOLong() {
        return dFOLong;
    }

    public void setdFOLong(String dFOLong) {
        this.dFOLong = dFOLong;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getdFONotes() {
        return dFONotes;
    }

    public void setdFONotes(String dFONotes) {
        this.dFONotes = dFONotes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}