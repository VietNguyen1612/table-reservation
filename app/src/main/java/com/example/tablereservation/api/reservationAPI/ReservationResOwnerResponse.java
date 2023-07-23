package com.example.tablereservation.api.reservationAPI;

import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.model.Table;
import com.example.tablereservation.model.User;

public class ReservationResOwnerResponse {
    private String _id;
    private String arrivedDate;
    private String duration;
    private int guessNum;
    private String note;
    private Restaurant restaurant;
    private String status;
    private Table table;
    private Customer customer;

    public ReservationResOwnerResponse(String arrivedDate, String duration, int guessNum, String note,
                                       Restaurant restaurant, String status, Table table,  Customer customer) {
        this.arrivedDate = arrivedDate;
        this.duration = duration;
        this.guessNum = guessNum;
        this.note = note;
        this.restaurant = restaurant;
        this.status = status;
        this.table = table;
        this.customer = customer;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getArrivedDate() {
        return arrivedDate;
    }

    public void setArrivedDate(String arrivedDate) {
        this.arrivedDate = arrivedDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getGuessNum() {
        return guessNum;
    }

    public void setGuessNum(int guessNum) {
        this.guessNum = guessNum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
