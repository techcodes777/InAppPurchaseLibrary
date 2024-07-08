package com.eclatsol.inapppurchase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.ProductDetails;
import com.eclatsol.inapppurchase.R;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubViewHolder> {

    List<ProductDetails> productDetailsList;
    Context context;

    PurChaseItemClick purChaseItemClick;


    public SubscriptionAdapter(Context context, List<ProductDetails> productDetailsList,PurChaseItemClick purChaseItemClick) {
        this.context = context;
        this.productDetailsList = productDetailsList;
        this.purChaseItemClick = purChaseItemClick;
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscription, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {

        holder.tvTitle.setText(productDetailsList.get(position).getTitle());
        holder.tvTitle.setText(productDetailsList.get(position).getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purChaseItemClick.position(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }

    public class SubViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvPrice;
        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    public  interface PurChaseItemClick{
        void position(int pos);
    }
}
