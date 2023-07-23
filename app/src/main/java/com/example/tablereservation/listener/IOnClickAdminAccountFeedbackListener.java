package com.example.tablereservation.listener;

import com.example.tablereservation.model.Feedback;
import com.example.tablereservation.model.Reservation;

public interface IOnClickAdminAccountFeedbackListener {
    void onClick(Feedback feedback,Reservation reservation);
}
