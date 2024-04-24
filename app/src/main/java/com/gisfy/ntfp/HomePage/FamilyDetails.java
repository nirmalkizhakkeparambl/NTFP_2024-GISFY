package com.gisfy.ntfp.HomePage;

import com.google.gson.annotations.SerializedName;

public class FamilyDetails {
    @SerializedName("Cid")
    private int cid;
    @SerializedName("FamilyName")
    private String family_name;
    @SerializedName("Village")
    private String village;
    @SerializedName("SocialCategory")
    private String socialCategory;
    @SerializedName("Age")
    private String age;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("DOB")
    private String dOB;
    @SerializedName("TypeOfId")
    private String typeOfId;
    @SerializedName("IDNumber")
    private String iDNumber;
    @SerializedName("EducationQualification")
    private String educationQualification;
    @SerializedName("BankAccount")
    private String bankAccount;
    @SerializedName("BankName")
    private String bankName;
    @SerializedName("MajorCrop")
    private String majorCrop;
    @SerializedName("BankIFSCCode")
    private String bankIFSCCode;
    @SerializedName("Id")
    private Object id;
    @SerializedName("Relation")
    private String relation;
    @SerializedName("Address")
    private String address;

    @SerializedName("Random")
    private String random;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getFamilyName() {
        return family_name;
    }

    public void setFamilyName(String familyName) {
        family_name = familyName;
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

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public String getTypeOfId() {
        return typeOfId;
    }

    public void setTypeOfId(String typeOfId) {
        this.typeOfId = typeOfId;
    }

    public String getiDNumber() {
        return iDNumber;
    }

    public void setiDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getEducationQualification() {
        return educationQualification;
    }

    public void setEducationQualification(String educationQualification) {
        this.educationQualification = educationQualification;
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

    public String getBankIFSCCode() {
        return bankIFSCCode;
    }

    public void setBankIFSCCode(String bankIFSCCode) {
        this.bankIFSCCode = bankIFSCCode;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
