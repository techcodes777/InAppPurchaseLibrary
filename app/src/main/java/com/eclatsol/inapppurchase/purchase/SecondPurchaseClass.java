package com.eclatsol.inapppurchase.purchase;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.eclatsol.inapppurchase.RestartAppActivity;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class SecondPurchaseClass {

    public static Activity activityMain;
    public static BillingClient billingClient;
    public static List<ProductDetails> productDetailsList = new ArrayList<>();
    public static PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }
    };

    public static void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                        Preferences.setPurchase(true);
                        Pref.getInstance().setString(Constant.PURCHASE_DONE, "done");
                        Intent intent = new Intent(activityMain, RestartAppActivity.class);
                        activityMain.startActivity(intent);
                        activityMain.finish();
                    }
                });
            }
        }
    }

    public static void onPurchaseInitialization(Activity activity) {
        activityMain = activity;
        billingClient = BillingClient.newBuilder(activity)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    showProducts();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public static void showProducts() {

        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("week")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("month")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("year")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();
        billingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                if (list.size() > 0) {
                    productDetailsList.addAll(list);
                    Pref.getInstance().setString(Constant.TITLE, productDetailsList.get(0).getTitle());
                    Pref.getInstance().setString(Constant.PRICE, productDetailsList.get(0).getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());
                    Log.e("title", "onProductDetailsResponse: " + Pref.getInstance().getString(Constant.TITLE));
                    Log.e("price", "onProductDetailsResponse: " + Pref.getInstance().getString(Constant.PRICE));
                }
            }
        });
    }

    public static void onClickRemoveAds() {

        ProductDetails productDetails = productDetailsList.get(0);
        assert productDetails.getSubscriptionOfferDetails() != null;
        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
                                .build()
                );
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        billingClient.launchBillingFlow(activityMain, billingFlowParams);
    }
}
