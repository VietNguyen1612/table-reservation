package com.example.tablereservation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.api.reservationAPI.ReservationResOwnerResponse;
import com.example.tablereservation.databinding.ItemResOwnerBookingHistoryBinding;
import com.example.tablereservation.listener.IOnClickResOwnerReservationItemListener;

import java.util.List;

public class ResOwnerReservationAdapter extends RecyclerView.Adapter<ResOwnerReservationAdapter.ResOwnerReservationViewHolder>{
    private final List<ReservationResOwnerResponse> mReservationResOwnerResponseList;
    private final IOnClickResOwnerReservationItemListener mIOnClickResOwnerReservationItemListener;

    public ResOwnerReservationAdapter(List<ReservationResOwnerResponse> mReservationResOwnerResponseList,
                                      IOnClickResOwnerReservationItemListener mIOnClickResOwnerReservationItemListener) {
        this.mReservationResOwnerResponseList = mReservationResOwnerResponseList;
        this.mIOnClickResOwnerReservationItemListener = mIOnClickResOwnerReservationItemListener;
    }

    @NonNull
    @Override
    public ResOwnerReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemResOwnerBookingHistoryBinding mItemResOwnerBookingHistoryBinding = ItemResOwnerBookingHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new ResOwnerReservationViewHolder(mItemResOwnerBookingHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResOwnerReservationViewHolder holder, int position) {
        ReservationResOwnerResponse reservation = mReservationResOwnerResponseList.get(position);
        if(reservation == null){
            return;
        }
        holder.mItemResOwnerBookingHistoryBinding.tvPhone.setText(reservation.getCustomer().getPhone());
        String strTable = reservation.getTable().getTableNumber() + " - Vị trí: " +reservation.getTable().getArea();
        holder.mItemResOwnerBookingHistoryBinding.tvTable.setText(strTable);
        holder.mItemResOwnerBookingHistoryBinding.tvRestaurant.setText(reservation.getRestaurant().getName());
        String strDate = reservation.getArrivedDate().substring(0,10);
        holder.mItemResOwnerBookingHistoryBinding.tvArriveDateTime.setText(strDate + " Vào lúc: "+reservation.getDuration());
        if(reservation.getStatus().equals("completed")){
            holder.mItemResOwnerBookingHistoryBinding.tvStatus.setText("confirm");
        } else {
            holder.mItemResOwnerBookingHistoryBinding.tvStatus.setText(reservation.getStatus());
        }
        holder.mItemResOwnerBookingHistoryBinding.tvGuessNum.setText(String.valueOf(reservation.getGuessNum()));
        holder.mItemResOwnerBookingHistoryBinding.tvNote.setText(reservation.getNote());
        if(reservation.getStatus().equals("pending")){
            holder.mItemResOwnerBookingHistoryBinding.btnCancel.setVisibility(View.VISIBLE);
            holder.mItemResOwnerBookingHistoryBinding.btnConfirm.setVisibility(View.VISIBLE);
            holder.mItemResOwnerBookingHistoryBinding.btnCancel.setOnClickListener(v ->
                    mIOnClickResOwnerReservationItemListener.onClickCancelReservation(reservation));
            holder.mItemResOwnerBookingHistoryBinding.btnConfirm.setOnClickListener(v ->
                    mIOnClickResOwnerReservationItemListener.onClickConfirmReservation(reservation));
        } else {
            holder.mItemResOwnerBookingHistoryBinding.btnCancel.setVisibility(View.GONE);
            holder.mItemResOwnerBookingHistoryBinding.btnConfirm.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(mReservationResOwnerResponseList == null){
            return 0;
        }
        return mReservationResOwnerResponseList.size();
    }


    public static class ResOwnerReservationViewHolder extends RecyclerView.ViewHolder{
        private final ItemResOwnerBookingHistoryBinding mItemResOwnerBookingHistoryBinding;

        public ResOwnerReservationViewHolder(@NonNull ItemResOwnerBookingHistoryBinding mItemResOwnerBookingHistoryBinding) {
            super(mItemResOwnerBookingHistoryBinding.getRoot());
            this.mItemResOwnerBookingHistoryBinding = mItemResOwnerBookingHistoryBinding;
        }
    }
}
