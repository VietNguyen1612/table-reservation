package com.example.tablereservation.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tablereservation.R;
import com.example.tablereservation.activity.MainActivity;
import com.example.tablereservation.activity.RestaurantDetailActivity;
import com.example.tablereservation.activity.SignInActivity;
import com.example.tablereservation.adapter.FoodCategoryAdapter;
import com.example.tablereservation.adapter.PopularRestaurantAdapter;
import com.example.tablereservation.adapter.RestaurantAdapter;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPIService;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.FragmentHomeBinding;
import com.example.tablereservation.model.FoodCategory;
import com.example.tablereservation.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment{
    private FragmentHomeBinding mFragmentHomeBinding;
    private List<FoodCategory> mFoodCategories = new ArrayList<>();

    private List<Restaurant> mListRestaurants;

    private List<Restaurant> mListPopularRestaurants;

    private static ProgressDialog mProgressDialog;

    private final Handler mHandlerBanner = new Handler();
    private final Runnable mRunnableBanner = new Runnable() {
        @Override
        public void run() {
            if (mListPopularRestaurants == null || mListPopularRestaurants.isEmpty()) {
                return;
            }
            if (mFragmentHomeBinding.viewpager2.getCurrentItem() == mListPopularRestaurants.size() - 1) {
                mFragmentHomeBinding.viewpager2.setCurrentItem(0);
                return;
            }
            mFragmentHomeBinding.viewpager2.setCurrentItem(mFragmentHomeBinding.viewpager2.getCurrentItem() + 1);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        getListRestaurant();
        return mFragmentHomeBinding.getRoot();
    }


    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(true, "Đồ ăn");
        }
    }

    private void displayListPopularRestaurants() {
        PopularRestaurantAdapter mPopularRestaurantAdapter = new PopularRestaurantAdapter(getListPopularRestaurants(), this::goToRestaurantDetail);
        mFragmentHomeBinding.viewpager2.setAdapter(mPopularRestaurantAdapter);
        mFragmentHomeBinding.indicator3.setViewPager(mFragmentHomeBinding.viewpager2);

        mFragmentHomeBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandlerBanner.removeCallbacks(mRunnableBanner);
                mHandlerBanner.postDelayed(mRunnableBanner, 3000);
            }
        });
    }

    private void displayRestaurants(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFragmentHomeBinding.rcvRestaurant.setLayoutManager(layoutManager);
        RestaurantAdapter mRestaurantAdapter = new RestaurantAdapter(mListRestaurants, this::goToRestaurantDetail);
        mFragmentHomeBinding.rcvRestaurant.setAdapter(mRestaurantAdapter);
    }

    private void getListRestaurant(){
        mProgressDialog.show();
        Call<List<Restaurant>> call = RestaurantAPI.retrofitService.getRestaurants();
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.isSuccessful()) {
                    mListRestaurants = response.body();
                    mProgressDialog.dismiss();
                    displayListPopularRestaurants();
                    displayRestaurants();
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

    private List<Restaurant> getListPopularRestaurants() {
        mListPopularRestaurants = new ArrayList<>();
        if (mListRestaurants == null || mListRestaurants.isEmpty()) {
            return mListPopularRestaurants;
        }
        for (int i=0 ; i<3; i++) {
            mListPopularRestaurants.add(mListRestaurants.get(i));
        }
        return mListPopularRestaurants;
    }

    private void goToRestaurantDetail(@NonNull Restaurant restaurant){
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurant_object",restaurant);
        GlobalFunction.startActivity(getActivity(), RestaurantDetailActivity.class,bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListRestaurant();
    }
}