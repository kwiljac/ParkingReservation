package com.example.ParkingReservation;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;

public class AdminPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Initialize and set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Set the toolbar's navigation icon click listener
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Optionally finish the current activity
        });

        // Button for navigating to Parking Management Activity
        Button buttonParkingManagement = findViewById(R.id.buttonParkingManagement);
        buttonParkingManagement.setOnClickListener(v -> openParkingManagementActivity());

        // Button for navigating to User Management Activity
        Button buttonUserManagement = findViewById(R.id.buttonUserManagement);
        buttonUserManagement.setOnClickListener(v -> openUserManagementActivity());
    }

    // Method to open Parking Management Activity
    private void openParkingManagementActivity() {
        Intent intent = new Intent(AdminPanelActivity.this, ParkingManagementActivity.class);
        startActivity(intent);
    }

    // Method to open User Management Activity
    private void openUserManagementActivity() {
        Intent intent = new Intent(AdminPanelActivity.this, UserManagementActivity.class);
        startActivity(intent);
    }
}
