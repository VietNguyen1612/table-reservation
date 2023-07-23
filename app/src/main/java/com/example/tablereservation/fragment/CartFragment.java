package com.example.tablereservation.fragment;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tablereservation.R;
import com.example.tablereservation.activity.AddFeedbackActivity;
import com.example.tablereservation.activity.MainActivity;
import com.example.tablereservation.adapter.BookingHistoryAdapter;
import com.example.tablereservation.api.reservationAPI.ReservationAPI;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.api.tableAPI.TableAPI;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.FragmentCartBinding;
import com.example.tablereservation.model.Reservation;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.model.Table;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends BaseFragment {
    int NOTIFICATION_ID=1;
    private FragmentCartBinding mFragmentCartBinding;
    private static ProgressDialog mProgressDialog;
    private static String mCustomerId;
    private static List<Reservation> mReservationList;
    private static List<Restaurant> mListRestaurants;

    private static List<Table> mListTables;

    private static boolean isNotiSent = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        getListReservation();
        return mFragmentCartBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Lịch sử đặt bàn");
        }
    }

    private void displayReservations(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFragmentCartBinding.rcvBookingHistory.setLayoutManager(layoutManager);
        BookingHistoryAdapter mBookingHistoryAdapter = new BookingHistoryAdapter(mReservationList,mListRestaurants, mListTables, this::goToAddFeedback);
        mFragmentCartBinding.rcvBookingHistory.setAdapter(mBookingHistoryAdapter);
    }

    private void goToAddFeedback(@NonNull Reservation reservation){
        if(reservation.getStatus().equals("completed")){
            Toast.makeText(getContext(), "Bạn đã feedback rồi",
                    Toast.LENGTH_SHORT).show();
        } else if(reservation.getStatus().equals("pending")){
            Toast.makeText(getContext(), "Đơn của bạn chưa được duyệt",
                    Toast.LENGTH_SHORT).show();
        } else {
            if(reservation.getFeedback().isEmpty()){
                Bundle bundle = new Bundle();
                bundle.putSerializable("reservation_object",reservation);
                GlobalFunction.startActivity(getActivity(), AddFeedbackActivity.class,bundle);
            }
        }
    }

    private  void getListTables(){
        Call<List<Table>> call = TableAPI.retrofitService.getAllTables();
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                if(response.isSuccessful()){
                    mListTables = response.body();
                    displayReservations();
                    int count = 0;
                    for (Reservation reservation: mReservationList) {
                        if(reservation.getStatus().equals("confirm")){
                            count++;
                        }
                    }
                    if(count!=0 && isNotiSent==false){
                        sendNotification("Bạn có "+count+" đơn đặt bàn chưa feedback");
                        isNotiSent = true;
                    }
                } else {
                    Log.d("error",response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getListReservation(){
        mProgressDialog.show();
        if(!StringUtil.isEmpty(DataStoreManager.getCustomerID())){
            mCustomerId = DataStoreManager.getCustomerID();
        }
        Call<List<Reservation>> call = ReservationAPI.retrofitInstance.getListReservationByCustomerId(mCustomerId);
        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if(response.isSuccessful()){
                    mReservationList = response.body();
                    getListRestaurant();
                    mProgressDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Lỗi đã xảy ra, vui lòng thử lại sau",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getListRestaurant(){
        Call<List<Restaurant>> call = RestaurantAPI.retrofitService.getRestaurants();
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.isSuccessful()) {
                    mListRestaurants = response.body();
                    getListTables();
                } else {
                    Toast.makeText(getContext(), "Lỗi đã xảy ra, vui lòng thử lại sau",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getListReservation();
    }

    private void sendNotification(String msg) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);

        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        Date currentDate = new Date();

// Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");

// Format the date using the desired format
        String formattedDate = dateFormat.format(currentDate);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getContext(), "channel_id")
                    .setContentTitle("Thông báo từ reservation system\t\t\t\t\t\t\t"+formattedDate)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.baseline_message_24)
                    .setLargeIcon(bitmap)
                    .build();
        }

        NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.notify(getNotificationId(), notification);
        }
    }
    private int getNotificationId(){
        return (int) new Date().getTime();
    }
}