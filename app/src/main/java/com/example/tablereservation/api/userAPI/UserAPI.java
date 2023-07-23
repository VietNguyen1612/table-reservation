package com.example.tablereservation.api.userAPI;

import com.example.tablereservation.api.APIClient;

public class UserAPI {
    public static UserAPIService retrofitService = APIClient.retrofit.create(UserAPIService.class);
}
