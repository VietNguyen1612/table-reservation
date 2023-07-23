package com.example.tablereservation.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tablereservation.adapter.TableAdapter;
import com.example.tablereservation.api.tableAPI.TableAPI;
import com.example.tablereservation.api.tableAPI.TableRequest;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.ActivityAddTableBinding;
import com.example.tablereservation.databinding.ActivityResOwnerAddRestaurantBinding;
import com.example.tablereservation.listener.IOnClickTableItemListener;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.model.Table;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.StringUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTableActivity extends BaseActivity{
    private Restaurant mRestaurant;
    private ActivityAddTableBinding mActivityAddTableBinding;
    private TableAdapter mTableAdapter;
    private List<Table> mListTables;
    private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddTableBinding = ActivityAddTableBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddTableBinding.getRoot());
        mProgressDialog = new ProgressDialog(this);
        mActivityAddTableBinding.rdbOther.setChecked(true);
        initToolbar();
        getDataIntent();
        getListTable();
        mActivityAddTableBinding.btnAdd.setOnClickListener(v -> addTable());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            mRestaurant = (Restaurant) bundleReceived.get("restaurant_object");
        }
    }

    private void initToolbar(){
        mActivityAddTableBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAddTableBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityAddTableBinding.rcvTable.setLayoutManager(linearLayoutManager);
        mTableAdapter = new TableAdapter(mListTables, new IOnClickTableItemListener() {
            @Override
            public void onClickItemTable(Table table) {
                onClickDeleteTable(table);
            }
        });
        mActivityAddTableBinding.rcvTable.setAdapter(mTableAdapter);
    }

    private void getListTable(){
        mProgressDialog.show();
        Call<List<Table>> call = TableAPI.retrofitService.getTablesByResId(mRestaurant.get_id());
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                if(response.isSuccessful()){
                    mListTables = response.body();
                    initView();
                } else {
                    Toast.makeText(AddTableActivity.this,
                            "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onClickDeleteTable(Table table){
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Sau khi xóa bàn sẽ không khôi phục được")
                .setPositiveButton("Đồng ý", ((dialogInterface, i) -> {
                    deleteTable(table);
                }))
                .setNegativeButton("Hủy bỏ",null)
                .show();
    }

    private void deleteTable(Table table){
        mProgressDialog.show();
        String token = "Bearer "+ DataStoreManager.getToken();
        Call<Void> call = TableAPI.retrofitService.deleteTableById(token,table.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AddTableActivity.this,
                            "Xóa thành công", Toast.LENGTH_SHORT).show();
                    getListTable();
                } else {
                    Toast.makeText(AddTableActivity.this,
                            "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void addTable(){
        mProgressDialog.show();
        if(StringUtil.isEmpty(mActivityAddTableBinding.edtTableNumber.getText().toString())){
            Toast.makeText(AddTableActivity.this,
                    "Vui lòng nhập số của bàn", Toast.LENGTH_SHORT).show();
            return;
        }
        int tableNumber =  Integer.parseInt(mActivityAddTableBinding.edtTableNumber.getText().toString());
        String area = "";
        if(mActivityAddTableBinding.rdbOther.isChecked()){
            area = mActivityAddTableBinding.rdbOther.getText().toString();
        } else if(mActivityAddTableBinding.rdbInside.isChecked()){
            area = mActivityAddTableBinding.rdbInside.getText().toString();
        } else {
            area = mActivityAddTableBinding.rdbOutside.getText().toString();
        }
        Log.d("resId",mRestaurant.get_id());
        TableRequest tableRequest = new TableRequest(tableNumber,area,mRestaurant.get_id());
        String token = "Bearer "+DataStoreManager.getToken();
        Call<TableRequest> call = TableAPI.retrofitService.createTable(token,tableRequest);
        call.enqueue(new Callback<TableRequest>() {
            @Override
            public void onResponse(Call<TableRequest> call, Response<TableRequest> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AddTableActivity.this,
                            "Tạo thành công", Toast.LENGTH_SHORT).show();
                    getListTable();
                } else {
                    Log.d("error",response.toString());
                    Toast.makeText(AddTableActivity.this,
                            "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TableRequest> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
