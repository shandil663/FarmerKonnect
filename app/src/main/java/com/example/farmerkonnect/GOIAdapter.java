package com.example.farmerkonnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerkonnect.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GOIAdapter extends RecyclerView.Adapter<GOIAdapter.GOIViewHolder> {
    private Context context;
    private List<Record> dataList;

    public GOIAdapter(Context context, List<Record> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public GOIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_grocery, parent, false);
        return new GOIViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GOIViewHolder holder, int position) {
        Record record = dataList.get(position);
        holder.itemNameView.setText(record.getCommodity());
        holder.itemPlaceView.setText(record.getDistrict() + ", " + record.getState());
        holder.itemPriceView.setText("Price: ₹" + record.getModal_price());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        holder.itemDateView.setText("Date: " + dateFormat.format(record.getArrival_date()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void appendData(List<Record> newData) {
        int initialSize = dataList.size();
        dataList.addAll(newData);
        notifyItemRangeInserted(initialSize, newData.size());
    }

    public void clearData() {
        dataList.clear();
        notifyDataSetChanged();
    }

    static class GOIViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView;
        TextView itemPlaceView;
        TextView itemPriceView;
        TextView itemDateView;

        GOIViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            itemPlaceView = itemView.findViewById(R.id.itemPlaceView);
            itemPriceView = itemView.findViewById(R.id.itemPriceView);
            itemDateView = itemView.findViewById(R.id.itemDateView);
        }
    }
}
