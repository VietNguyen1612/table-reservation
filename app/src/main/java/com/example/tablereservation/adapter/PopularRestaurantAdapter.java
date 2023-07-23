package com.example.tablereservation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.databinding.ItemRestaurantPopularBinding;
import com.example.tablereservation.listener.IOnClickRestaurantItemListener;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.util.GlideUtils;

import java.util.List;

public class PopularRestaurantAdapter extends RecyclerView.Adapter<PopularRestaurantAdapter.PopularRestaurantViewHolder> {
    public final List<Restaurant> mRestaurantList;

    public final IOnClickRestaurantItemListener iOnClickRestaurantItemListener;

    public PopularRestaurantAdapter(List<Restaurant> mRestaurantList, IOnClickRestaurantItemListener iOnClickRestaurantItemListener) {
        this.mRestaurantList = mRestaurantList;
        this.iOnClickRestaurantItemListener = iOnClickRestaurantItemListener;
    }

    @NonNull
    @Override
    public PopularRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantPopularBinding itemRestaurantPopularBinding = ItemRestaurantPopularBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new PopularRestaurantViewHolder(itemRestaurantPopularBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularRestaurantViewHolder holder, int position) {
        Restaurant restaurant = mRestaurantList.get(position);
        if(restaurant == null){
            return;
        }
        GlideUtils.loadUrlBanner(restaurant.getImage().get(0), holder.mItemRestaurantPopularBinding.imageFood);
        holder.mItemRestaurantPopularBinding.layoutItem.setOnClickListener(v ->
                iOnClickRestaurantItemListener.onClickItemRestaurant(restaurant));
    }

    @Override
    public int getItemCount() {
        if(mRestaurantList != null){
            return mRestaurantList.size();
        }
        return 0;
    }

    public static class PopularRestaurantViewHolder extends RecyclerView.ViewHolder{
        private final ItemRestaurantPopularBinding mItemRestaurantPopularBinding;

        public PopularRestaurantViewHolder(@NonNull ItemRestaurantPopularBinding itemRestaurantPopularBinding) {
            super(itemRestaurantPopularBinding.getRoot());
            this.mItemRestaurantPopularBinding = itemRestaurantPopularBinding;
        }
    }
}
