package com.gisfy.ntfp.VSS.Collectors;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Collector implements Serializable {
    @SerializedName("Random")
    private String uid;
    private String vss;
    private String division;
    private String range;
    @SerializedName("Village")
    private String village;
    @SerializedName("CollectorName")
    private String collector_name;
    @SerializedName("SpouseName")
    private String collector_spouse;
    @SerializedName("SocialCategory")
    private String category;
    @SerializedName("Age")
    private String age;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("DOB")
    private String dob;
    @SerializedName("TypeOfId")
    private String idtype;
    @SerializedName("IDNumber")
    private String Idno;
    @SerializedName("MajorCrop")
    private String ntfps;
    @SerializedName("EducationQualification")
    private String education;
    @SerializedName("TotalFamilycount")
    private String family;
    @SerializedName("BankName")
    private String bankname;
    @SerializedName("BankAccount")
    private String bankaccountno;
    @SerializedName("BankIFSCCode")
    private String bankifsc;
    @SerializedName("UserName")
    private String username;
    @SerializedName("Password")
    private String password;
    @SerializedName("Remarks")
    private String info;
    @SerializedName("isSynced")
    private int synced;
    private boolean isSelected;
    @SerializedName("Cid")
    private String Cid;


    public Collector(String uid, String vss, String division, String range, String village, String collector_name, String collector_spouse, String category, String age, String gender, String dob, String idtype, String idno, String ntfps, String education, String family, String bankname, String bankaccountno, String bankifsc, String username, String password, String info, int synced) {
        this.uid = uid;
        this.vss = vss;
        this.division = division;
        this.range = range;
        this.village = village;
        this.collector_name = collector_name;
        this.collector_spouse = collector_spouse;
        this.category = category;
        this.age = age;
        this.gender = gender;
        this.dob = dob;
        this.idtype = idtype;
        this.Idno = idno;
        this.ntfps = ntfps;
        this.education = education;
        this.family = family;
        this.bankname = bankname;
        this.bankaccountno = bankaccountno;
        this.bankifsc = bankifsc;
        this.username = username;
        this.password = password;
        this.info = info;
        this.synced = synced;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVss() {
        return vss;
    }

    public void setVss(String vss) {
        this.vss = vss;
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

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCollector_name() {
        return collector_name;
    }

    public void setCollector_name(String collector_name) {
        this.collector_name = collector_name;
    }

    public String getCollector_spouse() {
        return collector_spouse;
    }

    public void setCollector_spouse(String collector_spouse) {
        this.collector_spouse = collector_spouse;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getIdno() {
        return Idno;
    }

    public void setIdno(String idno) {
        Idno = idno;
    }

    public String getNtfps() {
        return ntfps;
    }

    public void setNtfps(String ntfps) {
        this.ntfps = ntfps;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankaccountno() {
        return bankaccountno;
    }

    public void setBankaccountno(String bankaccountno) {
        this.bankaccountno = bankaccountno;
    }

    public String getBankifsc() {
        return bankifsc;
    }

    public void setBankifsc(String bankifsc) {
        this.bankifsc = bankifsc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}