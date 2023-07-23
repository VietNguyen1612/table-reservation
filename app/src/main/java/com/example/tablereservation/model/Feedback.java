package com.example.tablereservation.model;

public class Feedback {
    private String _id;
    private String customerId;
    private String reservationId;
    private String content;

    public Feedback(String customerId, String reservationId, String content) {
        this.customerId = customerId;
        this.reservationId = reservationId;
        this.content = content;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
