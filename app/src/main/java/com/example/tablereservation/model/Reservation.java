package com.example.tablereservation.model;

import java.io.Serializable;
import java.util.List;

public class Reservation implements Serializable {
    private String _id;
    private String arrivedDate;
    private String duration;
    private int guessNum;
    private String note;

    private String restaurant;

    private String status;

    private List<String> feedback;
    private String table;

    public Reservation(String arrivedDate, String duration, int guessNum, String note, String restaurant) {
        this.arrivedDate = arrivedDate;
        this.duration = duration;
        this.guessNum = guessNum;
        this.note = note;
        this.restaurant = restaurant;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
