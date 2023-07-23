package com.example.tablereservation.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.databinding.ItemResOwnerRestaurantBinding;
import com.example.tablereservation.listener.IOnOwnerResListener;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.util.GlideUtils;

import java.util.List;

public class ResOwnerRestaurantAdapter extends RecyclerView.Adapter<ResOwnerRestaurantAdapter.ResOwnerRestaurantViewHolder>{
    private final List<Restaurant> mListRestaurants;
    private final IOnOwnerResListener iOnOwnerResListener;

    public ResOwnerRestaurantAdapter(List<Restaurant> restaurantList, IOnOwnerResListener listener){
        this.mListRestaurants = restaurantList;
        this.iOnOwnerResListener = listener;
    }

    @NonNull
    @Override
    public ResOwnerRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemResOwnerRestaurantBinding itemResOwnerRestaurantBinding = ItemResOwnerRestaurantBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new ResOwnerRestaurantViewHolder(itemResOwnerRestaurantBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResOwnerRestaurantViewHolder holder, int position) {
        Restaurant restaurant = mListRestaurants.get(position);
        if(restaurant == null) {
            return;
        }
        GlideUtils.loadUrl(restaurant.getImage().get(0),holder.mItemResOwnerRestaurantBinding.imgRes);
        if(restaurant.getStatus().equals("inactive")){
            holder.mItemResOwnerRestaurantBinding.tvResName.setText(restaurant.getName());
            holder.mItemResOwnerRestaurantBinding.tvResName.setPaintFlags(holder.
                    mItemResOwnerRestaurantBinding.tvResName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mItemResOwnerRestaurantBinding.tvPrice.setText("Your restaurant has been banned");
            holder.mItemResOwnerRestaurantBinding.imgEdit.setVisibility(View.GONE);
            holder.mItemResOwnerRestaurantBinding.imgDelete.setVisibility(View.GONE);
        } else {
            holder.mItemResOwnerRestaurantBinding.tvResName.setText(restaurant.getName());
            holder.mItemResOwnerRestaurantBinding.tvPrice.setText(restaurant.getPrice());
            holder.mItemResOwnerRestaurantBinding.tvDescription.setText(restaurant.getDescription());
            holder.mItemResOwnerRestaurantBinding.tvLocation.setText(restaurant.getAddress());
            holder.mItemResOwnerRestaurantBinding.imgEdit.setOnClickListener(v ->
                    iOnOwnerResListener.onClickUpdateRes(restaurant));
            holder.mItemResOwnerRestaurantBinding.imgDelete.setOnClickListener(v ->
                    iOnOwnerResListener.onClickDeleteRes(restaurant));
        }
    }

    @Override
    public int getItemCount() {
        return null == mListRestaurants ? 0 : mListRestaurants.size();
    }

    public static class ResOwnerRestaurantViewHolder extends RecyclerView.ViewHolder{
        private final ItemResOwnerRestaurantBinding mItemResOwnerRestaurantBinding;

        public ResOwnerRestaurantViewHolder(ItemResOwnerRestaurantBinding itemResOwnerRestaurantBinding) {
            super(itemResOwnerRestaurantBinding.getRoot());
            this.mItemResOwnerRestaurantBinding = itemResOwnerRestaurantBinding;
        }
    }
}
