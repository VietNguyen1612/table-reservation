package com.example.tablereservation.api.tableAPI;

import com.example.tablereservation.api.APIClient;

public class TableAPI {
    public static TableAPIService retrofitService = APIClient.retrofit.create(TableAPIService.class);
}
