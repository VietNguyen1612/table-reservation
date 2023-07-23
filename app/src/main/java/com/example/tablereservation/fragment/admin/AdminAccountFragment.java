package com.example.tablereservation.fragment.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablereservation.activity.AdminAccountDetailActivity;
import com.example.tablereservation.activity.AdminMainActivity;
import com.example.tablereservation.activity.ResOwnerMainActivity;
import com.example.tablereservation.activity.RestaurantDetailActivity;
import com.example.tablereservation.adapter.AdminAccountAdapter;
import com.example.tablereservation.api.accountAPI.AccountAPI;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.FragmentAdminAccountBinding;
import com.example.tablereservation.databinding.FragmentResOwnerCartBinding;
import com.example.tablereservation.fragment.BaseFragment;
import com.example.tablereservation.listener.IOnClickAccountItemListener;
import com.example.tablereservation.model.Account;
import com.example.tablereservation.prefs.DataStoreManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAccountFragment extends BaseFragment {
    private FragmentAdminAccountBinding mFragmentAdminAccountBinding;
    private static ProgressDialog mProgressDialog;
    private List<Account> mListAccounts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAdminAccountBinding = FragmentAdminAccountBinding.inflate(inflater, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        getListAccounts();
        return mFragmentAdminAccountBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar("Danh sách tài khoản");
        }
    }

    private void displayListAccount(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminAccountBinding.rcvAccount.setLayoutManager(layoutManager);
        AdminAccountAdapter adminAccountAdapter = new AdminAccountAdapter(mListAccounts, this::goToAccountDetail);
        mFragmentAdminAccountBinding.rcvAccount.setAdapter(adminAccountAdapter);
    }

    private void getListAccounts(){
        mProgressDialog.show();
        String token = "Bearer "+DataStoreManager.getToken();
        Call<List<Account>> call = AccountAPI.retrofitService.getAllAccounts(token);
        call.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if(response.isSuccessful()){
                    mListAccounts = response.body();
                    displayListAccount();
                } else {
                    Toast.makeText(getContext(), "Lỗi đã xảy ra, vui lòng thử lại sau",
                            Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void goToAccountDetail(@NonNull Account account){
        if(account.getRole().equals("admin")){
            return;
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("account_object",account);
            GlobalFunction.startActivity(getActivity(), AdminAccountDetailActivity.class,bundle);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getListAccounts();
    }
}
