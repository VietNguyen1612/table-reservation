package com.example.tablereservation.prefs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.tablereservation.model.Account;
import com.example.tablereservation.model.User;
import com.example.tablereservation.util.StringUtil;
import com.google.gson.Gson;

public class DataStoreManager {

    public static final String PREF_USER_INFOR = "PREF_USER_INFOR";
    public static final String PREF_TOKEN_INFOR = "PREF_TOKEN_INFOR";
    public static final String PREF_ACCOUNT_INFOR = "PREF_ACCOUNT_INFOR";
    public static final String PREF_CUSTOMER_ID = "PREF_CUSTOMER_ID";
    public static final String PREF_RES_OWNER_ID = "PREF_RES_OWNER_ID";
    private static DataStoreManager instance;
    private MySharedPreferences sharedPreferences;

    public static void init(Context context) {
        instance = new DataStoreManager();
        instance.sharedPreferences = new MySharedPreferences(context);
    }

    public static DataStoreManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }

    public static void setUser(@Nullable User user) {
        String jsonUser = "";
        if (user != null) {
            jsonUser = user.toJSon();
        }
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_USER_INFOR, jsonUser);
    }

    public static User getUser() {
        String jsonUser = DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_USER_INFOR);
        if (!StringUtil.isEmpty(jsonUser)) {
            return new Gson().fromJson(jsonUser, User.class);
        }
        return new User();
    }

    public static void setAccount(@Nullable Account account) {
        String jsonAccount = "";
        if (account != null) {
            jsonAccount = account.toJSon();
        }
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_ACCOUNT_INFOR, jsonAccount);
    }

    public static Account getAccount() {
        String jsonAccount = DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_ACCOUNT_INFOR);
        if (!StringUtil.isEmpty(jsonAccount)) {
            return new Gson().fromJson(jsonAccount, Account.class);
        }
        return new Account();
    }

    public static void setToken(String token){
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_TOKEN_INFOR,token);
    }

    public static String getToken(){
        return DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_TOKEN_INFOR);
    }

    public static void setCustomerID(String customerID){
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_CUSTOMER_ID,customerID);
    }

    public static String getCustomerID(){
        return DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_CUSTOMER_ID);
    }

    public static void setResOwnerID(String resOwnerID){
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_RES_OWNER_ID,resOwnerID);
    }

    public static String getResOwnerID(){
        return DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_RES_OWNER_ID);
    }
}