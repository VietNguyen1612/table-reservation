package com.example.tablereservation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.databinding.ItemTableBinding;
import com.example.tablereservation.listener.IOnClickTableItemListener;
import com.example.tablereservation.model.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableAdapterViewHoler> {
    private final List<Table> mListTables;
    private final IOnClickTableItemListener mIOnClickTableItemListener;

    public TableAdapter(List<Table> mListTables, IOnClickTableItemListener mIOnClickTableItemListener) {
        this.mListTables = mListTables;
        this.mIOnClickTableItemListener = mIOnClickTableItemListener;
    }

    @NonNull
    @Override
    public TableAdapterViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTableBinding itemTableBinding = ItemTableBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);
        return new TableAdapterViewHoler(itemTableBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapterViewHoler holder, int position) {
        Table table = mListTables.get(position);
        if(table == null){
            return;
        }
        holder.mItemTableBinding.tvName.setText("Bàn số: "+table.getTableNumber()+" "+table.getArea());
        holder.mItemTableBinding.imgDelete.setOnClickListener(v ->
                mIOnClickTableItemListener.onClickItemTable(table));
    }

    @Override
    public int getItemCount() {
        if(mListTables == null){
            return 0;
        }
        return mListTables.size();
    }

    public static class TableAdapterViewHoler extends RecyclerView.ViewHolder{
        private final ItemTableBinding mItemTableBinding;

        public TableAdapterViewHoler(ItemTableBinding mItemTableBinding) {
            super(mItemTableBinding.getRoot());
            this.mItemTableBinding = mItemTableBinding;
        }
    }
}
