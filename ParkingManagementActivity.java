package com.example.ParkingReservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkingReservation.management.ParkingSlotAdapter;
import com.example.ParkingReservation.management.ParkingSlotManagement;
import com.example.ParkingReservation.models.ParkingSlot;

import java.util.List;

public class ParkingManagementActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT = 100;

    private RecyclerView recyclerViewParkingSlots;
    private ParkingSlotAdapter parkingSlotAdapter;
    private ParkingSlotManagement parkingSlotManagement;

    private final ActivityResultLauncher<Intent> addActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Refresh the list when a new parking slot is added successfully
                    fetchParkingSlots();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_management);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AdminPanelActivity
                Intent intent = new Intent(ParkingManagementActivity.this, AdminPanelActivity.class);
                startActivity(intent);
                finish(); // Optional: finish current activity if you don't want to return to it
            }
        });

        Button buttonAddParkingSlot = findViewById(R.id.buttonAddParkingSlot);
        buttonAddParkingSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddParkingActivity
                Intent intent = new Intent(ParkingManagementActivity.this, AddParkingSlotActivity.class);
                addActivityResultLauncher.launch(intent);
            }
        });

        recyclerViewParkingSlots = findViewById(R.id.recyclerViewParkingSlots);
        recyclerViewParkingSlots.setLayoutManager(new LinearLayoutManager(this));

        parkingSlotManagement = new ParkingSlotManagement();
        fetchParkingSlots();
    }

    private void fetchParkingSlots() {
        Log.d("ParkingManagement", "Fetching parking slots...");
        parkingSlotManagement.getAllParkingSlots(new ParkingSlotManagement.ParkingSlotCallback() {
            @Override
            public void onSuccess(List<ParkingSlot> parkingSlots) {
                Log.d("ParkingManagement", "Parking slots fetched: " + parkingSlots.size());
                parkingSlotAdapter = new ParkingSlotAdapter(parkingSlots, new ParkingSlotAdapter.OnParkingSlotActionListener() {
                    @Override
                    public void onEditClick(ParkingSlot parkingSlot) {
                        // Create an Intent to start the EditParkingSlotActivity
                        Intent intent = new Intent(ParkingManagementActivity.this, EditParkingSlotActivity.class);
                        intent.putExtra("parkingSlot", parkingSlot); // Pass the selected parking slot
                        editActivityResultLauncher.launch(intent);
                    }

                    @Override
                    public void onDeleteClick(ParkingSlot parkingSlot) {
                        // Handle delete button click
                        deleteParkingSlot(parkingSlot);
                    }
                });
                recyclerViewParkingSlots.setAdapter(parkingSlotAdapter);
                Log.d("ParkingManagement", "Adapter set with parking slots.");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ParkingManagement", "Error fetching parking slots", e);
            }
        });
    }

    private void deleteParkingSlot(ParkingSlot parkingSlot) {
        parkingSlotManagement.deleteParkingSlot(parkingSlot.getSlotId())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ParkingManagementActivity.this, "Parking slot deleted.", Toast.LENGTH_SHORT).show();
                    fetchParkingSlots(); // Refresh the list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ParkingManagementActivity.this, "Error deleting parking slot.", Toast.LENGTH_SHORT).show();
                    Log.e("ParkingManagement", "Error deleting parking slot", e);
                });
    }

    private final ActivityResultLauncher<Intent> editActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("updatedParkingSlot")) {
                        ParkingSlot updatedSlot = (ParkingSlot) data.getSerializableExtra("updatedParkingSlot");
                        if (updatedSlot != null) {
                            // Refresh the parking slots list
                            fetchParkingSlots();
                        }
                    }
                }
            });
}