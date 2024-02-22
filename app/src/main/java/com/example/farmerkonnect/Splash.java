package com.example.farmerkonnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
LottieAnimationView lottieAnimationView;
    FirebaseAuth mAuth;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth= FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference("FarmersInfo");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        lottieAnimationView=findViewById(R.id.animationView1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(currentUser != null){
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child(currentUser.getUid()).exists()){
                                startActivity(new Intent(Splash.this,Home.class));
                                finish();
                            }

                            else{
                                startActivity(new Intent(Splash.this, Onboardpage.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    startActivity(new Intent(Splash.this,MainActivity.class));
                    finish();
                }


            }
        },0);
    }
}