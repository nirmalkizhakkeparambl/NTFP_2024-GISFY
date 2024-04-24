package com.gisfy.ntfp.SqliteHelper.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class NTFP {
    @PrimaryKey
    @SerializedName("Nid")
    private int nid;
    @SerializedName("NTFPscientificname")
    private String nTFPscientificname;
    @SerializedName("NTFPmalayalamname")
    private String nTFPmalayalamname;
    @SerializedName("Unit")
    private String unit;
    @Ignore
    @SerializedName("ItemType")
    private ArrayList<ItemType> itemType;


    @Override
    public String toString() {
        return nTFPmalayalamname+"( " +nTFPscientificname + " )";
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getNTFPscientificname() {
        return nTFPscientificname;
    }

    public void setNTFPscientificname(String nTFPscientificname) {
        this.nTFPscientificname = nTFPscientificname;
    }

    public String getNTFPmalayalamname() {
        return nTFPmalayalamname;
    }

    public void setNTFPmalayalamname(String nTFPmalayalamname) {
        this.nTFPmalayalamname = nTFPmalayalamname;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<ItemType> getItemType() {
        return itemType;
    }

    public void setItemType(ArrayList<ItemType> itemType) {
        this.itemType = itemType;
    }

    @Entity(foreignKeys = @ForeignKey(entity = NTFP.class,
            parentColumns = "nid",
            childColumns = "nTFPId",
            onDelete = CASCADE))
    public static class ItemType{
        @PrimaryKey
        @SerializedName("ItemId")
        private int itemId;
        @SerializedName("NTFPId")
        private int nTFPId;
        @SerializedName("case")
        private String mycase;
        @SerializedName("Grade1Price")
        private double grade1Price;
        @SerializedName("Grade2Price")
        private double grade2Price;
        @SerializedName("Grade3Price")
        private double grade3Price;

        @Override
        public String toString() {
            return mycase;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getNTFPId() {
            return nTFPId;
        }

        public void setNTFPId(int nTFPId) {
            this.nTFPId = nTFPId;
        }

        public String getMycase() {
            return mycase;
        }

        public void setMycase(String mycase) {
            this.mycase = mycase;
        }

        public double getGrade1Price() {
            return grade1Price;
        }

        public void setGrade1Price(double grade1Price) {
            this.grade1Price = grade1Price;
        }

        public double getGrade2Price() {
            return grade2Price;
        }

        public void setGrade2Price(double grade2Price) {
            this.grade2Price = grade2Price;
        }

        public double getGrade3Price() {
            return grade3Price;
        }

        public void setGrade3Price(double grade3Price) {
            this.grade3Price = grade3Price;
        }
    }


}
