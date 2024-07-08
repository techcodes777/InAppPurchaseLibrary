package com.eclatsol.inapppurchase;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class RestartAppActivity extends AppCompatActivity {
//    ActivityRestartAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityRestartAppBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_restart);

//        binding.btnRestartApp.setOnClickListener(view -> {
//            finishAffinity();
//            Intent intent = new Intent(RestartAppActivity.this, SplashActivity.class);
//            startActivity(intent);
//        });
    }
}