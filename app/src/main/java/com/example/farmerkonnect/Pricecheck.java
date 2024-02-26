package com.example.farmerkonnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Pricecheck extends AppCompatActivity {

    private static final int REQUEST_CODE_FILTER = 1001;

    private RecyclerView recyclerView;
    private GOIAdapter goiAdapter;
    private List<Record> dataList = new ArrayList<>();
    private int offset = 0;
    private ProgressBar progressBar; // Add a loading indicator
    private String districtFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricecheck);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar); // Find your progress
        goiAdapter = new GOIAdapter(this, dataList);
        recyclerView.setAdapter(goiAdapter);
        fetchInitialData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    offset += 20;
                    fetchData(offset);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void fetchInitialData() {
        fetchData(offset); // Start the initial data fetch
    }

    private void fetchData(int offset) {
        String url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=" + offset + "&limit=20";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            List<Record> newData = parseData(response);
                            goiAdapter.appendData(newData);
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            Toast.makeText(Pricecheck.this, "Error parsing data.", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Pricecheck.this, "Network error occurred.", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);
    }

    private List<Record> parseData(String response) throws JSONException, ParseException {
        List<Record> records = new ArrayList<>();

        JSONObject root = new JSONObject(response);
        JSONArray recordsArray = root.getJSONArray("records");

        for (int i = 0; i < recordsArray.length(); i++) {
            JSONObject recordObj = recordsArray.getJSONObject(i);

            // Extract the required fields from the JSON object
            String state = recordObj.getString("state");
            String district = recordObj.getString("district");
            String market = recordObj.getString("market");
            String commodity = recordObj.getString("commodity");
            String variety = recordObj.getString("variety");
            String grade = recordObj.getString("grade");

            // Assumes your 'arrival_date' is in a standard parsable format
            Date arrivalDate = new SimpleDateFormat("dd/MM/yyyy").parse(recordObj.getString("arrival_date"));

            double minPrice = recordObj.getDouble("min_price");
            double maxPrice = recordObj.getDouble("max_price");
            double modalPrice = recordObj.getDouble("modal_price");

            // Create a Record object
            Record record = new Record(state, district, market, commodity, variety, grade, arrivalDate, minPrice, maxPrice, modalPrice);
            // Add the record to the list
            records.add(record);
        }

        return records;
    }


}
