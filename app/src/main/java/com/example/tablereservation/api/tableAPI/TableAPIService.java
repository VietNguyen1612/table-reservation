package com.example.tablereservation.api.tableAPI;

import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.model.Table;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TableAPIService {
    @GET("table/restaurantId/{id}")
    Call<List<Table>> getTablesByResId(@Path("id") String resID);

    @GET("table")
    Call<List<Table>> getAllTables();

    @POST("table")
    Call<TableRequest> createTable(@Header("Authorization") String bearerToken,
                                   @Body TableRequest tableRequest);

    @DELETE("table/{id}")
    Call<Void> deleteTableById(@Header("Authorization") String bearerToken, @Path("id") String tableID);
}
