package com.example.tablereservation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.databinding.ItemRestaurantBinding;
import com.example.tablereservation.listener.IOnClickRestaurantItemListener;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.util.GlideUtils;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>{
    private final List<Restaurant> mListRestaurants;
    public final IOnClickRestaurantItemListener iOnClickRestaurantItemListener;
    public RestaurantAdapter(List<Restaurant> mListRestaurants, IOnClickRestaurantItemListener iOnClickRestaurantItemListener) {
        this.mListRestaurants = mListRestaurants;
        this.iOnClickRestaurantItemListener = iOnClickRestaurantItemListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding itemRestaurantBinding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);
        return new RestaurantViewHolder(itemRestaurantBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = mListRestaurants.get(position);
        if(restaurant == null){
            return;
        }
        GlideUtils.loadUrl(restaurant.getImage().get(0),holder.mItemRestaurantBinding.imgRestaurant);
        if(restaurant.getStatus().equals("inactive")){
            holder.mItemRestaurantBinding.tvResName.setText(restaurant.getName()+" /Inactive");
        } else {
            holder.mItemRestaurantBinding.tvResName.setText(restaurant.getName());
        }
        holder.mItemRestaurantBinding.tvLocation.setText(restaurant.getAddress());
        holder.mItemRestaurantBinding.tvPrice.setText(restaurant.getPrice());
        holder.mItemRestaurantBinding.layoutItem.setOnClickListener(v->
                iOnClickRestaurantItemListener.onClickItemRestaurant(restaurant));
    }

    @Override
    public int getItemCount() {
        if(mListRestaurants == null){
            return 0;
        }
        return mListRestaurants.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{
        private final ItemRestaurantBinding mItemRestaurantBinding;

        public RestaurantViewHolder(ItemRestaurantBinding itemRestaurantBinding){
            super(itemRestaurantBinding.getRoot());
            this.mItemRestaurantBinding = itemRestaurantBinding;
        }
    }
}
