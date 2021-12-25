package com.pradeep.app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DishApi {
    String BASE_URL = "https://demo1256134.mockable.io/";
    @GET("dishesoftheday")
    Call<DishDetail> getDish();
    @GET("dishes/{id}")
    Call<DishInformation> getDishDetails(@Path("id") long id);
}
