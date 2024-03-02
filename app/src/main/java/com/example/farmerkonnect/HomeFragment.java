package com.example.farmerkonnect;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import android.Manifest;
import android.content.pm.PackageManager;

import android.widget.Toast;


import androidx.core.app.ActivityCompat;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    RequestQueue queue;
    private Switch languageSwitch;
    private SharedPreferences sharedPreferences;
    BottomNavigationListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomNavigationListener");
        }
    }
    private static final String PREFS_NAME = "myPreferences";
    private static final String SWITCH_STATE = "switch_state";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView asbuttondetect, price;
    Button swt;
    TextView cityNameTextView, temperatureTextView, descriptionTextView, username;
    ImageView weatherIconImageView,sch1,sch2,sch3;
    TextView txt1,txt2,txt3,txt4,tx1,tx2,tx3,tx4,tx5,tx6,tx7,tx8,tx9,tx10;

    String forusername;

    Switch aSwitch;

    LottieAnimationView lottieAnimationView;

    FirebaseAuth oth;
    FirebaseDatabase db;
    BottomNavigationView bottomNavigationView;

    private final ActivityResultLauncher<String> requestLocationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getCurrentLocation();
                } else {
                    // Permission denied. 
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Permission permanently denied; guide the user to enable the permission in settings.
                        showGoToSettingsDialog(); // You'll need to implement this
                    } else {
                        // Optionally, explain why permission is needed again
                    }
                }
            });

    private void showGoToSettingsDialog() {
    }

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private final String API_KEY = "81db5ed1eb0f5f95a982f8a0ea6ed8e3";
    private FusedLocationProviderClient fusedLocationClient;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        username = rootView.findViewById(R.id.usernamehere);
        sch1=rootView.findViewById(R.id.sch1);
        sch2=rootView.findViewById(R.id.sch2);
        sch3=rootView.findViewById(R.id.sch3);
        txt1=rootView.findViewById(R.id.textView4);
        txt2=rootView.findViewById(R.id.tools);
        txt3=rootView.findViewById(R.id.gov);
        txt4=rootView.findViewById(R.id.el);
        tx1=rootView.findViewById(R.id.dis);
        tx2=rootView.findViewById(R.id.gu);
        tx3=rootView.findViewById(R.id.track);
        tx4=rootView.findViewById(R.id.forum);
        tx5=rootView.findViewById(R.id.el1);
        tx6=rootView.findViewById(R.id.dis1);
        tx7=rootView.findViewById(R.id.gu1);
        tx8=rootView.findViewById(R.id.track1);
        tx9=rootView.findViewById(R.id.forum1);
        tx10=rootView.findViewById(R.id.el2);
        oth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
//        bottomNavigationView = rootView.findViewById(R.id.bottomview);
        languageSwitch = rootView.findViewById(R.id.languageSwitch);
        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean switchState = sharedPreferences.getBoolean(SWITCH_STATE, false); // Default to false if not found
        if (switchState) {
            performPunjabiOperation();
        } else {
            performEnglishOperation();
        }
        languageSwitch.setChecked(switchState);
        languageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Code for Punjabi (On state)
                performPunjabiOperation();
            } else {
                // Code for English (Off state)
                performEnglishOperation();
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SWITCH_STATE, isChecked);
            editor.apply();
        });
        DatabaseReference ref = db.getReference("FarmersInfo");
        ref.child(oth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataModel model = snapshot.getValue(DataModel.class);
                forusername = model.getFarmername();
                username.setText(forusername);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cityNameTextView = rootView.findViewById(R.id.city_name_text_view);
        temperatureTextView = rootView.findViewById(R.id.temperature_text_view);
        descriptionTextView = rootView.findViewById(R.id.description_text_view);
        weatherIconImageView = rootView.findViewById(R.id.weather_icon_image_view);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        requestLocationIfNecessary();

        return rootView;
    }

    private void requestLocationIfNecessary() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getCurrentLocation();
        }
    }


    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user (if needed)
            Toast.makeText(getActivity(), "Location permission needed to get weather", Toast.LENGTH_LONG).show();
        }

        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(requireActivity(), location -> {
//                    if (location != null) {
//                        fetchWeatherData(location.getLatitude(), location.getLongitude());
//                    } else {
//                        Toast.makeText(getActivity(), "Unable to get location", Toast.LENGTH_SHORT).show();
//                    }
//                });


//        LocationRequest locationRequest = LocationRequest.create()
//                .setInterval(10000)          // Update interval (optional - adjust as needed)
//                .setFastestInterval(5000)    // Fastest desired interval (optional)
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//                Location location = locationResult.getLastLocation();
//                if (location != null) {
//                    fetchWeatherData(location.getLatitude(), location.getLongitude());
//                    fusedLocationClient.removeLocationUpdates(this); // Stop updates after getting one
//                } else {
//                    Toast.makeText(getActivity(), "Unable to get location", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, Looper.getMainLooper());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }


        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    Toast.makeText(getActivity(), "Cannot get location.", Toast.LENGTH_SHORT).show();
                }
                else{if(isAdded()){
                    fetchWeatherData(location.getLatitude(), location.getLongitude());}
                }
            }
        });

    }


    private void fetchWeatherData(double latitude, double longitude) {
        // ... (Implementation for fetching data from OpenWeatherMap API)
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (isAdded()) { // Check if fragment is attached
                            updateWeatherUI(response);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            if (isAdded()) {
                Toast.makeText(getActivity(), "Weather data error", Toast.LENGTH_SHORT).show();
            }
        }
        );

        queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }



    private void updateWeatherUI(JSONObject response) throws JSONException {
        // ... (Process the JSON response and update the UI)
        String cityName = response.getString("name");
        JSONObject main = response.getJSONObject("main");
        double temperature = main.getDouble("temp");
        JSONArray weatherArray = response.getJSONArray("weather");
        JSONObject weatherObject = weatherArray.getJSONObject(0);
        String description = weatherObject.getString("description");
        String iconCode = weatherObject.getString("icon");
        Log.d("WeatherFragment", "Icon Code: " + iconCode);
        // Update UI elements
        cityNameTextView.setText(cityName);
        temperatureTextView.setText(String.format("%.0f°C", temperature));
        descriptionTextView.setText(description);
        // Load weather icon (You'll likely want a way to map icon codes to image resources)
        // Example using Picasso library (You'll need to add the dependency in build.gradle)
        String urlhere = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        String iconUrl = urlhere;
        Picasso.get().load(iconUrl).into(weatherIconImageView);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        asbuttondetect = view.findViewById(R.id.detect);




        price = view.findViewById(R.id.price);
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), PriceByState.class));
            }
        });
        asbuttondetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Diseasesdetection.class));
            }
        });
        ImageSlider imageSlider = view.findViewById(R.id.imageslider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.yojna1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.yojna4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.yojn3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.yojna2, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(this); // Cancel requests on fragment stop
        }
    }

    private void performPunjabiOperation() {
        txt1.setText(R.string.phello);
        txt2.setText(R.string.ptool);
        txt3.setText(R.string.pscheme);
        txt4.setText(R.string.plearning);
        tx1.setText(R.string.pdis);
        tx2.setText(R.string.pguides);
        tx3.setText(R.string.ptracker);
        tx4.setText(R.string.pforum);
        tx5.setText(R.string.plearning);
        tx6.setText(R.string.pdis);
        tx7.setText(R.string.pguides);
        tx8.setText(R.string.ptracker);
        tx9.setText(R.string.pforum);
        tx10.setText(R.string.plearning);
        sch1.setImageResource(R.drawable.yojnapun1);
        sch2.setImageResource(R.drawable.yojnapun2);
        sch3.setImageResource(R.drawable.yojnapun3);
        BottomNavigationView bottomNavigationView = listener.getBottomNavigationView();
        if (bottomNavigationView != null) {
            Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0); // Second item has index 1
        menuItem.setTitle("ਮੁੱਖ ਪੰਨਾ");
        MenuItem menuItem1 = menu.getItem(1);
        menuItem1.setTitle("ਸੁਨੇਹਾ");
        MenuItem menuItem2 = menu.getItem(2);
        menuItem2.setTitle("ਸਬਜ਼ੀ ਮੰਡੀ");
        MenuItem menuItem3 = menu.getItem(3);
        menuItem3.setTitle("ਸਖਾਤਾ");}
        languageSwitch.setText("Punjabi");
        // Your Punjabi-specific operations here
    }

    private void performEnglishOperation() {
        txt1.setText(R.string.hello);
        txt2.setText(R.string.discover_tools);
        txt3.setText(R.string.government_schemes);
        txt4.setText(R.string.e_learning);
        tx1.setText(R.string.disease_detection);
        tx2.setText(R.string.farming_guides);
        tx3.setText(R.string.price_tracker);
        tx4.setText(R.string.community_forum);
        tx5.setText(R.string.e_learning);
        tx6.setText(R.string.disease_detection);
        tx7.setText(R.string.farming_guides);
        tx8.setText(R.string.price_tracker);
        tx9.setText(R.string.community_forum);
        tx10.setText(R.string.e_learning);
        sch1.setImageResource(R.drawable.yojna4);
        sch2.setImageResource(R.drawable.yojna1);
        sch3.setImageResource(R.drawable.yojn3);
        BottomNavigationView bottomNavigationView = listener.getBottomNavigationView();
        if (bottomNavigationView != null) {
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.getItem(0); // Second item has index 1
            menuItem.setTitle("Home");
            MenuItem menuItem1 = menu.getItem(1);
            menuItem1.setTitle("Inbox");
            MenuItem menuItem2 = menu.getItem(2);
            menuItem2.setTitle("Marketplace");
            MenuItem menuItem3 = menu.getItem(3);
            menuItem3.setTitle("Account");}
        languageSwitch.setText("English");
        // Your English-specific operations here
    }
}