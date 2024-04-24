package com.gisfy.ntfp.SqliteHelper.Entity;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

public class InventoryRelation {
    @Embedded
    private InventoryEntity inventory;
    @Relation(entity = NTFP.class,
            entityColumn = "nid",
            parentColumn = "ntfpId")
    private NTFP ntfp;
    @Relation(entity = NTFP.ItemType.class,
            entityColumn = "itemId",
            parentColumn = "typeId")
    private NTFP.ItemType itemType;
    @Relation(entity = CollectorsModel.class,
            entityColumn = "cid",
            parentColumn = "collectorId")
    private CollectorsModel collector;
    @Relation(entity = MemberModel.class,
            entityColumn = "memberId",
            parentColumn = "memberId")
    private MemberModel member;

    @Ignore
    private boolean selected;

    public InventoryEntity getInventory() {
        return inventory;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public void setInventory(InventoryEntity inventory) {
        this.inventory = inventory;
    }

    public NTFP getNtfp() {
        return ntfp;
    }

    public void setNtfp(NTFP ntfp) {
        this.ntfp = ntfp;
    }

    public NTFP.ItemType getItemType() {
        return itemType;
    }

    public void setItemType(NTFP.ItemType itemType) {
        this.itemType = itemType;
    }

    public CollectorsModel getCollector() {
        return collector;
    }

    public void setCollector(CollectorsModel collector) {
        this.collector = collector;
    }

    public MemberModel getMember() {
        return member;
    }

    public void setMember(MemberModel member) {
        this.member = member;
    }
}
