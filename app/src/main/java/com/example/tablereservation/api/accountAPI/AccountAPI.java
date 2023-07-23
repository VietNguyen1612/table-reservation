package com.example.tablereservation.api.accountAPI;

import com.example.tablereservation.api.APIClient;

public class AccountAPI {
    public static AccountAPIService retrofitService = APIClient.retrofit.create(AccountAPIService.class);
}
