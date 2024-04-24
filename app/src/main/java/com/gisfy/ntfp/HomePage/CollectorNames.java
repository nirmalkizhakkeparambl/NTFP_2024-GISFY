package com.gisfy.ntfp.HomePage;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CollectorNames {
    @PrimaryKey
    @SerializedName("Cid")
    private int cid;
    @SerializedName("CollectorName")
    private String collectorName;
    @SerializedName("Village")
    private String village;
    @SerializedName("Range")
    private String range;
    @SerializedName("VSS")
    private String vSS;
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
    private String totalFamilycount;
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
    private Object id;
    @SerializedName("VerifiedBy")
    private String verifiedBy;
    @SerializedName("Image")
    private String image;
    @SerializedName("Pass")
    private boolean pass;
    @SerializedName("PassStatus")
    private boolean passStatus;
    @SerializedName("PassExpieryDate")
    private String passExpieryDate;
    @SerializedName("PassApprovedDate")
    private String passApprovedDate;
    @SerializedName("LastRenualDate")
    private String lastRenualDate;


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

    public String getTotalFamilycount() {
        return totalFamilycount;
    }

    public void setTotalFamilycount(String totalFamilycount) {
        this.totalFamilycount = totalFamilycount;
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

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
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

    public boolean isPassStatus() {
        return passStatus;
    }

    public void setPassStatus(boolean passStatus) {
        this.passStatus = passStatus;
    }

    public String getPassExpieryDate() {
        return passExpieryDate;
    }

    public void setPassExpieryDate(String passExpieryDate) {
        this.passExpieryDate = passExpieryDate;
    }

    public String getPassApprovedDate() {
        return passApprovedDate;
    }

    public void setPassApprovedDate(String passApprovedDate) {
        this.passApprovedDate = passApprovedDate;
    }

    public String getLastRenualDate() {
        return lastRenualDate;
    }

    public void setLastRenualDate(String lastRenualDate) {
        this.lastRenualDate = lastRenualDate;
    }
}
