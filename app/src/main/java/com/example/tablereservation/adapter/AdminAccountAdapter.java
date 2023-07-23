package com.example.tablereservation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.databinding.ItemAdminAccountBinding;
import com.example.tablereservation.listener.IOnClickAccountItemListener;
import com.example.tablereservation.model.Account;

import java.util.List;

public class AdminAccountAdapter extends RecyclerView.Adapter<AdminAccountAdapter.AdminAccountAdapterViewHolder> {
    private final List<Account> mListAccounts;
    private final IOnClickAccountItemListener mIOnClickAccountItemListener;

    public AdminAccountAdapter(List<Account> mListAccounts, IOnClickAccountItemListener mIOnClickAccountItemListener) {
        this.mListAccounts = mListAccounts;
        this.mIOnClickAccountItemListener = mIOnClickAccountItemListener;
    }

    @NonNull
    @Override
    public AdminAccountAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminAccountBinding itemAdminAccountBinding = ItemAdminAccountBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);
        return new AdminAccountAdapterViewHolder(itemAdminAccountBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAccountAdapterViewHolder holder, int position) {
        Account account = mListAccounts.get(position);
        holder.mItemAdminAccountBinding.tvStatus.setText("Trạng thái: "+account.getStatus());
        holder.mItemAdminAccountBinding.tvEmail.setText("Email: "+account.getEmail());
        holder.mItemAdminAccountBinding.tvRole.setText("Role: "+account.getRole());
        holder.mItemAdminAccountBinding.layoutItem.setOnClickListener(v ->
                mIOnClickAccountItemListener.onClickAccountItem(account));
    }

    @Override
    public int getItemCount() {
        if(mListAccounts == null){
            return 0;
        }
        return mListAccounts.size();
    }

    public static class AdminAccountAdapterViewHolder extends RecyclerView.ViewHolder{
        private final ItemAdminAccountBinding mItemAdminAccountBinding;

        public AdminAccountAdapterViewHolder(ItemAdminAccountBinding mItemAdminAccountBinding) {
            super(mItemAdminAccountBinding.getRoot());
            this.mItemAdminAccountBinding= mItemAdminAccountBinding;
        }
    }
}
