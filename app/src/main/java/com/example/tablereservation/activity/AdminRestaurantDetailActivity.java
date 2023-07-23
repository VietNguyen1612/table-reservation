package com.example.tablereservation.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.tablereservation.adapter.MoreImageAdapter;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.databinding.ActivityAdminRestaurantDetailBinding;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.util.GlideUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRestaurantDetailActivity extends BaseActivity{
    private ActivityAdminRestaurantDetailBinding mActivityAdminRestaurantDetailBinding;
    private Restaurant mRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminRestaurantDetailBinding = ActivityAdminRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminRestaurantDetailBinding.getRoot());

        getDataIntent();
        initToolbar();
        setDataRestaurantDetail();
        initListener();
    }
    private void initToolbar() {
        mActivityAdminRestaurantDetailBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAdminRestaurantDetailBinding.toolbar.tvTitle.setText("Chi tiết nhà hàng");

        mActivityAdminRestaurantDetailBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRestaurant = (Restaurant) bundle.get("restaurant_object");
        }
    }
    private void initListener() {
        mActivityAdminRestaurantDetailBinding.tvActiveOrDeactive.setOnClickListener(v -> onClickActiveOrDeactivate(mRestaurant));
    }

    private void onClickActiveOrDeactivate(Restaurant restaurant){
        Map<String, Object> requestBody = new HashMap<>();
        if(restaurant.getStatus().equals("active")){
            requestBody.put("status", "inactive");
            Call<Void> call = RestaurantAPI.retrofitService.deactiveOrActiveRestaurant(restaurant.get_id(),requestBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(AdminRestaurantDetailActivity.this,
                                "Khóa nhà hàng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.d("error",response.toString());
                        Toast.makeText(AdminRestaurantDetailActivity.this,
                                "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            requestBody.put("status", "active");
            Call<Void> call = RestaurantAPI.retrofitService.deactiveOrActiveRestaurant(restaurant.get_id(),requestBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(AdminRestaurantDetailActivity.this,
                                "Mở nhà hàng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.d("error",response.toString());
                        Toast.makeText(AdminRestaurantDetailActivity.this,
                                "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    }
    private void displayListMoreImages() {
        if (mRestaurant.getImage() == null || mRestaurant.getImage().isEmpty()) {
            mActivityAdminRestaurantDetailBinding.tvMoreImageLabel.setVisibility(View.GONE);
            return;
        }
        mActivityAdminRestaurantDetailBinding.tvMoreImageLabel.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mActivityAdminRestaurantDetailBinding.rcvImages.setLayoutManager(gridLayoutManager);

        MoreImageAdapter moreImageAdapter = new MoreImageAdapter(mRestaurant.getImage());
        mActivityAdminRestaurantDetailBinding.rcvImages.setAdapter(moreImageAdapter);
    }
    private void setDataRestaurantDetail(){
        if(mRestaurant == null){
            return;
        }
        GlideUtils.loadUrlBanner(mRestaurant.getImage().get(0),mActivityAdminRestaurantDetailBinding.imageRestaurant);
        mActivityAdminRestaurantDetailBinding.tvRestaurantName.setText(mRestaurant.getName());
        mActivityAdminRestaurantDetailBinding.tvAddress.setText(mRestaurant.getAddress());
        mActivityAdminRestaurantDetailBinding.tvPrice.setText(mRestaurant.getPrice());
        mActivityAdminRestaurantDetailBinding.tvRestaurantDescription.setText(mRestaurant.getDescription());
        if(mRestaurant.getStatus().equals("active")){
            mActivityAdminRestaurantDetailBinding.tvActiveOrDeactive.setText("Khóa nhà hàng");
        } else {
            mActivityAdminRestaurantDetailBinding.tvActiveOrDeactive.setText("Mở nhà hàng");
        }
        displayListMoreImages();
    }
}
