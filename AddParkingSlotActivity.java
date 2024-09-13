package com.example.ParkingReservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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

public class AddParkingSlotActivity extends AppCompatActivity {

    private EditText editTextSlotId, editTextLocation, editTextCostPerDay;
    private SwitchMaterial switchAvailability;

    private Button buttonAddSlot;
    private ParkingSlotManagement parkingSlotManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking_slot);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.add_parking_slot_title); // Set title
        }

        // Initialize UI components
        editTextSlotId = findViewById(R.id.editTextSlotId);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextCostPerDay = findViewById(R.id.editTextCostPerDay);
        switchAvailability = findViewById(R.id.switchAvailability);
        buttonAddSlot = findViewById(R.id.buttonAddSlot);

        // Initialize ParkingSlotManagement
        parkingSlotManagement = new ParkingSlotManagement();

        // Set up the button click listener
        buttonAddSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParkingSlot();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Go back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addParkingSlot() {
        // Get input values from the UI
        String slotId = editTextSlotId.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String costPerHourString = editTextCostPerDay.getText().toString().trim();
        boolean isAvailable = switchAvailability.isChecked();

        // Validate inputs
        if (slotId.isEmpty() || location.isEmpty() || costPerHourString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse cost per hour to double
        double costPerHour;
        try {
            costPerHour = Double.parseDouble(costPerHourString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number for the cost.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a ParkingSlot object
        ParkingSlot parkingSlot = new ParkingSlot(slotId, location, isAvailable, costPerHour);

        // Add the parking slot to the database
        parkingSlotManagement.addParkingSlot(parkingSlot)
                .addOnSuccessListener(aVoid -> {
                    // Notify the user of success
                    Toast.makeText(AddParkingSlotActivity.this, "Parking slot added successfully.", Toast.LENGTH_SHORT).show();

                    // Set the result and finish the activity
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close the activity and return the result
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddParkingSlotActivity.this, "Error adding parking slot.", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        editTextSlotId.setText("");
        editTextLocation.setText("");
        editTextCostPerDay.setText("");
        switchAvailability.setChecked(false);
    }
}