package com.example.farmerkonnect;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActiveMarket extends AppCompatActivity {

    private RecyclerView cropsRecyclerView;
    private List<Crop> croplist2;
    private CropAdapter cropAdapter; // You'll need to create this adapter
    private DatabaseReference cropsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_market);

        cropsRecyclerView = findViewById(R.id.cropsRecyclerView);
        cropsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        croplist2 = new ArrayList<>();
        cropAdapter = new CropAdapter(croplist2);
        cropsRecyclerView.setAdapter(cropAdapter);

        // Firebase Initialization
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        cropsRef = database.getReference("crops");

        fetchCropData();
    }

    private void fetchCropData() {
        cropsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                croplist2.clear();
                for (DataSnapshot cropSnapshot : dataSnapshot.getChildren()) {
                    Crop crop = cropSnapshot.getValue(Crop.class);
                    croplist2.add(crop);
                }
                cropAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
            }
        });
    }
}
