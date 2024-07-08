package com.eclatsol.inapppurchase;

import static com.eclatsol.inapppurchase.purchase.PurchaseClass.onPurchaseInitialization;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.eclatsol.inapppurchase.purchase.Pref;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pref.getInstance().init(getApplicationContext());
        onPurchaseInitialization(this);

    }
}