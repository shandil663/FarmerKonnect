package com.example.farmerkonnect;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.farmerkonnect.ml.Detection;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Diseasesdetection extends AppCompatActivity {
    TextView result, demoTxt, classified, clickHere;

    LottieAnimationView lottieAnimationView;
    ImageView imageView, arrowImage;
    Button picture,gallery; // Add a button for accessing the gallery
    private static final int GALLERY_REQUEST_CODE = 100;
    int imageSize = 224;

    CardView viewcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseasesdetection);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        gallery = findViewById(R.id.gallery);
        picture = findViewById(R.id.button);
        lottieAnimationView = findViewById(R.id.animationViewdetect);
        clickHere = findViewById(R.id.click_here);
        classified = findViewById(R.id.classified);
        viewcard = findViewById(R.id.viewcard);
        clickHere.setVisibility(View.GONE);
        classified.setVisibility(View.GONE);
        result.setVisibility(View.GONE);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureFromCamera();
            }
        });

     //Assuming you add a button with id 'button_gallery' in your layout
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    selectFromGallery();
                }
            }
        });

    }


    private void captureFromCamera() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void selectFromGallery() {
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , GALLERY_REQUEST_CODE);
        } else {
            requestPermissions(new String[]{ Manifest.permission.READ_MEDIA_IMAGES}, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            int dimension = Math.min(image.getWidth(), image.getHeight());
//            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
//            lottieAnimationView.setVisibility(View.GONE);
//            viewcard.setVisibility(View.VISIBLE);
//            imageView.setImageBitmap(image);
//            clickHere.setVisibility(View.VISIBLE);
//            classified.setVisibility(View.VISIBLE);
//            result.setVisibility(View.VISIBLE);
//
//            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//
//            classifyImage(image);
//
//        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) { // Camera
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                lottieAnimationView.setVisibility(View.GONE);
                viewcard.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(image);
                clickHere.setVisibility(View.VISIBLE);
                classified.setVisibility(View.VISIBLE);
                result.setVisibility(View.VISIBLE);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                classifyImage(image);
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    // ... Rest of image processing logic remains the same ...
                    int dimension = Math.min(image.getWidth(), image.getHeight());
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                    lottieAnimationView.setVisibility(View.GONE);
                    viewcard.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(image);
                    clickHere.setVisibility(View.VISIBLE);
                    classified.setVisibility(View.VISIBLE);
                    result.setVisibility(View.VISIBLE);

                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                    classifyImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    private void classifyImage(Bitmap image) {


        try {
            Detection model = Detection.newInstance(getApplicationContext());

//create input for reference
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

//get 1D array of 224 * 224 pixels in image
            int[] intValue = new int[imageSize * imageSize];
            image.getPixels(intValue, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

// iterate over pixels and extract R, G, B values , add to bytebuffer
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValue[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));

                }
            }

            inputFeature0.loadBuffer(byteBuffer);

//run model interface and gets result
            Detection.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeatures0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeatures0.getFloatArray();

// find the index of the class with the biggest confidence
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidence.length; i++) {
                if (confidence[i] > maxConfidence) {
                    maxConfidence = confidence[i];
                    maxPos = i;

                }
            }
            String[] classes = {"Tomato Septoria leaf spot", "Healthy Tomato", "Tomato Mosaic Virus", "Tomato Yellow Leaf Curl Virus", "Tomato Target Spot", "Tomato Spider Mites", "Tomato Leaf Mold", "Tomato Late Blight", "Tomato Early Blight", "Tomato Bacterial Spot", "Healthy Potato", "Potato Late Blight", "Potato Early Blight", "Potato Bell Healthy", "Potato Bacterial Spots"};
            result.setText(classes[maxPos]);
            clickHere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
// to search the disease on internet
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/search?q=" + result.getText())));
                }


            });

            model.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

