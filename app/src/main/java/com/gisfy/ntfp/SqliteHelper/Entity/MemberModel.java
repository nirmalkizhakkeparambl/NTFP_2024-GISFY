package com.gisfy.ntfp.SqliteHelper.Entity;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MemberModel {
    @PrimaryKey
    @SerializedName("MemberId")
    private int memberId;
    @SerializedName("Membername")
    private String name;
    @SerializedName("MVillage")
    private String village;
    @SerializedName("MSocialCategory")
    private String socialCategory;
    @SerializedName("MAge")
    private String age;
    @SerializedName("MDOB")
    private String dob;
    @SerializedName("MTypeofId")
    private String idType;
    @SerializedName("MIDNumber")
    private String idNumber;
    @SerializedName("MEducationQualification")
    private String eduQualification;
    @SerializedName("MBankAccountNo")
    private String bankAccountNo;
    @SerializedName("MMajorCrop")
    private String majorCrop;
    @SerializedName("Division")
    private String division;
    @SerializedName("MBankIFSCCode")
    private String ifscCode;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("MRangeId")
    private String rangeId;
    @SerializedName("MVSSId")
    private String vssId;
    @SerializedName("MDivisionId")
    private String divisionId;
    @SerializedName("CollectorId")
    private int collectorId;
    @SerializedName("MBankName")
    private String bankName;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSocialCategory() {
        return socialCategory;
    }

    public void setSocialCategory(String socialCategory) {
        this.socialCategory = socialCategory;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEduQualification() {
        return eduQualification;
    }

    public void setEduQualification(String eduQualification) {
        this.eduQualification = eduQualification;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getMajorCrop() {
        return majorCrop;
    }

    public void setMajorCrop(String majorCrop) {
        this.majorCrop = majorCrop;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRangeId() {
        return rangeId;
    }

    public void setRangeId(String rangeId) {
        this.rangeId = rangeId;
    }

    public String getVssId() {
        return vssId;
    }

    public void setVssId(String vssId) {
        this.vssId = vssId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public int getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(int collectorId) {
        this.collectorId = collectorId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return name!=null?name:"No Name Found";
    }
}
