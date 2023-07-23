package com.example.tablereservation.listener;

import com.example.tablereservation.model.Restaurant;

public interface IOnOwnerResListener {
    void onClickUpdateRes(Restaurant restaurant);
    void onClickDeleteRes(Restaurant restaurant);
}
