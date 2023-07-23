package com.example.tablereservation.activity;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.example.tablereservation.R;
import com.example.tablereservation.adapter.MainViewPagerAdapter;
import com.example.tablereservation.adapter.ResOwnerMainViewPagerAdapter;
import com.example.tablereservation.databinding.ActivityResOwnerMainBinding;

public class ResOwnerMainActivity extends BaseActivity{
    private ActivityResOwnerMainBinding mActivityResOwnerMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityResOwnerMainBinding = ActivityResOwnerMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityResOwnerMainBinding.getRoot());
        ResOwnerMainViewPagerAdapter resOwnerMainViewPagerAdapter = new ResOwnerMainViewPagerAdapter(this);
        mActivityResOwnerMainBinding.viewpager2.setAdapter(resOwnerMainViewPagerAdapter);
        mActivityResOwnerMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mActivityResOwnerMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;

                    case 1:
                        mActivityResOwnerMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_cart).setChecked(true);
                        break;

                    case 2:
                        mActivityResOwnerMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                }
            }
        });

        mActivityResOwnerMainBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                mActivityResOwnerMainBinding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_cart) {
                mActivityResOwnerMainBinding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_profile) {
                mActivityResOwnerMainBinding.viewpager2.setCurrentItem(2);
            }
            return true;
        });
    }

    public void setToolBar(String title) {
        mActivityResOwnerMainBinding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mActivityResOwnerMainBinding.toolbar.tvTitle.setText(title);
    }
}
