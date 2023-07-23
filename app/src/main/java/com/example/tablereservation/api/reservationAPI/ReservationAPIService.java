package com.example.tablereservation.api.reservationAPI;

import com.example.tablereservation.api.accountAPI.LoginResponse;
import com.example.tablereservation.model.Account;
import com.example.tablereservation.model.Feedback;
import com.example.tablereservation.model.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationAPIService {
    @Headers({
            "Content-Type: application/json"
    })
    @POST("reservation")
    Call<ReservationRequest> createReservation(@Header("Authorization") String bearerToken, @Body ReservationRequest reservation);

    @GET("reservation/customer/{id}")
    Call<List<Reservation>> getListReservationByCustomerId(@Path("id") String customerID);

    @GET("reservation/restaurant/{id}")
    Call<List<ReservationResOwnerResponse>> getListReservationByRestaurantId(@Path("id") String restaurantID);

    @GET("reservation/restaurantOwner/{id}")
    Call<List<ReservationResOwnerResponse>> getListReservationByRestaurantOwnerId(@Path("id") String restaurantOwnerID);

    @Headers({
            "Content-Type: application/json"
    })
    @PATCH("reservation/{id}")
    Call<Void> updateReservation(@Path("id") String reservationID,@Body ReservationResOwnerResponse reservationResOwnerResponse);

    @GET("reservation/feedback/admin")
    Call<List<Feedback>> getListFeedbacksForAdmin(@Header("Authorization") String bearerToken);

    @DELETE("reservation/{reservationId}/feedback/{feedbackId}")
    Call<Void> deleteFeedbackById(@Header("Authorization") String bearerToken,
                                  @Path("reservationId") String reservationID,@Path("feedbackId") String feedbackID);

    @POST("reservation/{id}/feedback")
    Call<Void> addFeedback(@Header("Authorization") String bearerToken,@Path("id") String reservationID, @Body Feedback feedback);
}
