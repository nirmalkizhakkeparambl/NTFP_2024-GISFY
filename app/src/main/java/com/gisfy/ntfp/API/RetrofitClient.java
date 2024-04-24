package com.gisfy.ntfp.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    private static RetrofitClient instance = null;
    private Api myApi;
    private DashBoardAPIs dashBoardAPIs;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);

        Retrofit dashboardretrofit = new Retrofit.Builder().baseUrl(DashBoardAPIs.DashboardBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dashBoardAPIs = dashboardretrofit.create(DashBoardAPIs.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
    public DashBoardAPIs getDashboardApi() {
        return dashBoardAPIs;
    }

}
