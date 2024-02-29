package com.example.farmerkonnect;// ... imports ...

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class inbox extends Fragment {
    private RecyclerView recyclerView;
    private FarmerAdapter farmerAdapter;
    private DatabaseReference usersRef;
    private EditText searchEditText;
    private FirebaseRecyclerOptions<DataModel> options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 1. Inflate the layout
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        // 2. Find the RecyclerView
        recyclerView = view.findViewById(R.id.usersRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        options = new FirebaseRecyclerOptions.Builder<DataModel>()
                .setQuery(usersRef, DataModel.class)
                .build();
        // 3. Set layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 4. Get Firebase Database reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("FarmersInfo");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        FirebaseRecyclerOptions<DataModel> options = new FirebaseRecyclerOptions.Builder<DataModel>()
                .setQuery(usersRef, DataModel.class)
                .build();

        farmerAdapter = new FarmerAdapter(options); // Create FarmerAdapter class below
        recyclerView.setAdapter(farmerAdapter);
        farmerAdapter.startListening();

        farmerAdapter.setOnItemClickListener(new FarmerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataModel dataModel, int position) {
                searchEditText.setText("");
                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                chatIntent.putExtra("selected_user_id", dataModel.getUserid()); // Assuming you have a 'getUid' method
                chatIntent.putExtra("selected_user_name", dataModel.getFarmername());
                startActivity(chatIntent);
            }
        });


    }

    private void searchUsers(String searchQuery) {
//        if (!searchQuery.isEmpty()) {
//            Query query = usersRef.orderByChild("farmername")
//                    .startAt(searchQuery)
//                    .endAt(searchQuery + "\uf8ff");
//
//            FirebaseRecyclerOptions<DataModel> filteredOptions = new FirebaseRecyclerOptions.Builder<DataModel>()
//                    .setQuery(query, DataModel.class)
//                    .build();
//
//            farmerAdapter.updateOptions(filteredOptions);
//        } else {
//            farmerAdapter.updateOptions(options); // Reset to original options
//        }
        Query query = usersRef.orderByChild("farmername")
                .startAt(searchQuery)
                .endAt(searchQuery + "\uf8ff");

        FirebaseRecyclerOptions<DataModel> filteroptions = new FirebaseRecyclerOptions.Builder<DataModel>()
                .setQuery(query, DataModel.class)
                .build();

        farmerAdapter = new FarmerAdapter(filteroptions); // Create FarmerAdapter class below
        recyclerView.setAdapter(farmerAdapter);
        farmerAdapter.startListening();

        farmerAdapter.setOnItemClickListener(new FarmerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataModel dataModel, int position) {
                searchEditText.setText("");
                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                chatIntent.putExtra("selected_user_id", dataModel.getUserid()); // Assuming you have a 'getUid' method
                chatIntent.putExtra("selected_user_name", dataModel.getFarmername());
                startActivity(chatIntent);
            }
        });
    }


    // ... onStop() with farmerAdapter.stopListening() ...

    // ... Add item click handler here ...
}
