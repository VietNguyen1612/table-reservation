package com.example.tablereservation.api.restaurantAPI;

import com.example.tablereservation.api.reservationAPI.Customer;
import com.example.tablereservation.model.User;
import com.google.gson.annotations.SerializedName;

public class FeedbackResponse {
    @SerializedName("customerId")
    private  User user;
    private String content;

    public FeedbackResponse(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

