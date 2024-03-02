package com.example.farmerkonnect;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {

    private List<Crop> croplist;

    public CropAdapter(List<Crop> cropList) {
        this.croplist = cropList;
    }

    @Override
    public CropViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crop_market, parent, false);
        return new CropViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CropViewHolder holder, int position) {
        Crop crop = croplist.get(position);
//        holder.cropImageView.setImageURI(Uri.parse(crop.getImageUrl()));
// Assuming you have logic to handle image loading
        Picasso.get().load(crop.getImageUrl()).into(holder.cropImageView);
        holder.cropNameTextView.setText(crop.getName());
        holder.priceTextView.setText("Price: ₹" + String.format("%.2f", crop.getPrice())+" /Kg"); // Format price to 2 decimal places
        holder.quantityTextView.setText("Quantity: " + crop.getQuantity()+" Ton");

        // Set OnClickListener (currently a no-op, but ready for future use)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your click handling logic here (e.g., navigate to a detail screen)
            }
        });
    }

    @Override
    public int getItemCount() {
        return croplist.size();
    }

    public static class CropViewHolder extends RecyclerView.ViewHolder {

        ImageView cropImageView;
        TextView cropNameTextView, priceTextView, quantityTextView;

        public CropViewHolder(View itemView) {
            super(itemView);
            cropImageView = itemView.findViewById(R.id.cropImageView);
            cropNameTextView = itemView.findViewById(R.id.cropNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }
    }
}
