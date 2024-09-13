package com.example.ParkingReservation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ParkingReservation.management.ParkingSlotManagement;
import com.example.ParkingReservation.models.ParkingSlot;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class EditParkingSlotActivity extends AppCompatActivity {
    private static final String TAG = "EditParkingSlotActivity";

    private EditText editTextSlotNumber, editTextCostPerDay;
    private SwitchMaterial switchAvailability;
    private Button buttonSave;
    private ParkingSlotManagement parkingSlotManagement;
    private ParkingSlot parkingSlot;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parking_slot);

        // Initialize input fields and button
        editTextSlotNumber = findViewById(R.id.editTextSlotNumber);
        editTextCostPerDay = findViewById(R.id.editTextCostPerDay);
        switchAvailability = findViewById(R.id.switchAvailability);
        buttonSave = findViewById(R.id.buttonSave);

        parkingSlotManagement = new ParkingSlotManagement();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve the ParkingSlot object from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("parkingSlot")) {
            parkingSlot = (ParkingSlot) intent.getSerializableExtra("parkingSlot");
            if (parkingSlot != null) {
                Log.d(TAG, "ParkingSlot retrieved: " + parkingSlot.getSlotId() + ", " + parkingSlot.getLocation());
                editTextSlotNumber.setText(parkingSlot.getLocation());
                switchAvailability.setChecked(parkingSlot.isAvailable());
                editTextCostPerDay.setText(String.valueOf(parkingSlot.getCostPerDay()));
            } else {
                Log.e(TAG, "Received ParkingSlot is null");
            }
        } else {
            Log.e(TAG, "No ParkingSlot found in Intent");
        }

        // Set save button click listener
        buttonSave.setOnClickListener(v -> {
            if (parkingSlot != null) {
                String newLocation = editTextSlotNumber.getText().toString();
                boolean newAvailability = switchAvailability.isChecked();
                String newCostStr = editTextCostPerDay.getText().toString();
                double newCost = newCostStr.isEmpty() ? parkingSlot.getCostPerDay() : Double.parseDouble(newCostStr);

                Log.d(TAG, "New Location: " + newLocation);
                Log.d(TAG, "New Availability: " + newAvailability);
                Log.d(TAG, "New Cost per Day: " + newCost);

                // Update the parking slot information
                parkingSlot.setLocation(newLocation);
                parkingSlot.setAvailable(newAvailability);
                parkingSlot.setCostPerDay(newCost);

                // Save the updated slot back to the database
                parkingSlotManagement.updateParkingSlot(parkingSlot)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Parking slot updated successfully.");
                            Toast.makeText(this, "Parking slot updated successfully.", Toast.LENGTH_SHORT).show();

                            // Set result and finish the activity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("updatedParkingSlot", parkingSlot);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error updating parking slot", e);
                            Toast.makeText(this, "Error updating parking slot.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Log.e(TAG, "ParkingSlot is null when trying to update.");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, "Back button pressed");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}