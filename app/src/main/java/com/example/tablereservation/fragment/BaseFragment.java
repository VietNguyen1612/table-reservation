package com.example.tablereservation.fragment;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment  extends Fragment {
    public BaseFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        initToolbar();
    }

    protected abstract void initToolbar();
}
