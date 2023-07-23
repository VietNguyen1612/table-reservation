package com.example.tablereservation.fragment.resOwner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.activity.MainActivity;
import com.example.tablereservation.activity.ResOwnerMainActivity;
import com.example.tablereservation.adapter.ResOwnerReservationAdapter;
import com.example.tablereservation.api.reservationAPI.ReservationAPI;
import com.example.tablereservation.api.reservationAPI.ReservationResOwnerResponse;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.databinding.FragmentCartBinding;
import com.example.tablereservation.databinding.FragmentResOwnerCartBinding;
import com.example.tablereservation.fragment.BaseFragment;
import com.example.tablereservation.listener.IOnClickResOwnerReservationItemListener;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.prefs.DataStoreManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResOwnerCartFragment extends BaseFragment {

    private FragmentResOwnerCartBinding mFragmentResOwnerCartBinding;
    private List<ReservationResOwnerResponse> mReservationList;
    private static ProgressDialog mProgressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentResOwnerCartBinding = FragmentResOwnerCartBinding.inflate(inflater, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        getListReservations();
        return mFragmentResOwnerCartBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((ResOwnerMainActivity) getActivity()).setToolBar("Đơn hàng của tôi");
        }
    }

    private void getListReservations(){
        mProgressDialog.show();
        String resOwnerId = DataStoreManager.getResOwnerID();
            Call<List<ReservationResOwnerResponse>> call = ReservationAPI.retrofitInstance.getListReservationByRestaurantOwnerId(resOwnerId);
            call.enqueue(new Callback<List<ReservationResOwnerResponse>>() {
                @Override
                public void onResponse(Call<List<ReservationResOwnerResponse>> call, Response<List<ReservationResOwnerResponse>> response) {
                    if(response.isSuccessful()){
                        mReservationList = response.body();
                        displayReservations();
                    } else {
                        Toast.makeText(getContext(), "Lỗi đã xảy ra, vui lòng thử lại sau",
                                Toast.LENGTH_SHORT).show();
                    }
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<ReservationResOwnerResponse>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    private void displayReservations(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFragmentResOwnerCartBinding.rcvOrder.setLayoutManager(layoutManager);
        ResOwnerReservationAdapter resOwnerReservationAdapter = new ResOwnerReservationAdapter(mReservationList, new IOnClickResOwnerReservationItemListener() {
            @Override
            public void onClickCancelReservation(ReservationResOwnerResponse reservation) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Xác nhận hủy đơn")
                        .setMessage("Xác nhận hủy đơn của khách hàng")
                        .setPositiveButton("Đồng ý", ((dialogInterface, i) -> {
                            cancelReservation(reservation);
                        }))
                        .setNegativeButton("Hủy bỏ",null)
                        .show();
            }

            @Override
            public void onClickConfirmReservation(ReservationResOwnerResponse reservation) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Xác nhận duyệt đơn")
                        .setMessage("Xác nhận duyệt đơn của khách hàng")
                        .setPositiveButton("Đồng ý", ((dialogInterface, i) -> {
                            confirmReservation(reservation);
                        }))
                        .setNegativeButton("Hủy bỏ",null)
                        .show();
            }
        });

        mFragmentResOwnerCartBinding.rcvOrder.setAdapter(resOwnerReservationAdapter);
    }

    private void cancelReservation(ReservationResOwnerResponse reservation){
        reservation.setStatus("cancelled");
        mProgressDialog.show();
        Call<Void> call = ReservationAPI.retrofitInstance.updateReservation(reservation.get_id(),reservation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Hủy bỏ thành công",
                            Toast.LENGTH_SHORT).show();
                    getListReservations();
                } else {
                    Toast.makeText(getContext(), "Lỗi đã xảy ra, vui lòng thử lại sau",
                            Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void confirmReservation(ReservationResOwnerResponse reservation){
        reservation.setStatus("confirm");
        mProgressDialog.show();
        Call<Void> call = ReservationAPI.retrofitInstance.updateReservation(reservation.get_id(),reservation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Duyệt thành công",
                            Toast.LENGTH_SHORT).show();
                    getListReservations();
                } else {
                    Toast.makeText(getContext(), "Lỗi đã xảy ra, vui lòng thử lại sau",
                            Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getListReservations();
    }
}
