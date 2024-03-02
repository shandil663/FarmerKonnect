package com.example.farmerkonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
// ... other imports

public class SchemeFragment extends Fragment {

    private CardView farmerCardView;
    private CardView consumerCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scheme, container, false);

        farmerCardView = view.findViewById(R.id.farmerCardView);
        consumerCardView = view.findViewById(R.id.consumerCardView);

        // Set click listeners for the CardViews
        farmerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadCrops.class);
                startActivity(intent);
            }
        });

        consumerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActiveMarket.class);
                startActivity(intent);

            }
        });

        return view;
    }
}
