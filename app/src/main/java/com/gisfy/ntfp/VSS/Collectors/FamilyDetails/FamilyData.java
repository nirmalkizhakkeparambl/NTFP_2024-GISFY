package com.gisfy.ntfp.VSS.Collectors.FamilyDetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FamilyData implements Serializable {
    @SerializedName("MemberId")
    private String familyid;
    @SerializedName("MRandom")
    private String uid;
    @SerializedName("MVillage")
    private String village;
    @SerializedName("Membername")
    private String family_name;
    @SerializedName("MSocialCategory")
    private String category;
    @SerializedName("MAge")
    private String age;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("MDOB")
    private String dob;
    @SerializedName("MTypeofId")
    private String idtype;
    @SerializedName("MIDNumber")
    private String Idno;
    @SerializedName("MMajorCrop")
    private String ntfps;
    @SerializedName("MEducationQualification")
    private String education;
    @SerializedName("MBankName")
    private String bankname;
    @SerializedName("MBankAccountNo")
    private String bankaccountno;
    @SerializedName("MBankIFSCCode")
    private String bankifsc;
    private String info;
    private int synced;
    @SerializedName("Relation")
    private String relationship;
    @SerializedName("MAddress")
    private String address;

    public FamilyData() {
    }
    public FamilyData(String uid, String familyid, String village, String name, String category, String age, String gender, String dob, String idtype, String idno, String ntfps, String education, String bankname, String bankaccountno, String bankifsc, String info, int synced, String relationship, String address) {
        this.uid = uid;
        this.familyid = familyid;
        this.village = village;
        this.family_name = name;
        this.category = category;
        this.age = age;
        this.gender = gender;
        this.dob = dob;
        this.idtype = idtype;
        this.Idno = idno;
        this.ntfps = ntfps;
        this.education = education;
        this.bankname = bankname;
        this.bankaccountno = bankaccountno;
        this.bankifsc = bankifsc;
        this.info = info;
        this.synced = synced;
        this.relationship = relationship;
        this.address = address;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("familyid:"+ familyid)
                .append("uid:"+ uid)
                .append("village:"+ village)
                .append("family_name:"+ family_name)
                .append("category:"+ category)
                .append("age:"+ age)
                .append("gender:"+ gender)
                .append("dob:"+ dob)
                .append("idtype:"+ idtype)
                .append("Idno:"+ Idno)
                .append("ntfps:"+ ntfps)
                .append("education:"+ education)
                .append("bankname:"+ bankname)
                .append("bankaccountno:"+ bankaccountno)
                .append("bankifsc:"+ bankifsc)
                .append("info:"+ info)
                .append("synced:"+ synced)
                .append("relationship:"+ relationship)
                .append("address:"+ address)
                .toString();
    }
}