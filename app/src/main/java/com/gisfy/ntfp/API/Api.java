package com.gisfy.ntfp.API;

import com.gisfy.ntfp.Collectors.CollectorStockModel;
import com.gisfy.ntfp.HomePage.CollectorNames;
import com.gisfy.ntfp.HomePage.NTFPModel;
import com.gisfy.ntfp.HomePage.PCModel;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.PaymentsModel;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.ShipmentModel;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.DFOModel;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.RFO.Models.VSSModel;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorPaymentsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryEntity;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.Entity.NTFP;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.FamilyData;
import com.gisfy.ntfp.VSS.Payment.Recieved.ReceivedPaymentsModel;
import com.gisfy.ntfp.VSS.RequestForm.RFOModel;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://vanasree.com/NTFPAPI/API/";

    @POST("VSS")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<VSSUser>> getVSSUser(@Body HashMap<String, String> fields);

    @POST("ForgotPassword")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<VSSUser>> vssforgotpass(@Body HashMap<String, String> fields);

    @POST("VSSList")
    Call<List<VSSUser>> getAllVss();

    @POST("ForgotPassword")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<RFOUser>> rfoforgotpass(@Body HashMap<String, String> fields);

    @POST("UpdatePassword")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<StatusModel>> passReset(@Body HashMap<String, String> fields);

    @POST("RFO")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<RFOUser>> getRFOUser(@Body HashMap<String, String> fields);

    @POST("COllectorLogin")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<CollectorUser>> getCollectorUser(@Body HashMap<String, String> fields);

    @POST("MemberList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<MemberModel>> getMemberList(@Body HashMap<String, String> fields);


    @POST("CollectorPayment")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<CollectorPaymentsModel>> getCollectorPayments(@Body HashMap<String, String> fields);

    @POST("ForgotPassword")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<CollectorUser>> collectorForgotpass(@Body HashMap<String, String> fields);

    @POST("TransitSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<TransitPassModel>> getTransitPass(@Body HashMap<String, String> fields);

    @POST("ToDFOSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<DFOModel>> getToDFO(@Body HashMap<String, String> fields);

    @POST("ToVSSSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<VSSModel>> getVSS(@Body HashMap<String, String> fields);

    @POST("NTFPList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<NTFP>> getNTFPS(@Body HashMap<String, String> fields);

    @POST("PCList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<PCModel>> getPCS(@Body HashMap<String, String> fields);

    @POST("CollectorSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<CollectorsModel>> getCollectors(@Body HashMap<String, String> fields);


    @POST("RFOSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<RFOModel>> getRFOs(@Body HashMap<String, String> fields);

    @POST("DFOAck")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<DFOACKModel>> getDFOACK(@Body HashMap<String, String> fields);

    @POST("VSSAck")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<VSSACKModel>> getVSSACK(@Body HashMap<String, String> fields);

    @POST("RFOVSSAckStatus")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<StatusModel>> setVSSACK(@Body HashMap<String, String> fields);

    @POST("RFODFOAckStatus")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<StatusModel>> setDFOACK(@Body HashMap<String, String> fields);

    @POST("RFOTransit")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<StatusModel>> setTransitAck(@Body HashMap<String, String> fields);


    @POST("StocksSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<StocksModel>> stockSelect(@Body HashMap<String, String> fields);

    @POST("CollectorInventoryList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<InventoryEntity>> InventoryEntitySelect(@Body HashMap<String, String> fields);

    @POST("ShipmentSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<ShipmentModel>> shipmentSelect(@Body HashMap<String, String> fields);

    @POST("CollectorList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<Collector>> CollectorSelect(@Body HashMap<String, String> fields);

    @POST("MemberList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<FamilyData>> MemberListSelect(@Body HashMap<String, String> fields);

    @POST("PaymentSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<PaymentsModel>> paymentsSelect(@Body HashMap<String, String> fields);

    @POST("PaymentSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<ReceivedPaymentsModel>> paymentsReceived(@Body HashMap<String, String> fields);

    @POST("VSSPaymentStatus")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<StatusModel>> VSSPaymentStatus(@Body HashMap<String, String> fields);

    @POST("CollectorStockSelect")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<CollectorStockModel>> getCollectorStocksfromcollector(@Body HashMap<String, Integer> fields);

    @POST("StockRequestsFromCollector")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<CollectorStockModel>> getCollectorStocksfromvss(@Body HashMap<String, String> fields);
}
