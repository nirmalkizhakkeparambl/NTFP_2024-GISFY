package com.gisfy.ntfp.API;
import com.gisfy.ntfp.VSS.Dashboard.DashboardShipmentModel;
import com.gisfy.ntfp.VSS.Dashboard.DashboardTransitModel;
import com.gisfy.ntfp.VSS.Dashboard.PaymentsModel;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DashBoardAPIs {
    String DashboardBaseUrl = "http://vanasree.com/NTFPAPI/API/Dashboard/";
    @POST("TransitpassData")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<DashboardTransitModel>> TransitpassData(@Body HashMap<String, String> hashMap);

    @POST("ShipmentsData")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<DashboardShipmentModel>> ShipmentsData(@Body HashMap<String, String> hashMap);

    @POST("PCPaymentsData")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<PaymentsModel>> PCPaymentsData(@Body HashMap<String, String> hashMap);
}
