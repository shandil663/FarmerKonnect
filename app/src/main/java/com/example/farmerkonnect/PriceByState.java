package com.example.farmerkonnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceByState extends AppCompatActivity {

    private Spinner stateSpinner, districtSpinner, cropSpinner;
    private ImageView commodityImage;

    CardView cardphoto;
    private RecyclerView marketsRecyclerView;
    private MarketItemAdapter marketItemAdapter;

    private DatabaseReference pricesRef;
    private List<String> statesList;
    private Map<String, List<String>> districtsByState;
    private List<String> cropsList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_by_state);
        stateSpinner = findViewById(R.id.stateSpinner);
        districtSpinner = findViewById(R.id.districtSpinner);
        cropSpinner = findViewById(R.id.cropSpinner);
        commodityImage = findViewById(R.id.commodityImage);
        marketsRecyclerView = findViewById(R.id.marketsRecyclerView);

        pricesRef = FirebaseDatabase.getInstance().getReference("commodities");
        statesList = new ArrayList<>();
        districtsByState = new HashMap<>();
        cropsList1 = new ArrayList<>();
        cardphoto=findViewById(R.id.cardphoto);

        marketsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        marketItemAdapter = new MarketItemAdapter(new ArrayList<>());
        marketsRecyclerView.setAdapter(marketItemAdapter);

        populateStates();
        populateCrops();


        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedState = statesList.get(i);
                populateDistrictsLocally();
                populateDistricts(selectedState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchDataAndPopulateGrid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void populateStates() {
        // *** Fetch states from Firebase or a local data source ***
        // Example (add your states here):
        statesList.add("Punjab");
        statesList.add("Uttar Pradesh");
        statesList.add("Himachal Pradesh");
        statesList.add("Maharashtra");
        statesList.add("Gujarat");
        Spinner spinner = findViewById(R.id.stateSpinner);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, statesList);
        spinner.setAdapter(adapter);
//        ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, statesList);
//        stateSpinner.setAdapter(statesAdapter);
    }
    private void populateDistrictsLocally() {
        List<String> punjabDistricts = Arrays.asList("Amritsar", "Bathinda", "Faridkot", "Firozpur", "Fatehgarh Sahib", "Kapurthala", "Gurdaspur", "Hoshiarpur", "Jalandhar", "Ludhiana", "Mansa", "Moga", "Muktsar", "Nawanshahr", "Patiala", "Rupnagar", "Sangrur","Tarn Taran");
        List<String> maharashtraDistricts = Arrays.asList("Pune", "Mumbai", "Nagpur");
        List<String> gujaratDistricts = Arrays.asList("Ahmedabad", "Surat", "Vadodara");
        districtsByState.put("Punjab", punjabDistricts);
        districtsByState.put("Maharashtra", maharashtraDistricts);
        districtsByState.put("Gujarat", gujaratDistricts);
    }

    private void populateDistricts(String selectedState) {
        // *** Fetch districts based on selectedState (Firebase or local data) ***
        // Example (replace with your data):
        List<String> districts = districtsByState.get(selectedState);

        if (districts != null) {
//            ArrayAdapter<String> districtsAdapter = new ArrayAdapter<>(this,
//                    android.R.layout.simple_spinner_item, districts);
//            districtSpinner.setAdapter(districtsAdapter);
            Spinner spinner = findViewById(R.id.districtSpinner);
            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, districts);
            spinner.setAdapter(adapter);
        } else {
            // Handle the case where no districts are found for the state
        }
    }

    private void populateCrops() {
        // *** Fetch crops from Firebase or a local data source ***
        // Example (replace with your data):
        cropsList1.add("Rice");
        cropsList1.add("Wheat");
        cropsList1.add("Sugarcane");
        cropsList1.add("Tomato");

//        ArrayAdapter<String> cropsAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, cropsList);
//        cropSpinner.setAdapter(cropsAdapter);
        Spinner spinner = findViewById(R.id.cropSpinner);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, cropsList1);
        spinner.setAdapter(adapter);
    }

    private void fetchDataAndPopulateGrid() {
        String selectedState = stateSpinner.getSelectedItem().toString();
        String selectedDistrict = districtSpinner.getSelectedItem().toString();
        String selectedCommodity = cropSpinner.getSelectedItem().toString();

        Log.d("slecected",selectedState+selectedCommodity+selectedDistrict);
        pricesRef.child(selectedCommodity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                if (imageUrl != null) {
                    Log.d("url",imageUrl);
                    cardphoto.setVisibility(View.VISIBLE);
                    Picasso.get().load(imageUrl).into(commodityImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pricesRef.child(selectedCommodity)
                .child("prices")
                .child(selectedState)
                .child(selectedDistrict)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        List<Market> marketList = new ArrayList<>();
                        for (DataSnapshot marketSnapshot : snapshot.getChildren()) {
                            String marketName = marketSnapshot.getKey();
                            int price = marketSnapshot.getValue(Integer.class);
                            marketList.add(new Market(marketName, price));
                        }

                        marketItemAdapter.setMarketData(marketList);
                        marketItemAdapter.notifyDataSetChanged();

                        // Fetch Image URL and load with Picasso

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PriceByState.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
