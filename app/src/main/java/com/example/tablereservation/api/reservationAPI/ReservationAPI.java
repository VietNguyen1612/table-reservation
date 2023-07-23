package com.example.tablereservation.api.reservationAPI;

import com.example.tablereservation.api.APIClient;

public class ReservationAPI {
    public static ReservationAPIService retrofitInstance = APIClient.retrofit.create(ReservationAPIService.class);
}
