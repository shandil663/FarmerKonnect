package com.example.farmerkonnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity {

    Button getOTPButton, verifyOTPButton;
    CountryCodePicker cpp;
    EditText phoneNumberEditText, otpEditText;
    FirebaseAuth muth;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String phone;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth= FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance();


        getOTPButton = findViewById(R.id.getotp);
        verifyOTPButton = findViewById(R.id.verify);
        cpp = findViewById(R.id.ccp);
        phoneNumberEditText = findViewById(R.id.mobile);
        otpEditText = findViewById(R.id.otphere);
        muth = FirebaseAuth.getInstance();

        getOTPButton.setOnClickListener(view -> {
            phone = cpp.getSelectedCountryCodeWithPlus() + phoneNumberEditText.getText().toString();
            startVerification(phone);
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginPage.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                showOTPEntryUI();
            }
        };
    }

    private void startVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(muth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        muth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginPage.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                        DatabaseReference ref=db.getReference("FarmersInfo");
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if(currentUser != null){
                            ref.child(currentUser.getUid()).child("farmername").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        startActivity(new Intent(LoginPage.this,Home.class));
                                        finish();
                                    }

                                    else{
                                        startActivity(new Intent(LoginPage.this, Onboardpage.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                        }

                    } else {
                        Toast.makeText(LoginPage.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showOTPEntryUI() {
//        phoneNumberEditText.setVisibility(View.GONE);
//        cpp.setVisibility(View.GONE);
//        getOTPButton.setVisibility(View.GONE);

        otpEditText.setVisibility(View.VISIBLE);
        verifyOTPButton.setVisibility(View.VISIBLE);

        verifyOTPButton.setOnClickListener(view -> {
            String otpCode = otpEditText.getText().toString();
            verifyPhoneNumberWithCode(mVerificationId, otpCode);
        });
    }
}
