package com.example.tablereservation.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.ActivitySplashBinding;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.StringUtil;

public class SplashActivity extends BaseActivity{
    private ActivitySplashBinding mActivitySplashBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mActivitySplashBinding.getRoot());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::goToNextActivity, 2000);
    }

    private void goToNextActivity() {
        if (DataStoreManager.getAccount() != null && !StringUtil.isEmpty(DataStoreManager.getAccount().getEmail())) {
            if(StringUtil.isEmpty(DataStoreManager.getCustomerID()) && StringUtil.isEmpty(DataStoreManager.getResOwnerID())){
                GlobalFunction.gotoAdminMainActivity(this);
            } else if(StringUtil.isEmpty(DataStoreManager.getCustomerID())){
                GlobalFunction.gotoResOwnerMainActivity(this);
            } else{
                GlobalFunction.gotoMainActivity(this);
            }

        } else {
            GlobalFunction.startActivity(this, SignInActivity.class);
        }
    }
}
