package com.example.tablereservation.fragment.resOwner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tablereservation.activity.AddRestaurantActivity;
import com.example.tablereservation.activity.ResOwnerMainActivity;
import com.example.tablereservation.adapter.ResOwnerRestaurantAdapter;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.FragmentResOwnerHomeBinding;
import com.example.tablereservation.fragment.BaseFragment;
import com.example.tablereservation.listener.IOnOwnerResListener;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.prefs.DataStoreManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResOwnerHomeFragment extends BaseFragment {
    private FragmentResOwnerHomeBinding mFragmentResOwnerHomeBinding;
    private List<Restaurant> mRestaurantList;
    private ResOwnerRestaurantAdapter resOwnerRestaurantAdapter;
    private static ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentResOwnerHomeBinding = FragmentResOwnerHomeBinding.inflate(inflater, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        mFragmentResOwnerHomeBinding.btnAddRestaurant.setOnClickListener(v -> onClickAddRestaurant());
        getListRestaurant();
        return mFragmentResOwnerHomeBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((ResOwnerMainActivity) getActivity()).setToolBar("Nhà hàng của tôi");
        }
    }

    private void displayRestaurant(){
        if(getActivity() == null){
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentResOwnerHomeBinding.rcvRestaurant.setLayoutManager(linearLayoutManager);
        resOwnerRestaurantAdapter = new ResOwnerRestaurantAdapter(mRestaurantList, new IOnOwnerResListener() {
            @Override
            public void onClickUpdateRes(Restaurant restaurant) {
                onClickEditRestaurant(restaurant);
            }

            @Override
            public void onClickDeleteRes(Restaurant restaurant) {
                onClickDeleteRestaurant(restaurant);
            }
        });
        mFragmentResOwnerHomeBinding.rcvRestaurant.setAdapter(resOwnerRestaurantAdapter);
    }

    private void getListRestaurant(){
        mProgressDialog.show();
        String resOwnerId = DataStoreManager.getResOwnerID();
        String token = "Bearer "+ DataStoreManager.getToken();
        Call<List<Restaurant>> call = RestaurantAPI.retrofitService.getRestaurantsByResOwnerId(token,resOwnerId);
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.isSuccessful()) {

                    mRestaurantList = response.body();
                    displayRestaurant();
                    mProgressDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Lỗi đã xảy ra, vui lòng thử lại sau",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteRestaurant(Restaurant restaurant){
        mProgressDialog.show();
        String token = DataStoreManager.getToken();
        Call<Void> call = RestaurantAPI.retrofitService.deleteRestaurantById(token, restaurant.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(),"Xóa thành công", Toast.LENGTH_SHORT).show();
                    getListRestaurant();
                } else {
                    Toast.makeText(getActivity(),"Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onClickAddRestaurant(){
        GlobalFunction.startActivity(getActivity(), AddRestaurantActivity.class);
    }

    private void onClickEditRestaurant(Restaurant restaurant){
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurant_object", restaurant);
        GlobalFunction.startActivity(getActivity(), AddRestaurantActivity.class, bundle);
    }

    private void onClickDeleteRestaurant(Restaurant restaurant){
        new AlertDialog.Builder(getActivity())
                .setTitle("Xác nhận xóa")
                .setMessage("Sau khi xóa nhà hàng sẽ không khôi phục được")
                .setPositiveButton("Đồng ý", ((dialogInterface, i) -> {
                    deleteRestaurant(restaurant);
                }))
                .setNegativeButton("Hủy bỏ",null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getListRestaurant();
    }
}
