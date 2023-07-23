package com.example.tablereservation.api.restaurantAPI;

import com.example.tablereservation.api.APIClient;

public class RestaurantAPI {
    public static RestaurantAPIService retrofitService = APIClient.retrofit.create(RestaurantAPIService.class);
}
