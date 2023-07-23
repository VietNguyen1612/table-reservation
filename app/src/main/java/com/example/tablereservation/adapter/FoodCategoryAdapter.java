package com.example.tablereservation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.databinding.ActivityMainBinding;
import com.example.tablereservation.databinding.ItemFoodCategoryBinding;
import com.example.tablereservation.model.FoodCategory;

import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder> {
    private List<FoodCategory> mFoodCategories;

    public void setData(List<FoodCategory> list){
        this.mFoodCategories = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodCategoryViewHolder(ItemFoodCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCategoryViewHolder holder, int position) {
        FoodCategory foodCategory = mFoodCategories.get(position);
        holder.mItemFoodCategoryBinding.imgFood.setImageResource(foodCategory.getResourceId());
        holder.mItemFoodCategoryBinding.tvTitle.setText(foodCategory.getTitle());
    }

    @Override
    public int getItemCount() {
        return mFoodCategories.size();
    }

    public class FoodCategoryViewHolder extends RecyclerView.ViewHolder{
        private ItemFoodCategoryBinding mItemFoodCategoryBinding;
        public FoodCategoryViewHolder(@NonNull ItemFoodCategoryBinding mItemFoodCategoryBinding) {
            super(mItemFoodCategoryBinding.getRoot());
            this.mItemFoodCategoryBinding = mItemFoodCategoryBinding;
        }
    }
}
