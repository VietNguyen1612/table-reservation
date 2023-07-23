package com.example.tablereservation.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tablereservation.R;
import com.example.tablereservation.adapter.AdminMainViewPagerAdapter;
import com.example.tablereservation.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends BaseActivity{
    private ActivityAdminMainBinding mActivityAdminMainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminMainBinding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminMainBinding.getRoot());
        AdminMainViewPagerAdapter adminMainViewPagerAdapter = new AdminMainViewPagerAdapter(this);
        mActivityAdminMainBinding.viewpager2.setAdapter(adminMainViewPagerAdapter);
        mActivityAdminMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_restaurant).setChecked(true);
                        break;

                    case 1:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_account).setChecked(true);
                        break;

                    case 2:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                }
            }
        });
        mActivityAdminMainBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_restaurant) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_account) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_profile) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(2);
            }
            return true;
        });
    }

    public void setToolBar(String title) {
        mActivityAdminMainBinding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mActivityAdminMainBinding.toolbar.tvTitle.setText(title);
    }
}
