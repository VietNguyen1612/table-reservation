package com.example.tablereservation.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tablereservation.api.accountAPI.AccountAPI;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.ActivitySignUpBinding;
import com.example.tablereservation.model.Account;
import com.example.tablereservation.util.StringUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity{
    private ActivitySignUpBinding mActivitySignUpBinding;
    private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignUpBinding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignUpBinding.getRoot());
        mActivitySignUpBinding.rdbCustomer.setChecked(true);
        mActivitySignUpBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivitySignUpBinding.layoutSignIn.setOnClickListener(v -> finish());
        mActivitySignUpBinding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
        mProgressDialog = new ProgressDialog(this);
    }

    private void onClickValidateSignUp(){
        String strEmail = mActivitySignUpBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignUpBinding.edtPassword.getText().toString().trim();
        String strName = mActivitySignUpBinding.edtName.getText().toString();
        String strConfirmPassword = mActivitySignUpBinding.edtConfirmPassword.getText().toString().trim();
        String strPhone = mActivitySignUpBinding.edtPhone.getText().toString();
        String strAddress = mActivitySignUpBinding.edtAddress.getText().toString();

        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strName)) {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập password", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strConfirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập lại password", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPhone)) {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập điện thoại", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strAddress)){
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)){
            Toast.makeText(SignUpActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isVietnamPhoneNumber(strPhone)){
            Toast.makeText(SignUpActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (!strPassword.equals(strConfirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Mật khẩu không khớp, vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            signUp();
        }
    }

    private void signUp(){
        mProgressDialog.show();
        Map<String, String> additionalFields = new HashMap<>();
        additionalFields.put("email", mActivitySignUpBinding.edtEmail.getText().toString());
        additionalFields.put("password", mActivitySignUpBinding.edtPassword.getText().toString());
        if(mActivitySignUpBinding.rdbCustomer.isChecked()){
            additionalFields.put("role", "customer");
        } else {
            additionalFields.put("role", "restaurantOwner");
        }
        additionalFields.put("phone", mActivitySignUpBinding.edtPhone.getText().toString());
        additionalFields.put("name", mActivitySignUpBinding.edtName.getText().toString());
        additionalFields.put("address", mActivitySignUpBinding.edtAddress.getText().toString());
        Gson gson = new Gson();
        String json = gson.toJson(additionalFields);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Call<Account> call = AccountAPI.retrofitService.register(body);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this,"Đăng kí thành công",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this,"Đăng kí không thành công, vui lòng thử lại",
                            Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
