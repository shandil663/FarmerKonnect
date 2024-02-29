package com.example.farmerkonnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.farmerkonnect.databinding.ActivityHomeBinding;
import com.google.android.material.color.DynamicColors;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        binding.bottomview.setBackground(null);
        replaceFragment(new HomeFragment());
        binding.bottomview.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            }
            else if (itemId == R.id.user) {
                replaceFragment(new UserFargment());
            }else if (itemId == R.id.market) {
                replaceFragment(new SchemeFragment());
            }
            else if (itemId == R.id.chat) {
                replaceFragment(new inbox());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    }
