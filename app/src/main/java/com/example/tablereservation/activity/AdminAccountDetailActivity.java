package com.example.tablereservation.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tablereservation.adapter.AdminAccountBookingHistoryAdapter;
import com.example.tablereservation.adapter.TableAdapter;
import com.example.tablereservation.api.accountAPI.AccountAPI;
import com.example.tablereservation.api.reservationAPI.ReservationAPI;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.api.userAPI.UserAPI;
import com.example.tablereservation.databinding.ActivityAdminAccountDetailBinding;
import com.example.tablereservation.databinding.ActivityRestaurantDetailBinding;
import com.example.tablereservation.listener.IOnClickAdminAccountFeedbackListener;
import com.example.tablereservation.listener.IOnClickTableItemListener;
import com.example.tablereservation.model.Account;
import com.example.tablereservation.model.Feedback;
import com.example.tablereservation.model.Reservation;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.model.Table;
import com.example.tablereservation.model.User;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.StringUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAccountDetailActivity extends BaseActivity{
    private ActivityAdminAccountDetailBinding mActivityAdminAccountDetailBinding;
    private Account mAccount;
    private List<Reservation> mListReservations;
    private List<Restaurant> mListRestaurants;
    private List<Feedback> mListFeedbacks;
    private static ProgressDialog mProgressDialog;
    private User customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminAccountDetailBinding = ActivityAdminAccountDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminAccountDetailBinding.getRoot());
        mProgressDialog = new ProgressDialog(this);
        getDataIntent();
        if(StringUtil.isEmpty(mAccount.getCustomer())){
            getCustomer();
        } else {
            getListFeedbacks();
        }
        initToolbar();
    }

    private void initToolbar() {
        mActivityAdminAccountDetailBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAdminAccountDetailBinding.toolbar.tvTitle.setText("Chi tiết tài khoản");

        mActivityAdminAccountDetailBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAccount= (Account) bundle.get("account_object");
        }
    }

    private void displayAccountDetail(){
        if(StringUtil.isEmpty(mAccount.getCustomer())){
            mActivityAdminAccountDetailBinding.tvLabelBookingList.setVisibility(View.GONE);
        }
            mActivityAdminAccountDetailBinding.tvName.setText(customer.getName());
            mActivityAdminAccountDetailBinding.tvAddress.setText(customer.getAddress());
            mActivityAdminAccountDetailBinding.tvPhone.setText(customer.getPhone());
            mActivityAdminAccountDetailBinding.tvStatus.setText(mAccount.getStatus());
            if(mAccount.getStatus().equals("active")){
                mActivityAdminAccountDetailBinding.tvActiveOrBan.setText("khóa tài khoản");
                mActivityAdminAccountDetailBinding.tvActiveOrBan.setOnClickListener(v ->
                        banAccount());
            } else {
                mActivityAdminAccountDetailBinding.tvActiveOrBan.setText("Mở tài khoản");
                mActivityAdminAccountDetailBinding.tvActiveOrBan.setOnClickListener(v ->
                        unbanAccount());
            }


        if(!StringUtil.isEmpty(mAccount.getCustomer())){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mActivityAdminAccountDetailBinding.rcvOrder.setLayoutManager(linearLayoutManager);
            AdminAccountBookingHistoryAdapter adapter = new AdminAccountBookingHistoryAdapter(mListReservations, mListRestaurants, mListFeedbacks, new IOnClickAdminAccountFeedbackListener() {
                @Override
                public void onClick(Feedback feedback, Reservation reservation) {
                    new AlertDialog.Builder(AdminAccountDetailActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Sau khi xóa feedback sẽ không khôi phục được")
                            .setPositiveButton("Đồng ý", ((dialogInterface, i) -> {
                                deleteFeedback(feedback,reservation);
                            }))
                            .setNegativeButton("Hủy bỏ",null)
                            .show();
                }
            });
            mActivityAdminAccountDetailBinding.rcvOrder.setAdapter(adapter);
        }
    }

    private void banAccount(){
        mProgressDialog.show();
        String token = "Bearer "+DataStoreManager.getToken();
        Call<Void> call = AccountAPI.retrofitService.banAccount(token,mAccount.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AdminAccountDetailActivity.this,
                            "Khóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminAccountDetailActivity.this,
                            "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void unbanAccount(){
        mProgressDialog.show();
        String token = "Bearer "+ DataStoreManager.getToken();
        Call<Void> call = AccountAPI.retrofitService.unbanAccount(token,mAccount.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AdminAccountDetailActivity.this,
                            "Mở thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(AdminAccountDetailActivity.this,
                            "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCustomer(){
        if(StringUtil.isEmpty(mAccount.getCustomer())){
            mProgressDialog.show();
            Call<User> call = UserAPI.retrofitService.getResOwnerById(mAccount.getRestaurantOwner());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        customer = response.body();
                        displayAccountDetail();
                    } else {
                        Log.d("error in customer",response.toString());
                    }
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Call<User> call = UserAPI.retrofitService.getCustomerById(mAccount.getCustomer());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        customer = response.body();
                    } else {
                        Log.d("error in customer",response.toString());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getListFeedbacks(){
        mProgressDialog.show();
        String token = "Bearer "+ DataStoreManager.getToken();
        Call<List<Feedback>> call = ReservationAPI.retrofitInstance.getListFeedbacksForAdmin(token);
        call.enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                if(response.isSuccessful()){
                    mListFeedbacks = response.body();
                    getCustomer();
                    getListReservations();
                } else {
                    Log.d("error",response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getListReservations(){
        Call<List<Reservation>> call = ReservationAPI.retrofitInstance.getListReservationByCustomerId(mAccount.getCustomer());
        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if(response.isSuccessful()){
                    mListReservations = response.body();
                    getListRestaurants();
                } else {
                    Log.d("Err in reserv AdminAccDetailActi",response.toString());
                }
            }
            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getListRestaurants(){
        String token = "Bearer "+ DataStoreManager.getToken();
        Call<List<Restaurant>> call = RestaurantAPI.retrofitService.getAllRestaurants(token);
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if(response.isSuccessful()){
                    mListRestaurants = response.body();
                    displayAccountDetail();
                } else {
                    Log.d("Err in restaurant AdminAccDetailActi",response.toString());
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteFeedback(Feedback feedback, Reservation reservation){
        Log.d("fbId",feedback.get_id());
        Log.d("rsId",reservation.get_id());
        String token = "Bearer "+ DataStoreManager.getToken();
        Call<Void> call = ReservationAPI.retrofitInstance.deleteFeedbackById(token,reservation.get_id(), feedback.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AdminAccountDetailActivity.this,
                            "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AdminAccountDetailActivity.this,
                            "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
