package com.example.tablereservation.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tablereservation.activity.AdminMainActivity;
import com.example.tablereservation.activity.MainActivity;
import com.example.tablereservation.activity.ResOwnerMainActivity;
import com.example.tablereservation.activity.SignInActivity;
import com.example.tablereservation.api.userAPI.UserAPI;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.FragmentProfileBinding;
import com.example.tablereservation.model.Account;
import com.example.tablereservation.model.User;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.StringUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment {
    private static final int REQUEST_CODE = 2;
    private FragmentProfileBinding mFragmentProfileBinding;
    private static ProgressDialog mProgressDialog;
    private static String mUserId;
    private static Account mAccount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        mProgressDialog = new ProgressDialog(getContext());
            getProfileData();
        mFragmentProfileBinding.layoutSignOut.setOnClickListener(v->onClickSignOut());
        mFragmentProfileBinding.layoutContact.setOnClickListener(v->btnmessage());
        return mFragmentProfileBinding.getRoot();
    }

    private void btnmessage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE);
        } else {
            String phoneNumber = "0933857813";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null));
            startActivity(intent);
        }
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            if(StringUtil.isEmpty(DataStoreManager.getCustomerID()) && !StringUtil.isEmpty(DataStoreManager.getResOwnerID())){
                ((ResOwnerMainActivity) getActivity()).setToolBar("Tài khoản");
            } else if(!StringUtil.isEmpty(DataStoreManager.getCustomerID()) && StringUtil.isEmpty(DataStoreManager.getResOwnerID())){
                ((MainActivity) getActivity()).setToolBar(false,"Tài khoản");
            } else {
                ((AdminMainActivity) getActivity()).setToolBar("Tài khoản");
            }
        }
    }

    private void getProfileData(){
        mProgressDialog.show();
        if(DataStoreManager.getAccount() != null){
            mAccount = DataStoreManager.getAccount();
        }
        mFragmentProfileBinding.tvEmail.setText(mAccount.getEmail());
        if (!StringUtil.isEmpty(DataStoreManager.getCustomerID()) && StringUtil.isEmpty(DataStoreManager.getResOwnerID())) {
            mUserId = DataStoreManager.getCustomerID();
            Call<User> call = UserAPI.retrofitService.getCustomerById(mUserId);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        User user = response.body();
                        mFragmentProfileBinding.tvName.setText("Tên: "+user.getName());
                        mFragmentProfileBinding.tvPhone.setText("Điện thoại: "+user.getPhone());
                        mFragmentProfileBinding.tvAddress.setText("Địa chỉ: "+user.getAddress());
                        mProgressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Có lỗi xảy ra. Vui lòng thử lại",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("error",call.toString());
                    t.printStackTrace();
                }
            });
        } else if(StringUtil.isEmpty(DataStoreManager.getCustomerID()) && !StringUtil.isEmpty(DataStoreManager.getResOwnerID())){
            mUserId = DataStoreManager.getResOwnerID();
            Call<User> call = UserAPI.retrofitService.getResOwnerById(mUserId);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        User user = response.body();
                        mFragmentProfileBinding.tvName.setText("Tên: "+user.getName());
                        mFragmentProfileBinding.tvPhone.setText("Điện thoại: "+user.getPhone());
                        mFragmentProfileBinding.tvAddress.setText("Địa chỉ: "+user.getAddress());
                        mProgressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Có lỗi xảy ra. Vui lòng thử lại",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            mProgressDialog.dismiss();
            mFragmentProfileBinding.tvEmail.setVisibility(View.GONE);
            mFragmentProfileBinding.tvAddress.setVisibility(View.GONE);
            mFragmentProfileBinding.tvPhone.setVisibility(View.GONE);
            mFragmentProfileBinding.tvName.setVisibility(View.GONE);
            mFragmentProfileBinding.view1.setVisibility(View.GONE);
            mFragmentProfileBinding.view2.setVisibility(View.GONE);
            mFragmentProfileBinding.view3.setVisibility(View.GONE);
            mFragmentProfileBinding.view4.setVisibility(View.GONE);
            mFragmentProfileBinding.image.setVisibility(View.GONE);
        }
    }

    private void onClickSignOut() {
        if (getActivity() == null) {
            return;
        }
        DataStoreManager.setToken(null);
        DataStoreManager.setAccount(null);
        DataStoreManager.setCustomerID(null);
        DataStoreManager.setResOwnerID(null);
        Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        GlobalFunction.startActivity(getActivity(), SignInActivity.class);
        getActivity().finishAffinity();
    }
}