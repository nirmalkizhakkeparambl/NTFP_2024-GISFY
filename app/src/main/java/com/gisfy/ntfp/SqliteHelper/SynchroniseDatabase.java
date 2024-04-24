package com.gisfy.ntfp.SqliteHelper;


import android.content.Context;
import android.util.Log;


import com.gisfy.ntfp.SqliteHelper.Entity.CollectorPaymentsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryEntity;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.Entity.NTFP;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.google.gson.JsonArray;

import org.json.JSONObject;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MemberModel.class, CollectorsModel.class, NTFP.class,NTFP.ItemType.class, InventoryEntity.class}, version = 1, exportSchema = false)
public abstract class SynchroniseDatabase extends RoomDatabase {
    private static final String LOG_TAG = SynchroniseDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "NTFPRoomDatabase";
    private static SynchroniseDatabase sInstance;

    public static SynchroniseDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        SynchroniseDatabase.class, SynchroniseDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract NtfpDao ntfpDao();

}