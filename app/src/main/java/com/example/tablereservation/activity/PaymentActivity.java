package com.example.tablereservation.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tablereservation.api.restaurantAPI.RestaurantAPI;
import com.example.tablereservation.api.restaurantAPI.RestaurantRequest;
import com.example.tablereservation.databinding.ActivityPaymentBinding;
import com.example.tablereservation.databinding.ActivityResOwnerAddRestaurantBinding;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.prefs.DataStoreManager;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends BaseActivity{
    private ActivityPaymentBinding mActivityPaymentBinding;
    private RestaurantRequest mRestaurantRequest;
    private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityPaymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(mActivityPaymentBinding.getRoot());
        initToolbar();
        getDataIntent();
        mProgressDialog = new ProgressDialog(this);
        mActivityPaymentBinding.paymentButtonContainer.setup(createOrderActions -> {
            ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
            purchaseUnits.add(
                    new PurchaseUnit.Builder()
                            .amount(
                                    new Amount.Builder()
                                            .currencyCode(CurrencyCode.USD)
                                            .value("10.00")
                                            .build()
                            )
                            .build()
            );
            OrderRequest orderRequest = new OrderRequest(OrderIntent.CAPTURE,new AppContext.Builder()
                    .userAction(UserAction.PAY_NOW)
                    .build(),
                    purchaseUnits);
            createOrderActions.create(orderRequest,(CreateOrderActions.OnOrderCreated) null);
        }, approval -> {
            approval.getOrderActions().capture(captureOrderResult -> {
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            });
            mActivityPaymentBinding.tvBooking.setVisibility(View.VISIBLE);
            mActivityPaymentBinding.tvBooking.setOnClickListener(v->addRestaurant(mRestaurantRequest));
        });
    }

    private void addRestaurant(RestaurantRequest restaurantRequest){
        mProgressDialog.show();
        String token = "Bearer "+ DataStoreManager.getToken();
        Call<RestaurantRequest> call = RestaurantAPI.retrofitService.createRestaurant(token,restaurantRequest);
        call.enqueue(new Callback<RestaurantRequest>() {
            @Override
            public void onResponse(Call<RestaurantRequest> call, Response<RestaurantRequest> response) {
                if(response.isSuccessful()){
                    Toast.makeText(PaymentActivity.this,
                            "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentActivity.this,
                            "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RestaurantRequest> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initToolbar(){
        mActivityPaymentBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityPaymentBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRestaurantRequest = (RestaurantRequest) bundle.get("restaurant_object");
        }
    }
}
