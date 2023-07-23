package com.example.tablereservation.constant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.tablereservation.activity.AdminMainActivity;
import com.example.tablereservation.activity.MainActivity;
import com.example.tablereservation.activity.ResOwnerMainActivity;

public class GlobalFunction {
    public static void startActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void gotoMainActivity(Context context) {
            GlobalFunction.startActivity(context, MainActivity.class);
    }

    public static void gotoResOwnerMainActivity(Context context) {
        GlobalFunction.startActivity(context, ResOwnerMainActivity.class);
    }

    public static void gotoAdminMainActivity(Context context) {
        GlobalFunction.startActivity(context, AdminMainActivity.class);
    }
}
