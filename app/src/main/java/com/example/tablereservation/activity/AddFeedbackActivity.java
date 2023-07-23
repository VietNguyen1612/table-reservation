package com.example.tablereservation.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tablereservation.api.reservationAPI.ReservationAPI;
import com.example.tablereservation.databinding.ActivityAddFeedbackBinding;
import com.example.tablereservation.databinding.ActivityAddTableBinding;
import com.example.tablereservation.model.Feedback;
import com.example.tablereservation.model.Reservation;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.StringUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFeedbackActivity extends BaseActivity{
    private ActivityAddFeedbackBinding mActivityAddFeedbackBinding;
    private Reservation reservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddFeedbackBinding = ActivityAddFeedbackBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddFeedbackBinding.getRoot());
        initToolbar();
        getDataIntent();
        mActivityAddFeedbackBinding.btnAdd.setOnClickListener(v -> addFeedback());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            reservation = (Reservation) bundleReceived.get("reservation_object");
        }
    }

    private void initToolbar(){
        mActivityAddFeedbackBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAddFeedbackBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void addFeedback(){
        String token = "Bearer "+ DataStoreManager.getToken();
        String content = mActivityAddFeedbackBinding.edtFeedback.getText().toString();
        String cusId = DataStoreManager.getCustomerID();
        if(StringUtil.isEmpty(content)){
            Toast.makeText(AddFeedbackActivity.this,
                    "Vui lòng nhập feedback", Toast.LENGTH_SHORT).show();
            return;
        }
        Feedback feedback = new Feedback(cusId,reservation.get_id(),content);
        Call<Void> call = ReservationAPI.retrofitInstance.addFeedback(token,reservation.get_id(),feedback);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(AddFeedbackActivity.this,
                            "Feedback thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddFeedbackActivity.this,
                            "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    Log.d("error",response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
