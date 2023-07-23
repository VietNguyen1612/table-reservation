package com.example.tablereservation.listener;

import com.example.tablereservation.api.reservationAPI.ReservationResOwnerResponse;
import com.example.tablereservation.model.Reservation;

public interface IOnClickResOwnerReservationItemListener {
    void onClickCancelReservation(ReservationResOwnerResponse reservation);
    void onClickConfirmReservation(ReservationResOwnerResponse reservation);
}
