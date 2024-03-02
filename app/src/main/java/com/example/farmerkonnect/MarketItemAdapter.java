package com.example.farmerkonnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MarketItemAdapter extends RecyclerView.Adapter<MarketItemAdapter.MarketViewHolder> {

    private List<Market> marketList;

    public MarketItemAdapter(List<Market> marketList) {
        this.marketList = marketList;
    }

    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.price_list_item, parent, false);
        return new MarketViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        Market market = marketList.get(position);
        holder.marketNameTextView.setText(market.getName());
        holder.marketPriceTextView.setText(String.valueOf(market.getPrice()));
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    public void setMarketData(List<Market> newMarketList) {
        this.marketList = newMarketList;
    }

    // Inner Class
    public static class MarketViewHolder extends RecyclerView.ViewHolder {
        TextView marketNameTextView;
        TextView marketPriceTextView;

        public MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            marketNameTextView = itemView.findViewById(R.id.marketName);
            marketPriceTextView = itemView.findViewById(R.id.marketPrice);
        }
    }
}
