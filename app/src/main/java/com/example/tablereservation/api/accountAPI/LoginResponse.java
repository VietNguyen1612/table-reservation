package com.example.tablereservation.api.accountAPI;

import com.example.tablereservation.model.Account;

public class LoginResponse {
    private String token;
    private Account account;
    public String getToken() {
        return token;
    }

    public Account getAccount(){
        return account;
    }
}
