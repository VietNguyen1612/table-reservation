package com.example.tablereservation.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tablereservation.fragment.CartFragment;
import com.example.tablereservation.fragment.HomeFragment;
import com.example.tablereservation.fragment.ProfileFragment;
import com.example.tablereservation.fragment.resOwner.ResOwnerCartFragment;
import com.example.tablereservation.fragment.resOwner.ResOwnerHomeFragment;

public class ResOwnerMainViewPagerAdapter extends FragmentStateAdapter {
    public ResOwnerMainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ResOwnerHomeFragment();
            case 1:
                return new ResOwnerCartFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
