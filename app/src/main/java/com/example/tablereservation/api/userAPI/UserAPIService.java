package com.example.tablereservation.api.userAPI;

import com.example.tablereservation.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAPIService {

    @GET("customer/{id}")
    Call<User> getCustomerById(@Path("id") String customerID);

    @GET("restaurantOwner/{id}")
    Call<User> getResOwnerById(@Path("id") String resOwnerID);
}
