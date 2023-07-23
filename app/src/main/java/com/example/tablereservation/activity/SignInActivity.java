package com.example.tablereservation.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tablereservation.api.accountAPI.AccountAPI;
import com.example.tablereservation.api.accountAPI.AccountAPIService;
import com.example.tablereservation.api.accountAPI.LoginResponse;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.ActivitySignInBinding;
import com.example.tablereservation.model.Account;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.StringUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity{
    private ActivitySignInBinding mActivitySignInBinding;
    private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignInBinding.getRoot());
        mActivitySignInBinding.layoutSignUp.setOnClickListener(
                v -> GlobalFunction.startActivity(SignInActivity.this, SignUpActivity.class));
        mActivitySignInBinding.btnSignIn.setOnClickListener(v -> onClickValidateSignIn());
        mProgressDialog = new ProgressDialog(this);
    }

    private void onClickValidateSignIn() {
        String strEmail = mActivitySignInBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignInBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignInActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignInActivity.this, "Vui lòng nhập password", Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignInActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
        } else {
            signIn(strEmail,strPassword);
        }
    }

    private void signIn(String email, String password){
        mProgressDialog.show();
        Account account = new Account(email,password,"");
        Call<LoginResponse> call = AccountAPI.retrofitService.login(account);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();
                    Account account = loginResponse.getAccount();
                    DataStoreManager.setAccount(account);
                    DataStoreManager.setToken(token);

                    if(account.getRole().equals("customer"))  {
                        DataStoreManager.setCustomerID(account.getCustomer());
                        GlobalFunction.gotoMainActivity(SignInActivity.this);
                        finishAffinity();
                    } else if(account.getRole().equals("restaurantOwner")) {
                        DataStoreManager.setResOwnerID(account.getRestaurantOwner());
                        GlobalFunction.gotoResOwnerMainActivity(SignInActivity.this);
                        finishAffinity();
                    } else {
                        GlobalFunction.gotoAdminMainActivity(SignInActivity.this);
                        finishAffinity();
                    }


                } else {
                    Toast.makeText(SignInActivity.this, "Đăng nhập không thành công, vui lòng thử lại",
                            Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
