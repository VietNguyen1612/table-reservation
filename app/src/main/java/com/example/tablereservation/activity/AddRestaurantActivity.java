package com.example.tablereservation.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.api.restaurantAPI.RestaurantRequest;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.ActivityResOwnerAddRestaurantBinding;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRestaurantActivity extends BaseActivity{
    private ActivityResOwnerAddRestaurantBinding mActivityAddRestaurantBinding;
    private boolean isUpdate;
    private Restaurant mRestaurant;
    private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddRestaurantBinding = ActivityResOwnerAddRestaurantBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddRestaurantBinding.getRoot());
        mProgressDialog = new ProgressDialog(this);
        getDataIntent();
        initToolbar();
        initView();
        mActivityAddRestaurantBinding.btnAddOrEdit.setOnClickListener(v->addOrUpdateRestaurant());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mRestaurant = (Restaurant) bundleReceived.get("restaurant_object");
        }
    }

    private void initToolbar(){
        mActivityAddRestaurantBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        if(isUpdate){
            mActivityAddRestaurantBinding.toolbar.tvRight.setVisibility(View.VISIBLE);
            mActivityAddRestaurantBinding.toolbar.tvRight.setOnClickListener(v ->
                    onClickEditTable(mRestaurant));
        }
        mActivityAddRestaurantBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void onClickEditTable(Restaurant restaurant){
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurant_object", restaurant);
        GlobalFunction.startActivity(this, AddTableActivity.class, bundle);
    }

    private void initView(){
        if(isUpdate){
            mActivityAddRestaurantBinding.toolbar.tvTitle.setText("Chỉnh sửa nhà hàng");
            mActivityAddRestaurantBinding.btnAddOrEdit.setText("Chỉnh sửa");

            mActivityAddRestaurantBinding.edtName.setText(mRestaurant.getName());
            mActivityAddRestaurantBinding.edtDescription.setText(mRestaurant.getDescription());
            mActivityAddRestaurantBinding.edtPrice.setText(mRestaurant.getPrice());
            mActivityAddRestaurantBinding.edtAddress.setText(mRestaurant.getAddress());
            mActivityAddRestaurantBinding.edtImage.setText(getListImages());
        } else {
            mActivityAddRestaurantBinding.toolbar.tvTitle.setText("Thêm nhà hàng");
            mActivityAddRestaurantBinding.btnAddOrEdit.setText("Thêm");
        }
    }

    private String getListImages(){
        String result = "";
        if(mRestaurant == null || mRestaurant.getImage() == null || mRestaurant.getImage().isEmpty()){
            return result;
        }
        for(String image: mRestaurant.getImage()){
            if(StringUtil.isEmpty(result)){
                result = result + image;
            } else {
                result = result + ";" + image;
            }
        }
        return result;
    }

    private void addOrUpdateRestaurant(){
        String strName = mActivityAddRestaurantBinding.edtName.getText().toString().trim();
        String strDes = mActivityAddRestaurantBinding.edtDescription.getText().toString().trim();
        String strPrice = mActivityAddRestaurantBinding.edtPrice.getText().toString().trim();
        String strAddress = mActivityAddRestaurantBinding.edtAddress.getText().toString().trim();
        String strImages = mActivityAddRestaurantBinding.edtImage.getText().toString().trim();
        List<String> listImages = new ArrayList<>();
        if(!StringUtil.isEmpty(strImages)){
            String[] temp = strImages.split(";");
            for(String strUrl: temp){
                listImages.add(strUrl);
            }
        }
        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(strDes)){
            Toast.makeText(this, "Vui lòng nhập mô tả", Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(strAddress)){
            Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(strPrice)){
            Toast.makeText(this, "Vui lòng nhập giá tiền", Toast.LENGTH_SHORT).show();
            return;
        }
        //update
        if(isUpdate){
            mProgressDialog.show();
            RestaurantRequest restaurantRequest = new RestaurantRequest(strName,strAddress,
                    strDes,strPrice,listImages);
            String restaurantId = mRestaurant.get_id();
            Call<RestaurantRequest> call = RestaurantAPI.retrofitService.updateRestaurant(restaurantId,restaurantRequest);
            call.enqueue(new Callback<RestaurantRequest>() {
                @Override
                public void onResponse(Call<RestaurantRequest> call, Response<RestaurantRequest> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(AddRestaurantActivity.this,
                                "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddRestaurantActivity.this,
                                "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<RestaurantRequest> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            return;
        }


        GlobalFunction.startActivity(this,PaymentActivity.class);
        RestaurantRequest restaurantRequest = new RestaurantRequest(strName,strAddress,
                strDes,strPrice,listImages);
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurant_object",restaurantRequest);
        GlobalFunction.startActivity(AddRestaurantActivity.this,PaymentActivity.class,bundle);
    }
}
