package com.gisfy.ntfp.Login.Models;

import com.google.firebase.encoders.annotations.Encodable;
import com.google.gson.annotations.SerializedName;

public class CollectorUser {
    @SerializedName("Cid")
    private Integer cid;
    @SerializedName("Division")
    private String division;
    @SerializedName("Range")
    private String range;
    @SerializedName("DivisionId")
    private Integer divisionId;
    @SerializedName("RangeId")
    private Integer rangeId;
    @SerializedName("VSSId")
    private Integer vSSId;
    @SerializedName("CollectorName")
    private String collectorName;
    @SerializedName("VSSName")
    private String vSSName;
    @SerializedName("SpouseName")
    private String spouseName;
    @SerializedName("Image")
    private String image;
    @SerializedName("Pass")
    private Boolean pass;
    @SerializedName("PassStatus")
    private boolean passStatus;
    @SerializedName("PassExpieryDate")
    private String passExpieryDate;
    @SerializedName("PassApprovedDate")
    private String passApprovedDate;
    @SerializedName("FCMID")
    private String fcmid;
    @SerializedName("VSSFCMID")
    private String vssfcmid;

    @SerializedName("UserName")
    private String userName;
    public CollectorUser() {
    }


    public int getCid() {
        return cid;
    }


    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public boolean isPassStatus() {
        return passStatus;
    }

    public void setPassStatus(boolean passStatus) {
        this.passStatus = passStatus;
    }

    public String getPassExpieryDate() {
        return passExpieryDate;
    }



    public String getPassApprovedDate() {
        return passApprovedDate;
    }

    public void setPassApprovedDate(String passApprovedDate) {
        this.passApprovedDate = passApprovedDate;
    }


    public Integer getDivisionId() {
        return divisionId;
    }

    public Integer getRangeId() {
        return rangeId;
    }

    public Integer getvSSId() {
        return vSSId;
    }

    public String getvSSName() {
        return vSSName;
    }

    public Boolean getPass() {
        return pass;
    }

    public Boolean getPassStatus() {
        return passStatus;
    }

    public String getFcmid() {
        return fcmid;
    }

    public String getVssfcmid() {
        return vssfcmid;
    }

}

