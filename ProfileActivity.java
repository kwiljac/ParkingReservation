package com.example.ParkingReservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParkingReservation.R;
import com.example.ParkingReservation.management.UserItemManagement;

import com.example.ParkingReservation.management.ParkingSlotAdapter;
import com.example.ParkingReservation.models.Users;

import androidx.appcompat.widget.Toolbar;

import com.example.ParkingReservation.Registration.SignInActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.ParkingReservation.models.Users;
import com.example.ParkingReservation.Registration.SignInActivity;

import android.view.View;
import android.widget.Button;


public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView profileImageView;
    private TextView usernameTextView, ageTextView, phoneTextView, addressTextView;
    private Button signOutButton; // Add sign out button reference

    // Firebase references
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        usernameTextView = findViewById(R.id.nameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        addressTextView = findViewById(R.id.addressTextView);
        signOutButton = findViewById(R.id.signOutButton); // Initialize sign out button

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Get the user ID of the logged-in user
            String userId = currentUser.getUid();
            Log.d(TAG, "User ID: " + userId);

            // Reference to the logged-in user's data in Firebase Realtime Database
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            // Fetch user data from the database
            fetchUserData();
        } else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "No user is logged in.");
        }

        // Initialize and set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the back navigation button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Set a click listener for the toolbar's navigation icon
        toolbar.setNavigationOnClickListener(v -> {
            // Handle back navigation
            finish(); // Ends the current activity and goes back to the previous one
        });

        // Set a click listener for the sign out button
        signOutButton.setOnClickListener(v -> signOutUser());
    }

    private void fetchUserData() {
        // Listener to read data from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Snapshot data: " + snapshot.getValue());
                // Check if snapshot exists
                if (snapshot.exists()) {
                    Users user = snapshot.getValue(Users.class);
                    if (user != null) {
                        // Set user data to TextViews
                        usernameTextView.setText("Username: " + user.getUsername());
                        ageTextView.setText("Age: " + user.getAge());
                        phoneTextView.setText("Phone: " + user.getPhone());
                        addressTextView.setText("Address: " + user.getAddress());
                        // Load profile image if needed
                        loadProfileImage(user.getAddress()); // or any other relevant field
                        Log.d(TAG, "User data fetched successfully.");
                    } else {
                        Toast.makeText(ProfileActivity.this, "User object is null.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "User object is null.");
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "No data found for the user.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Snapshot does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    private void loadProfileImage(String imageUrl) {
        // Log the image URL and set a default image or handle image loading logic if required
        Log.d(TAG, "Loading profile image from URL: " + imageUrl);
        // Here you can use an image loading library like Glide or Picasso if needed
        profileImageView.setImageResource(R.drawable.default_profile); // Placeholder image
    }

    // Method to sign out the user
    private void signOutUser() {
        mAuth.signOut();
        Toast.makeText(ProfileActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
        // Redirect to Sign In activity
        Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Close the current activity
    }
}
