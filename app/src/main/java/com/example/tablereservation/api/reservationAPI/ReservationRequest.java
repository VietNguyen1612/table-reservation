package com.example.tablereservation.api.reservationAPI;

public class ReservationRequest {
    private String arrivedDate;
    private String duration;
    private int guessNum;
    private String note;
    private String restaurant;
    private String area;

    public ReservationRequest(String arrivedDate, String duration, int guessNum, String note, String restaurant, String area) {
        this.arrivedDate = arrivedDate;
        this.duration = duration;
        this.guessNum = guessNum;
        this.note = note;
        this.restaurant = restaurant;
        this.area = area;
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

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
