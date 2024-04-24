package com.gisfy.ntfp.SqliteHelper;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.gisfy.ntfp.Collectors.CollectorInventoryModel;
import com.gisfy.ntfp.HomePage.CollectorNames;
import com.gisfy.ntfp.HomePage.FamilyDetails;
import com.gisfy.ntfp.HomePage.NTFPModel;
import com.gisfy.ntfp.HomePage.PCModel;
import com.gisfy.ntfp.Login.Models.PaymentsModel;
import com.gisfy.ntfp.Login.Models.ShipmentModel;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.FamilyData;
import com.gisfy.ntfp.VSS.Inventory.Inventory;
import com.gisfy.ntfp.VSS.Payment.Model_payment;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;
import com.gisfy.ntfp.VSS.Shipment.Model_shipment;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NFTPDatabase";
    public static final String COLLECTORS = "Collectors";
    public static final String FAMILYDETAILS = "FamilyDetails";
    public static final String INVENTORY = "Inventory";
    public static final String PAYMENTS = "Payments";
    public static final String SHIPMENTS = "Shipments";
    public static final String NTFP = "Ntfps";
    public static final String COLLECTOR_NAME = "Collector_Name";
    public static final String PROCESSING_CENTER = "Processing_Center";


    public static final String TRANSIT_TABLE = "TransitTable";
    public static final String TRANSIT_NTFP_TABLE = "TransitNTFPTable";

    public static final String TRANSITID = "TransitId";
    public static final String DIVISION = "Division";
    public static final String RANGE = "Range";
    public static final String RFO = "Rfo";
    public static final String VSS = "Vss";
    public static final String DATE = "DateandTime";
    public static final String PCNAME = "PcName";
    public static final String TRANSITSTATUS = "TransitStatus";
    public static final String SHIPMENTSTATUS = "ShipmentStatus";
    public static final String FCMID = "FcmId";

    public static final String NTFPNAME = "NTFPName";
    public static final String UNIT = "Unit";
    public static final String QUANTITY = "Quantity";
    public static final String LOSE = "LoseAmound";
    public static final String MALAYALAM = "MALAYALAM";

    public static final String STOCKSID = "StocksId";
    public static final String NTFPTYPE = "NTFPType";
    public static final String SYNCED = "Synced";
    public static final String PAYSTATUS = "PayStatus";
    public static final String VSSSTATUS = "VSSStatus";
    private static String MEMBER_ID = "MemberId";

    public static final String COLLECTOR_INV_TABLE = "CollectorNTFPTable";


    public static final String TABLE_5 = "COLLDetails";
    public static final String TABLE_5_COL_ForeignKey = "FarmerId";
    public static final String TABLE_5_COL_0 = "Id";
    public static final String TABLE_5_COL_1 = "Relationship";
    public static final String TABLE_5_COL_2 = "CollName";
    public static final String TABLE_5_COL_4 = "MemID";
    public static String TABLE_5_COL_3 = "CollId";
    public static String TABLE_5_COL_5 = "selectVSSId";
    public static String TABLE_5_COL_6 = "selectMMID";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }


    /**
     * variables for future use
     * @param db
     */

    private static String MEMBER_TABLE ="MemberTable";
//    private static String MEMBER_ID = "MemberId";
    private static String MEMBER_NAME    ="Membername";
    private static String MEMBER_VILLAGE          ="MVillage";
    private static String MEMBER_SOCIAL_CAT          ="MSocialCategory";
    private static String MEMBER_AGE         ="MAge";
    private static String MEMBER_DOB          ="MDOB";
    private static String MEMBER_ID_TYPE          ="MTypeofId";
    private static String MEMBER_ID_NUMBER          ="MIDNumber";
    private static String MEMBER_EDU_QUAL          ="MEducationQualification";
    private static String MEMBER_BANK_ACC          ="MBankAccountNo";
    private static String MEMBER_MAJOR_CROP          ="MMajorCrop";
    private static String MEMBER_DIVISION          ="Division";
    private static String MEMBER_IFSC          ="MBankIFSCCode";
    private static String MEMBER_GENDER          ="Gender";
    private static String MEMBER_RANGE_ID          ="MRangeId";
    private static String MEMBER_VSS_ID          ="MVSSId";
    private static String MEMBER_uDIVISION_ID          ="MDivisionId";
    private static String MEMBER_COLLECTOR_ID          ="CollectorId";
    private static String MEMBER_BANK_NAME          ="MBankName";
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TRANSIT_TABLE + " (" + TRANSITID + " TEXT PRIMARY KEY, " + DIVISION + " TEXT, " + RANGE + " TEXT, " + RFO +  " TEXT, " + VSS + " TEXT, " + DATE + " TEXT, " + PCNAME + " TEXT, " + TRANSITSTATUS + " TEXT, " + SHIPMENTSTATUS + " TEXT, "+FCMID+ " TEXT )");

        db.execSQL("CREATE TABLE " + TRANSIT_NTFP_TABLE + " (" + TRANSITID + " TEXT , " + NTFPNAME + " TEXT, " + MALAYALAM +  " TEXT, "  + NTFPTYPE + " TEXT, "+ UNIT + " TEXT, " + QUANTITY +  " TEXT, " +STOCKSID+ " INTEGER )");

        db.execSQL("CREATE TABLE " + COLLECTOR_INV_TABLE + " (" + STOCKSID + " TEXT , " + VSS + " TEXT, "+ DIVISION + " TEXT, "+ RANGE + " TEXT, "+ COLLECTOR_NAME + " TEXT, "+ NTFPNAME + " TEXT, " + NTFPTYPE + " TEXT, " + UNIT + " TEXT, " + QUANTITY +  " TEXT, " + DATE +  " TEXT, " + PAYSTATUS +  " INTEGER, "+ VSSSTATUS +  " INTEGER, " +SYNCED+ " INTEGER , " +MEMBER_ID+ " INTEGER)");


        db.execSQL(
                "create table " + NTFP +
                        " (id integer primary key," +
                        "english text," +
                        "malayalam text," +
                        "price text," +
                        "quantity text," +
                        "ItemType text)"
        );
        db.execSQL(
                "create table " + PROCESSING_CENTER +
                        " (id integer primary key,"+
                        "pcid text," +
                        "pc text)"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_5 + "(" +
                        TABLE_5_COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        TABLE_5_COL_1 + " VARCHAR," +
                        TABLE_5_COL_2 + " VARCHAR," +
                        TABLE_5_COL_3 + " VARCHAR," +
                        TABLE_5_COL_5 + " VARCHAR," +
                        TABLE_5_COL_6 + " VARCHAR" +
        ")");

        db.execSQL(
                "create table " + COLLECTOR_NAME +
                        " (id integer primary key,"+
                        "name text)"
        );

        db.execSQL(
                "create table " + PAYMENTS +
                        "(id integer primary key," +
                        "uid text," +
                        "VSS  text, " +
                        "Division text," +
                        "Range text," +
                        "Amount integer," +
                        "DateTime text," +
                        "Source text," +
                        "SourceName text," +
                        "paymentType text," +
                        "collector text," +
                        "product text," +
                        "measurement text," +
                        "quantity text," +
                        "Synced integer," +
                        "Status integer)"
        );

        db.execSQL("create table " + COLLECTORS +
                        " (id integer primary key," +
                        "uid text," +
                        "vss  text, " +
                        "division text," +
                        "range text," +
                        "village text," +
                        "collector_name text," +
                        "collector_spouse text," +
                        "category text," +
                        "age text," +
                        "gender text," +
                        "dob text," +
                        "idtype text," +
                        "Idno text," +
                        "ntfps text," +
                        "education text," +
                        "family text," +
                        "bankname text," +
                        "bankaccountno text," +
                        "bankifsc text," +
                        "username text," +
                        "password text," +
                        "info text," +
                        "synced text)"
        );
        db.execSQL("create table " + FAMILYDETAILS +
                "(id text primary key," +
                "uid text," +
                "familyid text," +
                "village text," +
                "family_name text," +
                "category text," +
                "age text," +
                "gender text," +
                "dob text," +
                "idtype text," +
                "Idno text," +
                "ntfps text," +
                "education text," +
                "bankname text," +
                "bankaccountno text," +
                "bankifsc text," +
                "info text,"+
                "synced text," +
                "relationship text,"+
                "address text)"
        );
        db.execSQL(
                "create table " + INVENTORY +
                        " (id integer primary key," +
                        "uid text," +
                        "vss text," +
                        "division text," +
                        "range text," +
                        "collector text," +
                        "ntfp text," +
                        "unit text," +
                        "quantity text," +
                        "date text," +
                        "amount text," +
                        "ntfpType text," +
                        "ntfpGrade text,"+
                        "transit integer," +
                        "payment integer," +
                        "synced integer)"
        );
        db.execSQL(
                "create table " + SHIPMENTS +
                        " (id integer primary key," +
                        "uid text," +
                        "modified integer," +
                        "vss text," +
                        "division text," +
                        "range text," +
                        "processing_center text," +
                        "date text," +
                        "transitPass text," +
                        "vehicleType text," +
                        "vehicleNo text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COLLECTORS);
        db.execSQL("DROP TABLE IF EXISTS " + FAMILYDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + SHIPMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + NTFP);
        db.execSQL("DROP TABLE IF EXISTS " + PROCESSING_CENTER);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSIT_NTFP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COLLECTOR_INV_TABLE);
        onCreate(db);
    }

    public void Insert(NTFPModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ntfp = new ContentValues();
        ntfp.put("english",model.getnTFPscientificname());
        ntfp.put("malayalam",model.getnTFPmalayalamname());
        ntfp.put("price",model.getPurchaseCost());
        ntfp.put("quantity",model.getUnit());
        ntfp.put("ItemType",model.getItemType());
        db.insert(NTFP, null, ntfp);
    }
    public void Insert(FamilyDetails data){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + FAMILYDETAILS + " where uid='"+data.getRandom().trim()+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <=0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", data.getRandom());
            contentValues.put("village", data.getVillage());
            contentValues.put("family_name", data.getFamilyName());
            contentValues.put("category", data.getSocialCategory());
            contentValues.put("age", data.getAge());
            contentValues.put("gender", data.getGender());
            contentValues.put("dob", data.getdOB());
            contentValues.put("idtype", data.getTypeOfId());
            contentValues.put("Idno", data.getiDNumber());
            contentValues.put("ntfps", data.getMajorCrop());
            contentValues.put("education", data.getEducationQualification());
            contentValues.put("bankname", data.getBankName());
            contentValues.put("bankaccountno", data.getBankAccount());
            contentValues.put("bankifsc", data.getBankIFSCCode());
            contentValues.put("synced", 1);
            contentValues.put("relationship", data.getRelation());
            contentValues.put("address", data.getAddress());
            db.insert(FAMILYDETAILS, null, contentValues);
        }
        cursor.close();
    }

    public void Insert(CollectorNames data){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + COLLECTORS + " where uid='"+data.getRandom().trim()+"'";
        Cursor cursor = db.rawQuery(Query, null);

        if(cursor.getCount() <=0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", data.getRandom());
            contentValues.put("vss", data.getvSS());
            contentValues.put("division", data.getDivision());
            contentValues.put("range", data.getRange());
            contentValues.put("village", data.getVillage());
            contentValues.put("collector_name", data.getCollectorName());
            contentValues.put("collector_spouse", data.getSpouseName());
            contentValues.put("category", data.getSocialCategory());
            contentValues.put("age", data.getAge());
            contentValues.put("gender", "");
            contentValues.put("dob", data.getdOB());
            contentValues.put("idtype", data.getTypeOfId());
            contentValues.put("Idno", data.getiDNumber());
            contentValues.put("ntfps", data.getMajorCrop());
            contentValues.put("education", data.getEducationQualification());
            contentValues.put("family", data.getTotalFamilycount());
            contentValues.put("bankname", data.getBankName());
            contentValues.put("bankaccountno", data.getBankAccount());
            contentValues.put("bankifsc", data.getBankIFSCCode());
            contentValues.put("username", data.getUserName());
            contentValues.put("password", data.getPassword());
            contentValues.put("info", data.getRemarks());
            contentValues.put("synced", 1);
            db.insert(COLLECTORS, null, contentValues);
        }
        cursor.close();
    }
    public void Insert(ShipmentModel data){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + SHIPMENTS + " where uid='"+data.getShipmentNumber().trim()+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <=0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", data.getShipmentNumber());
            contentValues.put("modified", 1);
            contentValues.put("vss", data.getvSS());
            contentValues.put("division", data.getDivision());
            contentValues.put("range", data.getRange());
            contentValues.put("processing_center", data.getProcessingCenter());
            contentValues.put("date", data.getDateTime());
            contentValues.put("transitPass", data.getTransitPass());
            contentValues.put("vehicleType", data.getTransportation());
            contentValues.put("vehicleNo", data.getVehicleNo());
            db.insert(SHIPMENTS, null, contentValues);
        }
        cursor.close();
    }
    public void Insert(PaymentsModel data){
        Log.i("in method",data.getRandom());
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + PAYMENTS + " where uid='"+data.getRandom().trim()+"'";
        Cursor cursor = db.rawQuery(Query, null);
        Log.i("dsfm",cursor.getCount()+"");
        Log.i("product349",data.getProduct()+"");


        if(cursor.getCount() <=0){
            Log.i("in condition",data.getRandom());
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", data.getRandom());
            contentValues.put("VSS", data.getvSS());
            contentValues.put("Division", data.getDivision());
            contentValues.put("Range", data.getRange());
            contentValues.put("Amount", data.getAmount());
            contentValues.put("DateTime", data.getDateTime());
            contentValues.put("Source", data.getThirdParty());
            contentValues.put("SourceName", data.getSocietyName());
            contentValues.put("paymentType", data.getPaymentType());
            contentValues.put("product", data.getProduct());
            contentValues.put("measurement", data.getQuantityMeasurement());
            contentValues.put("quantity", data.getQuantity());
            contentValues.put("Synced",1);
            contentValues.put("Status",Integer.valueOf(data.getPaymentStatus()));
            db.insert(PAYMENTS, null, contentValues);
        }
        cursor.close();
    }

    public void Insert(StocksModel data){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + INVENTORY + " where uid='"+data.getRandom().trim()+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <=0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", data.getRandom());
            contentValues.put("vss", data.getvSSName());
            contentValues.put("division", data.getDivisionName());
            contentValues.put("range", data.getRangeName());
            contentValues.put("collector", data.getCollector());
            contentValues.put("ntfp", data.getnTFPName());
            contentValues.put("unit", data.getUnit());
            contentValues.put("quantity", data.getQuantity());
            contentValues.put("date", data.getDateandTime());
            contentValues.put("amount", data.getAmount());
            contentValues.put("ntfpType", data.getnTFPType());
            contentValues.put("transit", 0);
            contentValues.put("payment", 0);
            contentValues.put("synced", 1);
            db.insert(INVENTORY, null, contentValues);
        }
        cursor.close();
    }

    public void Insert(PCModel pc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pc", pc.getPcName());
        contentValues.put("pcid", pc.getPcId());
        db.insert(PROCESSING_CENTER, null, contentValues);
    }

    public List<PCModel> getPCs(){
        List<PCModel> pcs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from " + PROCESSING_CENTER , null);
        if (cursor.moveToFirst()) {
            do {
                pcs.add(new PCModel(Integer.parseInt(cursor.getString(1)),cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pcs;
    }
    public void Insert(CollectorInventoryModel model,int type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues inv = new ContentValues();
        inv.put(STOCKSID,model.getUID());
        inv.put(VSS,model.getVSS());
        inv.put(DIVISION,model.getDivision());
        inv.put(RANGE,model.getRange());
        inv.put(COLLECTOR_NAME,model.getName());
        inv.put(NTFPNAME,model.getNTFP());
        inv.put(NTFPTYPE,model.getNTFPType());
        inv.put(UNIT,model.getUnit());
        inv.put(QUANTITY,model.getQuantity());
        inv.put(DATE,model.getDate());
        inv.put(PAYSTATUS,model.getPayStatus());
        inv.put(VSSSTATUS,model.getVSSStatus());
        inv.put(SYNCED,model.getSynced());
        inv.put(MEMBER_ID,model.getMemberId());
        if (type==0)
            db.insert(COLLECTOR_INV_TABLE, null, inv);
        else
            db.update(COLLECTOR_INV_TABLE, inv, STOCKSID+" = ? ", new String[]{model.getUID()});

    }

    @SuppressLint("Range")
    public List<CollectorInventoryModel> getCollectorInv(){
        List<CollectorInventoryModel> invs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from " + COLLECTOR_INV_TABLE +" order by Synced", null);
        if (cursor.moveToFirst()) {
            do {
                CollectorInventoryModel model=new CollectorInventoryModel();
                model.setUID(cursor.getString(cursor.getColumnIndex(STOCKSID)));
                model.setDivision(cursor.getString(cursor.getColumnIndex(DIVISION)));
                model.setVSS(cursor.getString(cursor.getColumnIndex(VSS)));
                model.setRange( cursor.getString(cursor.getColumnIndex(RANGE)));
                model.setName(cursor.getString(cursor.getColumnIndex(COLLECTOR_NAME)));
                model.setNTFP( cursor.getString(cursor.getColumnIndex(NTFPNAME)));
                model.setNTFPType(cursor.getString(cursor.getColumnIndex(NTFPTYPE)));
                model.setUnit(cursor.getString(cursor.getColumnIndex(UNIT)));
                model.setQuantity( cursor.getString(cursor.getColumnIndex(QUANTITY)));
                model.setDate( cursor.getString(cursor.getColumnIndex(DATE)));
                model.setPayStatus(cursor.getInt(cursor.getColumnIndex(PAYSTATUS)));
                model.setVSSStatus(cursor.getInt(cursor.getColumnIndex(VSSSTATUS)));
                model.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED)));
                model.setMemberId(cursor.getInt(cursor.getColumnIndex(MEMBER_ID)));
                invs.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return invs;
    }
    @SuppressLint("Range")
    public List<CollectorInventoryModel> getCollectorInv(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        List<CollectorInventoryModel> invs = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+COLLECTOR_INV_TABLE+" where "+name,null);
        if (cursor.moveToFirst()) {
            do {
                CollectorInventoryModel model=new CollectorInventoryModel();
                model.setUID(cursor.getString(cursor.getColumnIndex(STOCKSID)));
                model.setDivision(cursor.getString(cursor.getColumnIndex(DIVISION)));
                model.setVSS(cursor.getString(cursor.getColumnIndex(VSS)));
                model.setRange( cursor.getString(cursor.getColumnIndex(RANGE)));
                model.setName(cursor.getString(cursor.getColumnIndex(COLLECTOR_NAME)));
                model.setNTFP( cursor.getString(cursor.getColumnIndex(NTFPNAME)));
                model.setNTFPType(cursor.getString(cursor.getColumnIndex(NTFPTYPE)));
                model.setUnit(cursor.getString(cursor.getColumnIndex(UNIT)));
                model.setQuantity( cursor.getString(cursor.getColumnIndex(QUANTITY)));
                model.setQuantity( cursor.getString(cursor.getColumnIndex(LOSE)));
                model.setDate( cursor.getString(cursor.getColumnIndex(DATE)));
                model.setPayStatus(cursor.getInt(cursor.getColumnIndex(PAYSTATUS)));
                model.setVSSStatus(cursor.getInt(cursor.getColumnIndex(VSSSTATUS)));
                model.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED)));
                model.setMemberId(cursor.getInt(cursor.getColumnIndex(MEMBER_ID)));
                invs.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return invs;
    }

    public long addFamilyMember( String relationship, String CollName, String CollId,String selectVSSId,String selectMMid
           ) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_5_COL_1, relationship);
        contentValues.put(TABLE_5_COL_2, CollName);
        contentValues.put(TABLE_5_COL_3, CollId);
        contentValues.put(TABLE_5_COL_5, selectVSSId);
        contentValues.put(TABLE_5_COL_6, selectMMid);


        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_5, null, contentValues);
        return result;
    }

    public boolean updateFamilyMember(long familyMemberId, String relationship,
                                      String CollName, String CollId,String selectdVssID,String selectdMMID) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_5_COL_1, relationship);
        contentValues.put(TABLE_5_COL_2, CollName);
        contentValues.put(TABLE_5_COL_3, CollId);
        contentValues.put(TABLE_5_COL_5, selectdVssID);
        contentValues.put(TABLE_5_COL_6, selectdMMID);



        SQLiteDatabase db = getWritableDatabase();
        int count = db.update(TABLE_5, contentValues, TABLE_5_COL_0 + " = ?", new String[]{familyMemberId + ""});
        if (count > 0)
            return true;
        else
            return false;
    }



    public String getpcIDfrompcName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String pcid="";
        Cursor cursor = db.rawQuery("select * from Processing_Center where pc='"+name+"'",null);
        if (cursor.moveToFirst()) {
            do {
                Log.i("odfd",cursor.getString(1)+"");
                pcid=cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pcid;
    }

    public String getpcpcNamefrompcID(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String pcid="";
        boolean isExist = false;
        Cursor cursor = db.rawQuery("select * from Processing_Center where pcid='"+name+"'",null);
        if (cursor.moveToFirst()) {
            do {
                pcid=cursor.getString(2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pcid;
    }
    public List<String> getPCStrings(){
        List<String> pcs = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from " + PROCESSING_CENTER , null);
        if (cursor.moveToFirst()) {
            do {
                pcs.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pcs;
    }

    public List<String> getCollectorNames(){
        List<String> names = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from " + COLLECTORS , null);
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }
        return names;
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isExist = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }
    public List<String> getNTFPs(String lang){
        List<String> ntfps = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if (lang.equals("ml")) {
            cursor = db.rawQuery("select malayalam from " + NTFP, null);
        }
        else{
            cursor = db.rawQuery("select english from " + NTFP, null);
        }
        if (cursor.moveToFirst()) {
            do {
                ntfps.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ntfps;
    }
    public List<NTFPModel> getNTFPSWhere(String where,String lang){
        List<NTFPModel> ntfps = new ArrayList<NTFPModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if (lang.equals("ml"))
            cursor = db.rawQuery("select * from " + NTFP +" WHERE "+where+"" , null);
        else
            cursor = db.rawQuery("select * from " + NTFP +" WHERE "+where+"" , null);
        if (cursor.moveToFirst()) {
            do {
                ntfps.add(new NTFPModel(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ntfps;
    }
    private boolean checkDataExistOrNot(String tableName,String columnName, String value) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + value+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;  // return false if value not exists in database
        }
        cursor.close();
        return true;  // return true if value exists in database
    }
    public boolean insertData(FamilyData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("village", data.getVillage());
        contentValues.put("family_name", data.getFamily_name());
        contentValues.put("category", data.getCategory());
        contentValues.put("age", data.getAge());
        contentValues.put("gender", data.getGender());
        contentValues.put("dob", data.getDob());
        contentValues.put("idtype", data.getIdtype());
        contentValues.put("Idno", data.getIdno());
        contentValues.put("ntfps", data.getNtfps());
        contentValues.put("education", data.getEducation());
        contentValues.put("bankname", data.getBankname());
        contentValues.put("bankaccountno", data.getBankaccountno());
        contentValues.put("bankifsc", data.getBankifsc());
        contentValues.put("info", data.getInfo());
        contentValues.put("synced", data.getSynced());
        contentValues.put("relationship", data.getRelationship());
        contentValues.put("address", data.getAddress());
        Log.i("family Id632",data.getFamilyid());
        Log.i("family  Name Id632",data.getFamily_name());
        contentValues.put("familyid", data.getFamilyid());
        if (checkDataExistOrNot(FAMILYDETAILS,"familyid",data.getFamilyid())) {
            db.update(FAMILYDETAILS, contentValues, "familyid = ? ", new String[]{data.getFamilyid()});
        }else{
            db.insert(FAMILYDETAILS, null, contentValues);
        }

        Log.i("dbcontentValues",db.toString());

        return true;
    }

    public boolean insertData(Collector data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("vss", data.getVss());
        contentValues.put("division", data.getDivision());
        contentValues.put("range", data.getRange());
        contentValues.put("village", data.getVillage());
        contentValues.put("collector_name", data.getCollector_name());
        contentValues.put("collector_spouse", data.getCollector_spouse());
        contentValues.put("category", data.getCategory());
        contentValues.put("age", data.getAge());
        contentValues.put("gender", data.getGender());
        contentValues.put("dob", data.getDob());
        contentValues.put("idtype", data.getIdtype());
        contentValues.put("Idno", data.getIdno());
        contentValues.put("ntfps", data.getNtfps());
        contentValues.put("education", data.getEducation());
        contentValues.put("family", data.getFamily());
        contentValues.put("bankname", data.getBankname());
        contentValues.put("bankaccountno", data.getBankaccountno());
        contentValues.put("bankifsc", data.getBankifsc());
        contentValues.put("username", data.getUsername());
        contentValues.put("password", data.getPassword());
        contentValues.put("info", data.getInfo());
        contentValues.put("synced", data.getSynced());
        db.insert(COLLECTORS, null, contentValues);
        return true;
    }
    public boolean insertData(Inventory data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("vss", data.getVss());
        contentValues.put("division", data.getDivision());
        contentValues.put("range", data.getRange());
        contentValues.put("collector", data.getCollector());
        contentValues.put("ntfp", data.getNtfp());
        contentValues.put("unit", data.getUnit());
        contentValues.put("quantity", data.getQuantity());
        contentValues.put("date", data.getDate());
        contentValues.put("amount", data.getAmount());
        contentValues.put("ntfpType", data.getNtfpType());
        contentValues.put("transit", data.getTransit());
        contentValues.put("payment", data.getPayment());
        contentValues.put("synced", data.getSynced());
        contentValues.put("ntfpGrade", data.getNtfpGrade());
        contentValues.put(MEMBER_ID, data.getMemberId());
        db.insert(INVENTORY, null, contentValues);
        return true;
    }
    public boolean insertData(Model_payment data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("VSS", data.getVss());
        contentValues.put("Division", data.getDivision());
        contentValues.put("Range", data.getRange());
        contentValues.put("Amount", data.getAmount());
        contentValues.put("DateTime", data.getDate());
        contentValues.put("Source", data.getSource());
        contentValues.put("SourceName", data.getSourceName());

        contentValues.put("paymentType", data.getPaymentType());
        contentValues.put("collector", data.getCollector());
        contentValues.put("product", data.getProduct());
        contentValues.put("measurement", data.getMeasurement());
        contentValues.put("quantity", data.getQuantity());

        contentValues.put("Synced", data.getSynced());
        contentValues.put("Status", data.getStatus());
        db.insert(PAYMENTS, null, contentValues);
        return true;
    }
    public boolean insertData(Model_shipment data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("modified", data.getSynced());
        contentValues.put("vss", data.getVss());
        contentValues.put("division", data.getDivision());
        contentValues.put("range", data.getRange());
        contentValues.put("processing_center", data.getProcessing_center());
        contentValues.put("date", data.getDate());
        contentValues.put("transitPass", data.getTransitPass());
        contentValues.put("vehicleType", data.getVehicleType());
        contentValues.put("vehicleNo", data.getVehicleNo());
        db.insert(SHIPMENTS, null, contentValues);
        return true;
    }
    public boolean updateData(FamilyData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("village", data.getVillage());
        contentValues.put("family_name", data.getFamily_name());
        contentValues.put("category", data.getCategory());
        contentValues.put("age", data.getAge());
        contentValues.put("gender", data.getGender());
        contentValues.put("dob", data.getDob());
        contentValues.put("idtype", data.getIdtype());
        contentValues.put("Idno", data.getIdno());
        contentValues.put("ntfps", data.getNtfps());
        contentValues.put("education", data.getEducation());
        contentValues.put("bankname", data.getBankname());
        contentValues.put("bankaccountno", data.getBankaccountno());
        contentValues.put("bankifsc", data.getBankifsc());
        contentValues.put("info", data.getInfo());
        contentValues.put("relationship", data.getRelationship());
        contentValues.put("address", data.getAddress());
        db.update(FAMILYDETAILS, contentValues, "uid = ? ", new String[]{data.getUid()});
        return true;
    }

    public boolean updateData(Collector data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("vss", data.getVss());
        contentValues.put("division", data.getDivision());
        contentValues.put("range", data.getRange());
        contentValues.put("village", data.getVillage());
        contentValues.put("collector_name", data.getCollector_name());
        contentValues.put("collector_spouse", data.getCollector_spouse());
        contentValues.put("category", data.getCategory());
        contentValues.put("age", data.getAge());
        contentValues.put("gender", data.getGender());
        contentValues.put("dob", data.getDob());
        contentValues.put("idtype", data.getIdtype());
        contentValues.put("Idno", data.getIdno());
        contentValues.put("ntfps", data.getNtfps());
        contentValues.put("education", data.getEducation());
        contentValues.put("family", data.getFamily());
        contentValues.put("bankname", data.getBankname());
        contentValues.put("bankaccountno", data.getBankaccountno());
        contentValues.put("bankifsc", data.getBankifsc());
        contentValues.put("username", data.getUsername());
        contentValues.put("password", data.getPassword());
        contentValues.put("info", data.getInfo());
        contentValues.put("synced", data.getSynced());
        db.update(COLLECTORS, contentValues, "uid = ? ", new String[]{data.getUid()});
        return true;
    }


    public boolean updateData(Inventory data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("vss", data.getVss());
        contentValues.put("division", data.getDivision());
        contentValues.put("range", data.getRange());
        contentValues.put("collector", data.getCollector());
        contentValues.put("ntfp", data.getNtfp());
        contentValues.put("unit", data.getUnit());
        contentValues.put("quantity", data.getQuantity());

        contentValues.put("date", data.getDate());
        contentValues.put("amount", data.getAmount());
        contentValues.put("ntfpType", data.getNtfpType());
        contentValues.put("transit", data.getTransit());
        contentValues.put("payment", data.getPayment());
        contentValues.put("synced", data.getSynced());
        contentValues.put("ntfpGrade", data.getNtfpGrade());
        contentValues.put(MEMBER_ID, data.getMemberId());
        db.update(INVENTORY, contentValues, "uid = ? ", new String[]{data.getUid()});
        return true;
    }
    public boolean updateData(Model_payment data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("VSS", data.getVss());
        contentValues.put("Division", data.getDivision());
        contentValues.put("Range", data.getRange());
        contentValues.put("Amount", data.getAmount());
        contentValues.put("DateTime", data.getDate());
        contentValues.put("Source", data.getSource());
        contentValues.put("SourceName", data.getSourceName());
        contentValues.put("paymentType", data.getPaymentType());
        contentValues.put("collector", data.getCollector());
        contentValues.put("product", data.getProduct());
        contentValues.put("measurement", data.getMeasurement());
        contentValues.put("quantity", data.getQuantity());
        contentValues.put("Synced", data.getSynced());
        contentValues.put("Status", data.getStatus());
        db.update(PAYMENTS, contentValues, "uid = ? ", new String[]{data.getUid()});
        return true;
    }
    public boolean updateData(Model_shipment data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", data.getUid());
        contentValues.put("modified", data.getSynced());
        contentValues.put("vss", data.getVss());
        contentValues.put("division", data.getDivision());
        contentValues.put("range", data.getRange());
        contentValues.put("processing_center", data.getProcessing_center());
        contentValues.put("date", data.getDate());
        contentValues.put("transitPass", data.getTransitPass());
        contentValues.put("vehicleType", data.getVehicleType());
        contentValues.put("vehicleNo", data.getVehicleNo());
        db.update(SHIPMENTS, contentValues, "uid = ? ", new String[]{data.getUid()});
        return true;
    }

    public int numberOfRows(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        return numRows;
    }

    public Integer deleteData(String table_name,String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name,
                "uid = ? ",
                new String[]{uid});
    }
    public Integer deleteData(String table_name,String coloumname,String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name,
                coloumname+" = ? ",
                new String[]{uid});
    }
    public void deleteData(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ table_name);
    }
    public List<FamilyData> getAllDataFromFamilyWhere(String where) {
        List<FamilyData> filesFolderDataModelArrayList = new ArrayList<FamilyData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FAMILYDETAILS+" WHERE "+where+"" , null);
        if (cursor.moveToFirst()) {
            do {
                FamilyData data = new FamilyData(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getInt(17),
                        cursor.getString(18),
                        cursor.getString(19)
                );
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        Log.i("getData","select * from " + FAMILYDETAILS+" WHERE "+where );
        return filesFolderDataModelArrayList;
    }

    public List<Collector> getAllDataFromCollectorsWhere(String where) {
        List<Collector> filesFolderDataModelArrayList = new ArrayList<Collector>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + COLLECTORS+" WHERE "+where+"" , null);
        if (cursor.moveToFirst()) {
            do {
                Collector data = new Collector(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getString(17),
                        cursor.getString(18),
                        cursor.getString(19),
                        cursor.getString(20),
                        cursor.getString(21),
                        cursor.getString(22),
                        cursor.getInt(23)
                );
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }

    public List<Inventory> getAllDataFromInventoriesWhere(String where) {
        List<Inventory> filesFolderDataModelArrayList = new ArrayList<Inventory>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + INVENTORY +" WHERE "+where+"" , null);
        if (cursor.moveToFirst()) {
            do {
                Inventory data = new Inventory(
                        cursor.getInt(cursor.getColumnIndex(MEMBER_ID)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getInt(14),
                        cursor.getInt(15),
                        cursor.getInt(16));
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }

    public List<Model_payment> getAllDataFromPaymentsWhere(String where) {
        List<Model_payment> filesFolderDataModelArrayList = new ArrayList<Model_payment>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PAYMENTS +" WHERE "+where+"" , null);
        if (cursor.moveToFirst()) {
            do {
                Model_payment data = new Model_payment(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getInt(14),
                        cursor.getInt(15));
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }
    public List<Model_shipment> getAllDataFromShipmentsWhere(String where) {
        List<Model_shipment> filesFolderDataModelArrayList = new ArrayList<Model_shipment>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + SHIPMENTS +" WHERE "+where+"" , null);
        if (cursor.moveToFirst()) {
            do {
                Model_shipment data = new Model_shipment(
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10));
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }
    public List<FamilyData> getAllDataFromFamily() {
        List<FamilyData> filesFolderDataModelArrayList = new ArrayList<FamilyData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+FAMILYDETAILS+" order by synced", null);
        if (cursor.moveToFirst()) {
            do {
                FamilyData data = new FamilyData(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getInt(17),
                        cursor.getString(17),
                        cursor.getString(18)

                );
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }

    public List<Collector> getAllDataFromCollectors() {
        List<Collector> filesFolderDataModelArrayList = new ArrayList<Collector>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " +COLLECTORS+ " order by synced " , null);
        if (cursor.moveToFirst()) {
            do {
                Collector data = new Collector(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getString(17),
                        cursor.getString(18),
                        cursor.getString(19),
                        cursor.getString(20),
                        cursor.getString(21),
                        cursor.getString(22),
                        cursor.getInt(23)
                );
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }

    public List<Inventory> getAllDataFromInventories() {
        List<Inventory> filesFolderDataModelArrayList = new ArrayList<Inventory>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + INVENTORY +" order by date desc", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                Inventory data = new Inventory(
                      cursor.getInt(cursor.getColumnIndex("memberId")),
                      cursor.getString(cursor.getColumnIndex("uid")),
                      cursor.getString(cursor.getColumnIndex("vss")),
                      cursor.getString(cursor.getColumnIndex("division")),
                      cursor.getString(cursor.getColumnIndex("range")),
                      cursor.getString(cursor.getColumnIndex("collector")),
                      cursor.getString(cursor.getColumnIndex("ntfp")),
                      cursor.getString(cursor.getColumnIndex("unit")),
                      cursor.getString(cursor.getColumnIndex("quantity")), cursor.getString(cursor.getColumnIndex("LoseAmound")),
                      cursor.getString(cursor.getColumnIndex("date")),
                      cursor.getString(cursor.getColumnIndex("amount")),
                      cursor.getString(cursor.getColumnIndex("ntfpType")),
                      cursor.getString(cursor.getColumnIndex("ntfpGrade")),
                      cursor.getInt(cursor.getColumnIndex("transit")),
                      cursor.getInt(cursor.getColumnIndex("payment")),
                      cursor.getInt(cursor.getColumnIndex("synced")));
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }

    public List<Model_payment> getAllDataFromPayments() {
        List<Model_payment> filesFolderDataModelArrayList = new ArrayList<Model_payment>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" select * from " + PAYMENTS + " order by DateTime desc ", null);
        if (cursor.moveToFirst()) {
            do {
                Model_payment data = new Model_payment(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getInt(14),
                        cursor.getInt(15));
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }

    public List<Model_shipment> getAllDataFromShipments() {
        List<Model_shipment> filesFolderDataModelArrayList = new ArrayList<Model_shipment>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + SHIPMENTS+" order by date desc" , null);
        if (cursor.moveToFirst()) {
            do {
                Model_shipment data = new Model_shipment(
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10));
                filesFolderDataModelArrayList.add(data);
            } while (cursor.moveToNext());
        }
        return filesFolderDataModelArrayList;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" delete from " + COLLECTORS);
        db.execSQL(" delete from " + FAMILYDETAILS);
        db.execSQL(" delete from " + INVENTORY);
        db.execSQL(" delete from " + PAYMENTS );
        db.execSQL(" delete from " + SHIPMENTS);
        db.execSQL(" delete from " + PROCESSING_CENTER);
    }


    public void Insert(TransitPassModel model){
        SQLiteDatabase db = this.getWritableDatabase();
            String Query = "Select * from " + TRANSIT_TABLE + " where " + TRANSITID + "='" + model.getTransUniqueId() + "'";
            Cursor cursor = db.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                //Save vssId,RamgeId,Divisionid here
                ContentValues contentValues = new ContentValues();
                contentValues.put(TRANSITID, model.getTransUniqueId());
                contentValues.put(DIVISION, model.getDivisionName());
                contentValues.put(RANGE, model.getRangeName());
                contentValues.put(RFO, model.getrFOName());
                contentValues.put(VSS, model.getvSSName());
                contentValues.put(DATE, model.getDate());
                contentValues.put(PCNAME, model.getpCName());
                contentValues.put(TRANSITSTATUS, model.getTransitStatus());
                contentValues.put(SHIPMENTSTATUS, model.getShipmentStatus());
                contentValues.put(FCMID, model.getfCMID());
                if (model.getnTFP() != null) {
                    for (TransitNTFPModel data : model.getnTFP()) {
                        ContentValues ntfpValues = new ContentValues();
                        ntfpValues.put(TRANSITID, model.getTransUniqueId());
                        ntfpValues.put(NTFPNAME, data.getnTFPName());
                        ntfpValues.put(MALAYALAM, data.getnTFPmalayalamname());
                        ntfpValues.put(UNIT, data.getUnit());
                        ntfpValues.put(QUANTITY, data.getQuantity());
                        ntfpValues.put(STOCKSID, data.getStocksId());

                        db.insert(TRANSIT_NTFP_TABLE, null, ntfpValues);
                    }
                }
                db.insert(TRANSIT_TABLE, null, contentValues);
            }

    }
    public Cursor getSingleRow(String table, String selection, String arg) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(table, null, selection + " = ?", new String[]{arg}, null, null, null);
    }

    public List<String> getTransitIds(){
        List<String> transits = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from " + TRANSIT_TABLE +" where "+SHIPMENTSTATUS+" = 'Pending'" , null);
        if (cursor.moveToFirst()) {
            do {
                transits.add(cursor.getString(cursor.getColumnIndex(TRANSITID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transits;
    }

    public List<TransitPassModel> getTransitPassModels(){
        List<TransitPassModel> transitPassModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from " + TRANSIT_TABLE , null);
        if (cursor.moveToFirst()) {
            do {
                TransitPassModel model=new TransitPassModel();
                model.setTransUniqueId(cursor.getString(cursor.getColumnIndex(TRANSITID)));
                model.setDivisionName(cursor.getString(cursor.getColumnIndex(DIVISION)));
                model.setRangeName(cursor.getString(cursor.getColumnIndex(RANGE)));
                model.setrFOName(cursor.getString(cursor.getColumnIndex(RFO)));
                model.setvSSName(cursor.getString(cursor.getColumnIndex(VSS)));
                model.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                model.setpCName(cursor.getString(cursor.getColumnIndex(PCNAME)));
                model.setTransitStatus(cursor.getString(cursor.getColumnIndex(TRANSITSTATUS)));
                model.setShipmentStatus(cursor.getString(cursor.getColumnIndex(SHIPMENTSTATUS)));
                model.setfCMID(cursor.getString(cursor.getColumnIndex(FCMID)));
                Cursor NTFPCursor=db.rawQuery("select * from " + TRANSIT_NTFP_TABLE+"where "+TRANSITID+" = "+model.getTransUniqueId() , null);
                List<TransitNTFPModel> NTFPModels=new ArrayList<>();
                if (NTFPCursor.moveToFirst()) {
                    do {
                        TransitNTFPModel data=new TransitNTFPModel();
                        data.setnTFPName(NTFPCursor.getString(NTFPCursor.getColumnIndex(NTFPNAME)));
                        data.setnTFPmalayalamname(NTFPCursor.getString(NTFPCursor.getColumnIndex(MALAYALAM)));
                        data.setUnit(NTFPCursor.getString(NTFPCursor.getColumnIndex(UNIT)));
                        data.setQuantity(NTFPCursor.getString(NTFPCursor.getColumnIndex(QUANTITY)));
                        data.setStocksId(NTFPCursor.getInt(NTFPCursor.getColumnIndex(STOCKSID)));

                        NTFPModels.add(data);
                    } while (NTFPCursor.moveToNext());
                }
                model.setnTFP(NTFPModels);
                NTFPCursor.close();
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transitPassModels;
    }

    public List<TransitNTFPModel> getTransitNTFPModels(String transid){

        List<TransitNTFPModel> NTFPModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor NTFPCursor=db.rawQuery("select * from " + TRANSIT_NTFP_TABLE+" where "+TRANSITID+" = '"+transid.trim()+"'", null);

        if (NTFPCursor.moveToFirst()) {
            do {
                Log.i("cirsss",NTFPCursor.getColumnIndex(STOCKSID)+"");

                TransitNTFPModel data=new TransitNTFPModel();
                data.setnTFPName(NTFPCursor.getString(NTFPCursor.getColumnIndex(NTFPNAME)));
                data.setnTFPmalayalamname(NTFPCursor.getString(NTFPCursor.getColumnIndex(MALAYALAM)));
                data.setUnit(NTFPCursor.getString(NTFPCursor.getColumnIndex(UNIT)));
                data.setQuantity(NTFPCursor.getString(NTFPCursor.getColumnIndex(QUANTITY)));
                data.setStocksId(NTFPCursor.getInt(NTFPCursor.getColumnIndex(STOCKSID)));

                NTFPModels.add(data);
            } while (NTFPCursor.moveToNext());
        }
        NTFPCursor.close();
        return NTFPModels;
    }

}
