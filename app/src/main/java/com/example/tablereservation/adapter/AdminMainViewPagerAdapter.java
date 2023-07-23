package com.example.tablereservation.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tablereservation.fragment.HomeFragment;
import com.example.tablereservation.fragment.ProfileFragment;
import com.example.tablereservation.fragment.admin.AdminAccountFragment;
import com.example.tablereservation.fragment.admin.AdminRestaurantFragment;
import com.example.tablereservation.fragment.resOwner.ResOwnerCartFragment;
import com.example.tablereservation.fragment.resOwner.ResOwnerHomeFragment;

public class AdminMainViewPagerAdapter extends FragmentStateAdapter {

    public AdminMainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AdminRestaurantFragment();
            case 1:
                return new AdminAccountFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new AdminRestaurantFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
