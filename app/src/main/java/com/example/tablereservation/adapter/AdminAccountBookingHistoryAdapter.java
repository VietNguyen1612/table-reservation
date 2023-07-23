package com.example.tablereservation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.databinding.ItemAdminAccountBookingHistoryBinding;
import com.example.tablereservation.listener.IOnClickAdminAccountFeedbackListener;
import com.example.tablereservation.model.Feedback;
import com.example.tablereservation.model.Reservation;
import com.example.tablereservation.model.Restaurant;

import java.util.List;

public class AdminAccountBookingHistoryAdapter extends RecyclerView.
        Adapter<AdminAccountBookingHistoryAdapter.AdminAccountBookingHistoryAdapterViewHolder> {
    private final List<Reservation> mListReservations;
    private final List<Restaurant> mListRestaurants;
    private final List<Feedback> mListFeedbacks;
    private final IOnClickAdminAccountFeedbackListener mIOnClickAdminAccountFeedbackListener;

    public AdminAccountBookingHistoryAdapter(List<Reservation> mListReservations, List<Restaurant> mListRestaurants, List<Feedback> mListFeedbacks, IOnClickAdminAccountFeedbackListener mIOnClickAdminAccountFeedbackListener) {
        this.mListReservations = mListReservations;
        this.mListRestaurants = mListRestaurants;
        this.mListFeedbacks = mListFeedbacks;
        this.mIOnClickAdminAccountFeedbackListener = mIOnClickAdminAccountFeedbackListener;
    }

    @NonNull
    @Override
    public AdminAccountBookingHistoryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminAccountBookingHistoryBinding itemAdminAccountBookingHistoryBinding = ItemAdminAccountBookingHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);
        return new AdminAccountBookingHistoryAdapterViewHolder(itemAdminAccountBookingHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAccountBookingHistoryAdapterViewHolder holder, int position) {
        Reservation reservation = mListReservations.get(position);
        if(reservation == null){
            return;
        }
        for (Restaurant restaurant: mListRestaurants) {
            if(reservation.getRestaurant().equals(restaurant.get_id())){
                holder.mItemAdminAccountBookingHistoryBinding.tvStatus.setText("Trạng thái: "+reservation.getStatus());
                holder.mItemAdminAccountBookingHistoryBinding.tvArrivedDate.setText("Ngày tới: "+reservation.getArrivedDate().substring(0,10));
                holder.mItemAdminAccountBookingHistoryBinding.tvResName.setText("Tên nhà hàng: "+restaurant.getName());
                for(Feedback feedback: mListFeedbacks){
                    if(!reservation.getFeedback().isEmpty()){
                        if(feedback.get_id().equals(reservation.getFeedback().get(0))){
                            holder.mItemAdminAccountBookingHistoryBinding.tvFeedback.setText("Feedback: "+feedback.getContent());
                            holder.mItemAdminAccountBookingHistoryBinding.imgDelete.setOnClickListener(v->
                                    mIOnClickAdminAccountFeedbackListener.onClick(feedback,reservation));
                            return;
                        }
                    } else{
                        holder.mItemAdminAccountBookingHistoryBinding.imgDelete.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mListReservations == null){
            return 0;
        }
        return mListReservations.size();
    }

    public static class AdminAccountBookingHistoryAdapterViewHolder extends RecyclerView.ViewHolder{
        private final ItemAdminAccountBookingHistoryBinding mItemAdminAccountBookingHistoryBinding;

        public AdminAccountBookingHistoryAdapterViewHolder(ItemAdminAccountBookingHistoryBinding mItemAdminAccountBookingHistoryBinding) {
            super(mItemAdminAccountBookingHistoryBinding.getRoot());
            this.mItemAdminAccountBookingHistoryBinding= mItemAdminAccountBookingHistoryBinding;
        }
    }
}
