package com.example.farmerkonnect;

// ... imports ..

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class FarmerAdapter extends FirebaseRecyclerAdapter<DataModel, FarmerAdapter.FarmerViewHolder> {
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DataModel dataModel, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public FarmerAdapter(@NonNull FirebaseRecyclerOptions<DataModel> options) {
        super(options);
    }
// ... existing code ...

    private FirebaseRecyclerOptions<DataModel> options; // Add a member variable

    public void updateOptions(FirebaseRecyclerOptions<DataModel> options) {
        this.options = options;
        startListening();
    }
// ... rest of your code ...

    @Override
    protected void onBindViewHolder(@NonNull FarmerViewHolder holder, int position, @NonNull DataModel dataModel) {
        holder.farmerName.setText(dataModel.getFarmername());
        holder.farmLocation.setText(dataModel.getFarmLocation());

        // Load dummy profile image (change 'default_image.jpg' to your actual image resource)
        Picasso.get().load("https://images.assetsdelivery.com/compings_v2/joshigraphy/joshigraphy2112/joshigraphy211200075.jpg").into(holder.profileImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position), position);
                }
            }
        });

    }

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new FarmerViewHolder(view);
    }


    public static class FarmerViewHolder extends RecyclerView.ViewHolder {
        TextView farmerName;
        TextView farmLocation;
        ImageView profileImage;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);

            farmerName = itemView.findViewById(R.id.userName);
            farmLocation = itemView.findViewById(R.id.userLocation);
            profileImage = itemView.findViewById(R.id.profilePicture);
        }
    }

}
