package com.example.tablereservation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.api.restaurantAPI.FeedbackResponse;
import com.example.tablereservation.databinding.ItemAdminAccountBookingHistoryBinding;
import com.example.tablereservation.databinding.ItemFeedbackBinding;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackAdapterViewHolder> {
    private final List<FeedbackResponse> mFeedbackList;

    public FeedbackAdapter(List<FeedbackResponse> mFeedbackList) {
        this.mFeedbackList = mFeedbackList;
    }

    @NonNull
    @Override
    public FeedbackAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedbackBinding itemFeedbackBinding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);
        return new FeedbackAdapterViewHolder(itemFeedbackBinding) ;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapterViewHolder holder, int position) {
        FeedbackResponse feedbackResponse = mFeedbackList.get(position);
        if(feedbackResponse==null){
            return;
        }
        holder.mItemFeedbackBinding.tvName.setText(feedbackResponse.getUser().getName());
        holder.mItemFeedbackBinding.tvFeedback.setText(feedbackResponse.getContent());
    }

    @Override
    public int getItemCount() {
        return mFeedbackList.size();
    }


    public static class FeedbackAdapterViewHolder extends RecyclerView.ViewHolder{
        private final ItemFeedbackBinding mItemFeedbackBinding;

        public FeedbackAdapterViewHolder(ItemFeedbackBinding mItemFeedbackBinding) {
            super(mItemFeedbackBinding.getRoot());
            this.mItemFeedbackBinding = mItemFeedbackBinding;
        }
    }
}
