package com.example.tablereservation.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tablereservation.adapter.FeedbackAdapter;
import com.example.tablereservation.adapter.MoreImageAdapter;
import com.example.tablereservation.api.restaurantAPI.FeedbackResponse;
import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.ActivityRestaurantDetailBinding;
import com.example.tablereservation.model.Feedback;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.util.GlideUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailActivity extends BaseActivity{
    private ActivityRestaurantDetailBinding mActivityRestaurantDetailBinding;
    private Restaurant mRestaurant;
    private List<FeedbackResponse> mFeedbackList;
    private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRestaurantDetailBinding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityRestaurantDetailBinding.getRoot());
        mProgressDialog = new ProgressDialog(this);
        getDataIntent();
        initToolbar();
        initListener();
    }


    private void initListener() {
        mActivityRestaurantDetailBinding.tvBooking.setOnClickListener(v -> onClickBooking(mRestaurant));
    }

    private void onClickBooking(@NonNull Restaurant restaurant){
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurant_object",restaurant);
        GlobalFunction.startActivity(this, BookingActivity.class,bundle);
    }

    private void initToolbar() {
        mActivityRestaurantDetailBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityRestaurantDetailBinding.toolbar.tvRight.setVisibility(View.VISIBLE);
        mActivityRestaurantDetailBinding.toolbar.tvTitle.setText("Chi tiết nhà hàng");
        mActivityRestaurantDetailBinding.toolbar.tvRight.setText("Vị trí");
        mActivityRestaurantDetailBinding.toolbar.tvRight.setOnClickListener(v -> goToMapActivity(mRestaurant));
        mActivityRestaurantDetailBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void goToMapActivity(Restaurant restaurant){
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurant_object",restaurant);
        GlobalFunction.startActivity(this,MapActivity.class,bundle);
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRestaurant = (Restaurant) bundle.get("restaurant_object");
            getFeedbackList(mRestaurant);
        }
    }

    private void displayListMoreImages() {
        if (mRestaurant.getImage() == null || mRestaurant.getImage().isEmpty()) {
            mActivityRestaurantDetailBinding.tvMoreImageLabel.setVisibility(View.GONE);
            return;
        }
        mActivityRestaurantDetailBinding.tvMoreImageLabel.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mActivityRestaurantDetailBinding.rcvImages.setLayoutManager(gridLayoutManager);

        MoreImageAdapter moreImageAdapter = new MoreImageAdapter(mRestaurant.getImage());
        mActivityRestaurantDetailBinding.rcvImages.setAdapter(moreImageAdapter);


        //display feedback
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mActivityRestaurantDetailBinding.rcvFeedbacks.setLayoutManager(layoutManager);
        FeedbackAdapter feedbackAdapter = new FeedbackAdapter(mFeedbackList);
        mActivityRestaurantDetailBinding.rcvFeedbacks.setAdapter(feedbackAdapter);
    }

    private void setDataRestaurantDetail(){
        if(mRestaurant == null){
            return;
        }
        GlideUtils.loadUrlBanner(mRestaurant.getImage().get(0),mActivityRestaurantDetailBinding.imageRestaurant);
        mActivityRestaurantDetailBinding.tvRestaurantName.setText(mRestaurant.getName());
        mActivityRestaurantDetailBinding.tvAddress.setText(mRestaurant.getAddress());
        mActivityRestaurantDetailBinding.tvPrice.setText(mRestaurant.getPrice());
        mActivityRestaurantDetailBinding.tvRestaurantDescription.setText(mRestaurant.getDescription());

        displayListMoreImages();
    }

    private void getFeedbackList(Restaurant restaurant){
        mProgressDialog.show();
        Call<List<FeedbackResponse>> call = RestaurantAPI.retrofitService.getFeedbackListByRestaurantId(restaurant.get_id());
        call.enqueue(new Callback<List<FeedbackResponse>>() {
            @Override
            public void onResponse(Call<List<FeedbackResponse>> call, Response<List<FeedbackResponse>> response) {
                if(response.isSuccessful()){
                    mFeedbackList = response.body();
                    setDataRestaurantDetail();
                } else {
                    Log.d("error",response.toString());
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<FeedbackResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
