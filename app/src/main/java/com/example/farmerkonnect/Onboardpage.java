package com.example.farmerkonnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Onboardpage extends AppCompatActivity {
    Button button;

    FirebaseAuth Muth;

    FirebaseDatabase db;


    EditText Farmername, Farmerage, Farmeraddress, Farmeremail, Farmsize, FarmLocation, Farmcrop, Farmcultivation, Farmworker, Farmlivestocks, Farmchannel, Farmergoal, Farmertech, Farmerinterest, FarmerChallenges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboardpage);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel dt = new DataModel(Farmername.getText().toString().trim(), Farmerage.getText().toString().trim(), Farmeraddress.getText().toString().trim(), Farmeremail.getText().toString().trim(), Farmsize.getText().toString().trim(), FarmLocation.getText().toString().trim(), Farmcrop.getText().toString().trim(), Farmcultivation.getText().toString().trim(), Farmworker.getText().toString().trim(), Farmlivestocks.getText().toString().trim(), Farmchannel.getText().toString().trim(), Farmergoal.getText().toString().trim(), Farmertech.getText().toString().trim(), Farmerinterest.getText().toString().trim(), FarmerChallenges.getText().toString().trim()
                );
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
}