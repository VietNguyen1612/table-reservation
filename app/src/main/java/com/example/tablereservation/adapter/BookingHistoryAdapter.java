package com.example.tablereservation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.databinding.ItemBookingHistoryBinding;
import com.example.tablereservation.listener.IOnClickBookingHistoryListener;
import com.example.tablereservation.model.Reservation;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.model.Table;
import com.example.tablereservation.util.StringUtil;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryAdapterViewHolder> {
    private final List<Reservation> mReservationList;
    private final List<Restaurant> mRestaurantList;
    private final List<Table> mTableList;

    private final IOnClickBookingHistoryListener mIOnClickBookingHistoryListener;

    public BookingHistoryAdapter(List<Reservation> mReservationList, List<Restaurant> mRestaurantList, List<Table> mTableList, IOnClickBookingHistoryListener mIOnClickBookingHistoryListener) {
        this.mReservationList = mReservationList;
        this.mRestaurantList = mRestaurantList;
        this.mTableList = mTableList;
        this.mIOnClickBookingHistoryListener = mIOnClickBookingHistoryListener;
    }

    @NonNull
    @Override
    public BookingHistoryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingHistoryBinding itemBookingHistoryBinding = ItemBookingHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);
        return new BookingHistoryAdapterViewHolder(itemBookingHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryAdapterViewHolder holder, int position) {
        Reservation reservation = mReservationList.get(position);
        String resName = "";
        String tableName = "";
        if(reservation == null){
            return;
        }
        for (Restaurant restaurant: mRestaurantList) {
            if(restaurant.get_id().equals(reservation.getRestaurant())){
                resName = restaurant.getName();
                break;
            }
        }
        for(Table table: mTableList){
            if(table.get_id().equals(reservation.getTable())){
                tableName = table.getArea()+ " - Bàn số "+table.getTableNumber();
            }
        }
        if(!StringUtil.isEmpty(resName)){
            holder.mItemBookingHistoryBinding.tvResName.setText("Nhà hàng: "+resName);
            holder.mItemBookingHistoryBinding.tvArrivedDate.setText("Thời gian đến: "+reservation.getArrivedDate().substring(0,10)+" "+reservation.getDuration());
            holder.mItemBookingHistoryBinding.tvGuestNum.setText("Số người: "+reservation.getGuessNum());
            holder.mItemBookingHistoryBinding.tvNote.setText("Ghi chú: "+reservation.getNote());
            if(reservation.getStatus().equals("completed")){
                holder.mItemBookingHistoryBinding.tvStatus.setText("Trạng thái: confirm");
            } else {
                holder.mItemBookingHistoryBinding.tvStatus.setText("Trạng thái: "+reservation.getStatus());
            }

            holder.mItemBookingHistoryBinding.tvArea.setText("Khu vực ngồi: "+tableName);
            holder.mItemBookingHistoryBinding.layoutItem.setOnClickListener(v ->
                    mIOnClickBookingHistoryListener.onClick(reservation));
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        if(mReservationList == null){
            return 0;
        }
        return mReservationList.size();
    }


     public static class BookingHistoryAdapterViewHolder extends RecyclerView.ViewHolder{
        private final ItemBookingHistoryBinding mItemBookingHistoryBinding;
        public BookingHistoryAdapterViewHolder(ItemBookingHistoryBinding itemBookingHistoryBinding){
            super(itemBookingHistoryBinding.getRoot());
            this.mItemBookingHistoryBinding = itemBookingHistoryBinding;
        }
    }
}
