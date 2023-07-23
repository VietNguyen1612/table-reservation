package com.example.tablereservation.fragment.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.activity.AdminMainActivity;
import com.example.tablereservation.activity.AdminRestaurantDetailActivity;
import com.example.tablereservation.activity.ResOwnerMainActivity;
import com.example.tablereservation.activity.RestaurantDetailActivity;
import com.example.tablereservation.adapter.RestaurantAdapter;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.FragmentAdminRestaurantBinding;
import com.example.tablereservation.databinding.FragmentResOwnerHomeBinding;
import com.example.tablereservation.fragment.BaseFragment;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.prefs.DataStoreManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRestaurantFragment extends BaseFragment {
    private FragmentAdminRestaurantBinding mFragmentAdminRestaurantBinding;
    private List<Restaurant> mListRestaurants;
    private static ProgressDialog mProgressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminRestaurantBinding = FragmentAdminRestaurantBinding.inflate(inflater, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        getListRestaurants();
        return mFragmentAdminRestaurantBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar("Nhà hàng");
        }
    }

    private void displayRestaurants(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminRestaurantBinding.rcvRestaurant.setLayoutManager(layoutManager);
        RestaurantAdapter mRestaurantAdapter = new RestaurantAdapter(mListRestaurants, this::goToRestaurantDetail);
        mFragmentAdminRestaurantBinding.rcvRestaurant.setAdapter(mRestaurantAdapter);
    }

    private void getListRestaurants(){
        mProgressDialog.show();
        String token = "Bearer "+ DataStoreManager.getToken();
        Call<List<Restaurant>> call = RestaurantAPI.retrofitService.getAllRestaurants(token);
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if(response.isSuccessful()){
                    mListRestaurants = response.body();
                    displayRestaurants();
                } else {
                    Log.d("error",response.toString());
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void goToRestaurantDetail(@NonNull Restaurant restaurant){
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurant_object",restaurant);
        GlobalFunction.startActivity(getActivity(), AdminRestaurantDetailActivity.class,bundle);
    }
    @Override
    public void onResume() {
        super.onResume();
        getListRestaurants();
    }
}
