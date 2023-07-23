package com.example.tablereservation.api.restaurantAPI;

import com.example.tablereservation.api.APIClient;
import com.example.tablereservation.model.Restaurant;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestaurantAPIService {
    @GET("restaurant")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurant/admin")
    Call<List<Restaurant>> getAllRestaurants(@Header("Authorization") String bearerToken);

    @GET("restaurant/{id}")
    Call<Restaurant> getRestaurantById(@Path("id") String restaurantID);

    @GET("restaurant/restaurantOwner/{id}")
    Call<List<Restaurant>> getRestaurantsByResOwnerId(@Header("Authorization") String bearerToken,
                                                      @Path("id") String resOwnerID);

    @GET("restaurant/{id}/feedbacks")
    Call<List<FeedbackResponse>> getFeedbackListByRestaurantId(@Path("id") String restaurantID);

    @POST("restaurant")
    Call<RestaurantRequest> createRestaurant(@Header("Authorization") String bearerToken,
                                      @Body RestaurantRequest restaurantRequest);
    @PATCH("restaurant/{id}")
    Call<RestaurantRequest> updateRestaurant(@Path("id") String restaurantID,@Body RestaurantRequest restaurantRequest);
    @PATCH("restaurant/{id}")
    Call<Void> deactiveOrActiveRestaurant(@Path("id") String restaurantID, @Body Map<String, Object> requestBody);
    @DELETE("restaurant/{id}")
    Call<Void> deleteRestaurantById(@Header("Authorization") String bearerToken, @Path("id") String restaurantID);
}
