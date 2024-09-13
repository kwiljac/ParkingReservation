package com.example.ParkingReservation.Registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParkingReservation.MainActivity;
import com.example.ParkingReservation.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
// Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button signInButton = findViewById(R.id.signInButton);
        TextView registerTextView = findViewById(R.id.registerLink);

        signInButton.setOnClickListener(v ->  SignInUser());

        registerTextView.setOnClickListener(v -> {
           Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
           startActivity(intent);
        });
    }

    private void SignInUser() {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("SignInActivity", "Attempting to start MainActivity");
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                Log.d("SignInActivity", "Started MainActivity intent");
            }
        });

    }
}