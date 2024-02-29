package com.example.farmerkonnect;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Onboardpage extends AppCompatActivity {
    Button button,getlocation;

    FirebaseAuth Muth;
    RequestQueue queue;
    FirebaseDatabase db;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private final String API_KEY = "81db5ed1eb0f5f95a982f8a0ea6ed8e3";
    private FusedLocationProviderClient fusedLocationClient;
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
    private void requestLocationIfNecessary() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getCurrentLocation();
        }
    }

    EditText Farmername, Farmerage, Farmeraddress, Farmeremail, Farmsize, FarmLocation, Farmcrop, Farmcultivation, Farmworker, Farmlivestocks, Farmchannel, Farmergoal, Farmertech, Farmerinterest, FarmerChallenges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboardpage);
        getlocation=findViewById(R.id.getlocationonboard);
        Farmername = findViewById(R.id.username);
        Farmeraddress = findViewById(R.id.farmeradd);
        Farmeremail = findViewById(R.id.emailadd);
        Farmerage = findViewById(R.id.age);
        Farmsize = findViewById(R.id.farmsize1);
        FarmLocation = findViewById(R.id.locationfarm);
        Farmcrop = findViewById(R.id.crops1);
        Farmcultivation = findViewById(R.id.soil1);
        Farmworker = findViewById(R.id.people);
        Farmlivestocks = findViewById(R.id.livestocks);
        Farmchannel = findViewById(R.id.channel);
        Farmergoal = findViewById(R.id.goal);
        Farmertech = findViewById(R.id.tech);
        Farmerinterest = findViewById(R.id.interest);
        FarmerChallenges = findViewById(R.id.major);


        button = findViewById(R.id.gohome);
        Muth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();



        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                requestLocationIfNecessary();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel dt = new DataModel(Farmername.getText().toString().trim(), Farmerage.getText().toString().trim(), Farmeraddress.getText().toString().trim(), Farmeremail.getText().toString().trim(), Farmsize.getText().toString().trim(), FarmLocation.getText().toString().trim(), Farmcrop.getText().toString().trim(), Farmcultivation.getText().toString().trim(), Farmworker.getText().toString().trim(), Farmlivestocks.getText().toString().trim(), Farmchannel.getText().toString().trim(), Farmergoal.getText().toString().trim(), Farmertech.getText().toString().trim(), Farmerinterest.getText().toString().trim(), FarmerChallenges.getText().toString().trim(),
                Muth.getCurrentUser().getUid());
                FirebaseUser currentuser = Muth.getCurrentUser();
                if (currentuser != null) {
                    String uid = currentuser.getUid();
                    DatabaseReference ref = db.getReference("FarmersInfo");
                    ref.child(uid).setValue(dt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Onboardpage.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Onboardpage.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }

                else {
                    Toast.makeText(Onboardpage.this, "User not signed in", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
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
                    Toast.makeText(getApplicationContext(), "Cannot get location.", Toast.LENGTH_SHORT).show();
                }
                else{
                    fetchWeatherData(location.getLatitude(), location.getLongitude());}

            }
        });

    }

    private void fetchWeatherData(double latitude, double longitude) {
        // ... (Implementation for fetching data from OpenWeatherMap API)
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                            updateWeatherUI(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

            Toast.makeText(getApplicationContext(), "Weather data error", Toast.LENGTH_SHORT).show();


        });

        queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void updateWeatherUI(JSONObject response) throws JSONException {
        String cityName = response.getString("name");
        FarmLocation.setText(cityName);


    }
}