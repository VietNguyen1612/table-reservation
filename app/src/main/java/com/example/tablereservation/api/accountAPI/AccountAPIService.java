package com.example.tablereservation.api.accountAPI;


import com.example.tablereservation.model.Account;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountAPIService {
    @Headers({
            "Content-Type: application/json"
    })
    @POST("account/login")
    Call<LoginResponse> login(@Body Account account);

    @Headers({
            "Content-Type: application/json"
    })
    @POST("account/register")
    Call<Account> register(@Body RequestBody requestBody);

    @GET("account")
    Call<List<Account>> getAllAccounts(@Header("Authorization") String bearerToken);

    @PATCH("account/ban/{id}")
    Call<Void> banAccount(@Header("Authorization") String bearerToken, @Path("id") String acountID);

    @PATCH("account/unban/{id}")
    Call<Void> unbanAccount(@Header("Authorization") String bearerToken, @Path("id") String acountID);
}
