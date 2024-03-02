package com.example.farmerkonnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UploadCrops extends AppCompatActivity {

    // Variable Declarations
    private AppCompatAutoCompleteTextView cropNameEditText;
    private EditText priceEditText, quantityEditText;
    private ImageView cropImageView;
    private Button uploadButton;
    private Uri imageUri;

    // Firebase Declarations
    private FirebaseDatabase database;
    private DatabaseReference cropsRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Spinner cropSpinner;
   List<String> cropsList = new ArrayList<>();

    // Image Picker Launcher
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_crops);

        // Initialize Views

        cropSpinner = findViewById(R.id.cropSpinner);
        priceEditText = findViewById(R.id.priceEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        cropImageView = findViewById(R.id.cropImageView);
        uploadButton = findViewById(R.id.uploadButton);

        // Firebase Initialization
        database = FirebaseDatabase.getInstance();
        cropsRef = database.getReference("crops");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("crop_images");

        // Setup AutoCompleteTextView

        populateCrops();

        // Image Picker Setup
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        cropImageView.setImageURI(imageUri);
                    }
                });

        // Image Selection (OnClickListener)
        cropImageView.setOnClickListener(view -> selectImage());

        // Upload Details (OnClickListener)
        uploadButton.setOnClickListener(view -> uploadCropDetails());
    }

    // AutoCompleteTextView Setup


    // Image Selection Handling
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
    private void populateCrops() {
        // *** Fetch crops from Firebase or a local data source ***
        // Example (replace with your data):
        cropsList.add("Onion");
        cropsList.add("Cabbage");
        cropsList.add("Potato");
        cropsList.add("Mushroom");
        cropsList.add("Pea");
        cropsList.add("Rice");
        cropsList.add("Wheat");
        cropsList.add("Sugarcane");
        cropsList.add("Tomato");

//        ArrayAdapter<String> cropsAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, cropsList);
//        cropSpinner.setAdapter(cropsAdapter);
        Spinner spinner = findViewById(R.id.cropSpinner);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, cropsList);
        spinner.setAdapter(adapter);
    }

    // Upload Crop Details
    private void uploadCropDetails() {
        // Get data from input fields
        String cropName = cropSpinner.getSelectedItem().toString();
        String priceString = priceEditText.getText().toString();
        String quantityString = quantityEditText.getText().toString();

        // Basic Input Validation
        if (cropName.isEmpty() || priceString.isEmpty() || quantityString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert to appropriate data types
        double price = Double.parseDouble(priceString);
        int quantity = Integer.parseInt(quantityString);

        // Start the upload process
        if (imageUri != null) {
            uploadImageToStorage();
        } else {
            Toast.makeText(this, "Select image for crop", Toast.LENGTH_SHORT).show();
        }
    }
    // Inside your UploadCrops class
    private void uploadImageToStorage() {
        if (imageUri == null) {  // Check if an image was selected
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique filename with a .jpg extension
        String imageName = UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        // Start the upload task
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Success Listener: Get Download URL for uploaded image
        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveCropDataToDatabase(imageUrl);
                }))

                // Failure Listener: Handle upload errors
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
// Inside your UploadCrops class

    private void saveCropDataToDatabase(String imageUrl) {
        // Get data from input fields
        String cropName = cropSpinner.getSelectedItem().toString();
        double price = Double.parseDouble(priceEditText.getText().toString());
        int quantity = Integer.parseInt(quantityEditText.getText().toString());

        // Create a Crop object
        Crop crop = new Crop(cropName, price, quantity, imageUrl);

        // Generate a unique push ID for the crop entry
        String cropId = cropsRef.push().getKey();

        // Save the Crop object to Firebase Realtime Database
        cropsRef.child(cropId).setValue(crop)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    // Optionally: Clear the form fields after a successful upload
                    clearFormFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

// (You already have the Crop class from previous examples or can add it here)

    //  Optional helper method to clear the form
    private void clearFormFields() {
       cropSpinner.setSelection(0);
        priceEditText.setText("");
        quantityEditText.setText("");
        cropImageView.setImageResource(R.drawable.choose); // Reset image
        imageUri = null;
    }




    // ... (Rest of code)
}
