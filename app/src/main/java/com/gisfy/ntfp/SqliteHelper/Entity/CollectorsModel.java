package com.gisfy.ntfp.SqliteHelper.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class CollectorsModel {
    @PrimaryKey
    @SerializedName("Cid")
    private int cid;
    @SerializedName("CollectorName")
    private String collectorName;
    @SerializedName("Village")
    private String village;
    @SerializedName("SpouseName")
    private String spouseName;
    @SerializedName("SocialCategory")
    private String socialCategory;
    @SerializedName("Age")
    private String age;
    @SerializedName("DOB")
    private String dOB;
    @SerializedName("TypeOfId")
    private String typeOfId;
    @SerializedName("IDNumber")
    private String iDNumber;
    @SerializedName("EducationQualification")
    private String educationQualification;
    @SerializedName("TotalFamilycount")
    private String totalFamilyCount;
    @SerializedName("BankAccount")
    private String bankAccount;
    @SerializedName("BankName")
    private String bankName;
    @SerializedName("MajorCrop")
    private String majorCrop;
    @SerializedName("Rights")
    private String rights;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("Remarks")
    private String remarks;
    @SerializedName("Password")
    private String password;
    @SerializedName("Division")
    private String division;
    @SerializedName("BankIFSCCode")
    private String bankIFSCCode;
    @SerializedName("Random")
    private String random;
    @SerializedName("Id")
    private String id;
    @SerializedName("VerifiedBy")
    private String verifiedBy;
    @SerializedName("Image")
    private String image;
    @SerializedName("Pass")
    private boolean pass;
    @SerializedName("DivisionId")
    private int divisionId;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("Region")
    private String region;
    @SerializedName("RangeId")
    private int rangeId;
    @SerializedName("VSSId")
    private int vSSId;
    @SerializedName("RegionId")
    private int regionId;
    @Ignore
    @SerializedName("Member")
    private ArrayList<MemberModel> member;

    @Override
    public String toString() {
        return collectorName;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
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

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getTypeOfId() {
        return typeOfId;
    }

    public void setTypeOfId(String typeOfId) {
        this.typeOfId = typeOfId;
    }

    public String getIDNumber() {
        return iDNumber;
    }

    public void setIDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getEducationQualification() {
        return educationQualification;
    }

    public void setEducationQualification(String educationQualification) {
        this.educationQualification = educationQualification;
    }

    public String getTotalFamilyCount() {
        return totalFamilyCount;
    }

    public void setTotalFamilyCount(String totalFamilyCount) {
        this.totalFamilyCount = totalFamilyCount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMajorCrop() {
        return majorCrop;
    }

    public void setMajorCrop(String majorCrop) {
        this.majorCrop = majorCrop;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getBankIFSCCode() {
        return bankIFSCCode;
    }

    public void setBankIFSCCode(String bankIFSCCode) {
        this.bankIFSCCode = bankIFSCCode;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
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

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public int getVSSId() {
        return vSSId;
    }

    public void setVSSId(int vSSId) {
        this.vSSId = vSSId;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public ArrayList<MemberModel> getMember() {
        return member;
    }

    public void setMember(ArrayList<MemberModel> member) {
        this.member = member;
    }


}
