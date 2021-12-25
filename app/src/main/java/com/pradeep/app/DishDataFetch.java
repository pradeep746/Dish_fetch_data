package com.pradeep.app;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DishDataFetch {
    private static DishDataFetch instance = null;
    private DishApi myApi;

    private DishDataFetch() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DishApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(DishApi.class);
    }

    public static synchronized DishDataFetch getInstance() {
        if (instance == null) {
            instance = new DishDataFetch();
        }
        return instance;
    }

    public DishApi getMyApi() {
        return myApi;
    }
}
