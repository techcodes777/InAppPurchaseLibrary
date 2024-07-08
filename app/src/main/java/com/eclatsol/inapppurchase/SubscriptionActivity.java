package com.eclatsol.inapppurchase;

import static com.eclatsol.inapppurchase.purchase.PurchaseClass.onPurchaseInitialization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eclatsol.inapppurchase.adapter.SubscriptionAdapter;
import com.eclatsol.inapppurchase.purchase.Pref;
import com.eclatsol.inapppurchase.purchase.PurchaseClass;

public class SubscriptionActivity extends AppCompatActivity implements SubscriptionAdapter.PurChaseItemClick {


    RecyclerView recyclerViewPurchase;
    SubscriptionAdapter subscriptionAdapter;
    private Button btnSubscription;

    int position = 0;
    private TextView tvNoDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Pref.getInstance().init(getApplicationContext());
        onPurchaseInitialization(this);

        recyclerViewPurchase = findViewById(R.id.recyclerViewPurchase);
        btnSubscription = findViewById(R.id.btnSubscription);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);
        subscriptionAdapter = new SubscriptionAdapter(this, PurchaseClass.productDetailsList, this);
        recyclerViewPurchase.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPurchase.setAdapter(subscriptionAdapter);


        btnSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    PurchaseClass.onClickRemoveAds(position);
                } else {
                    Log.e("main", "onClick: position in empty");
                    Toast.makeText(SubscriptionActivity.this, "Please select subscription", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PurchaseClass.productDetailsList.size() > 0) {
            recyclerViewPurchase.setVisibility(View.VISIBLE);
            tvNoDataFound.setVisibility(View.GONE);
        } else {
            recyclerViewPurchase.setVisibility(View.INVISIBLE);
            tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void position(int pos) {
        position = pos;
    }
}