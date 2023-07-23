package com.example.tablereservation;

import android.app.Application;

import com.example.tablereservation.prefs.DataStoreManager;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class ControllerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "your client id",
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.CONTINUE,
                "com.example.tablereservation://paypalpay"
        ));
        DataStoreManager.init(getApplicationContext());
    }
}
