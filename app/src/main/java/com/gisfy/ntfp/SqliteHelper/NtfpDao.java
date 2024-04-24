package com.gisfy.ntfp.SqliteHelper;


import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryEntity;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryRelation;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.Entity.NTFP;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface NtfpDao {

    /**
     *
     * @param memberModels
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllMembers(List<MemberModel> memberModels);

    @Update
    void updateMember(MemberModel memberModel);

    @Query("select * from MemberModel")
    List<MemberModel> getAllMembers();

    @Query("select * from MemberModel where memberId=:id")
    MemberModel getMemberFromMemberId(int id);

    @Query("select * from MemberModel where collectorId=:Cid")
    List<MemberModel> getMemberFromMemberCId(int Cid);
    /**
     *
     * @param collectorModels
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllCollector(List<CollectorsModel> collectorModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCollector(CollectorsModel collectorModel);

    @Query("select * from CollectorsModel order by collectorName asc")
    List<CollectorsModel> getAllCollector();

    @Query("select * from CollectorsModel where Cid=:id")
    CollectorsModel getCollectorFromCId(int id);

    /**
     *
     * @param ntfps
     */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllNTFPS(List<NTFP> ntfps);

    @Update
    void updateNTFP(NTFP ntfp);

    @Query("select * from NTFP")
    List<NTFP> getAllNTFPs();

    @Query("select * from NTFP where nid=:id")
    NTFP getNTFPFromNId(int id);

    /**
     *
     * @param itemTypes
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllNTFPTypes(List<NTFP.ItemType> itemTypes);

    @Update
    void updateNTFPType(NTFP.ItemType ntfp);

    @Query("select * from ItemType")
    List<NTFP.ItemType> getAllNTFPTypes();

    @Query("select * from ItemType where nTFPId=:id")
    List<NTFP.ItemType> getItemTypesFromNid(int id);

    /**
     * Inventory
     * @return
     */
    @Transaction
    @Query("select * from InventoryEntity where price !='' ")
    List<InventoryRelation> getAllInventories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInventory(InventoryEntity entity);

    @Query("DELETE FROM InventoryEntity WHERE inventoryId = :id")
    void deleteInventory(String id);

    @Transaction
    @Query("select * from InventoryEntity where inventoryId=:uid")
    InventoryRelation getInventoryFromId(String uid);

    @Query("update InventoryEntity set synced=:flag where inventoryId=:id")
    void setSyncStatus(boolean flag,String id);
}
