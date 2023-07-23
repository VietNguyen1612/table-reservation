package com.example.tablereservation.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://restaurant-reservation-o9fn.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
