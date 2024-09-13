package com.example.ParkingReservation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkingReservation.management.MailgunClient;
import com.example.ParkingReservation.management.ParkingSlotManagement;
import com.example.ParkingReservation.models.ParkingSlot;
import com.example.ParkingReservation.models.Reservation;
import com.example.ParkingReservation.models.Users;
import com.example.ParkingReservation.management.ReserveSlotAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button buttonGoToAdminPanel;
    private RecyclerView recyclerViewReservationSlots;
    private ReserveSlotAdapter reserveSlotAdapter;
    private ParkingSlotManagement parkingSlotManagement;
    private ImageButton profileButton;
    private String userRole; // Store user role here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewReservationSlots = findViewById(R.id.ParkingRecyclerView);
        recyclerViewReservationSlots.setLayoutManager(new LinearLayoutManager(this));

        parkingSlotManagement = new ParkingSlotManagement();
        profileButton = findViewById(R.id.profileButton);
        buttonGoToAdminPanel = findViewById(R.id.buttonGoToAdminPanel);

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        reserveSlotAdapter = new ReserveSlotAdapter(new ArrayList<>(), new ReserveSlotAdapter.OnParkingSlotActionListener() {
            @Override
            public void onReserveClick(ParkingSlot parkingSlot) {
                handleReservation(parkingSlot);
            }
        });

        recyclerViewReservationSlots.setAdapter(reserveSlotAdapter);

        Button buttonOpenDatePicker = findViewById(R.id.buttonOpenDatePicker);
        buttonOpenDatePicker.setOnClickListener(v -> showDatePickerDialog());

        buttonGoToAdminPanel.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminPanelActivity.class);
            startActivity(intent);
        });

        fetchUserRole(); // Fetch user role and update UI
    }

    private void fetchUserRole() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        String currentUserId = auth.getCurrentUser().getUid(); // Get UID of the currently logged-in user

        usersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        userRole = user.getRole();
                        Log.d("MainActivity", "User role fetched: " + userRole);
                        handleRoleBasedVisibility(); // Update UI based on role
                        fetchParkingSlots(); // Ensure to fetch parking slots after role is fetched
                    } else {
                        Log.d("MainActivity", "User not found");
                    }
                } else {
                    Log.d("MainActivity", "No data available for UID: " + currentUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Error fetching user role", databaseError.toException());
            }
        });
    }

    private void handleRoleBasedVisibility() {
        if ("ADMIN".equals(userRole)) {
            buttonGoToAdminPanel.setVisibility(View.VISIBLE);
        } else {
            buttonGoToAdminPanel.setVisibility(View.GONE);
        }
    }

    private void fetchParkingSlots() {
        Log.d("MainActivity", "Fetching parking slots...");
        parkingSlotManagement.getAllParkingSlots(new ParkingSlotManagement.ParkingSlotCallback() {
            @Override
            public void onSuccess(List<ParkingSlot> parkingSlots) {
                Log.d("MainActivity", "Fetched parking slots: " + parkingSlots.size());
                reserveSlotAdapter.updateData(parkingSlots);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("MainActivity", "Error fetching parking slots", e);
                Toast.makeText(MainActivity.this, "Failed to fetch parking slots", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleReservation(ParkingSlot parkingSlot) {
        boolean newStatus = !parkingSlot.isAvailable();
        parkingSlot.setAvailable(newStatus);

        parkingSlotManagement.updateParkingSlot(parkingSlot).addOnSuccessListener(aVoid -> {
            if (newStatus) {
                // If slot is reserved, add reservation
                addReservation(parkingSlot);
            } else {
                // Slot is unreserved
                Toast.makeText(MainActivity.this, "Slot ID: " + parkingSlot.getSlotId() + " is now unreserved.", Toast.LENGTH_SHORT).show();
                sendEmailNotification("Slot reserved", "The parking slot with ID: " + parkingSlot.getSlotId() + " has been reserved.");
                // Optionally, update the UI to reflect the change
                fetchParkingSlots(); // Refresh list
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(MainActivity.this, "Failed to update slot status", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Error updating slot: ", e);
        });
    }

    private void addReservation(ParkingSlot parkingSlot) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Use actual user ID
        String reservationId = parkingSlotManagement.generateReservationId(); // Generate reservation ID

        // Create a new reservation
        Reservation reservation = new Reservation(
                reservationId,
                parkingSlot.getSlotId(),
                userId,
                getCurrentDate(),
                parkingSlot.getCostPerDay()
        );

        parkingSlotManagement.addReservation(reservation)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Reservation added successfully.", Toast.LENGTH_SHORT).show();
                    sendEmailNotification("Slot Unreserved", "The parking slot with ID: " + parkingSlot.getSlotId() + " has been unreserved.");
                    // Optionally, update the UI to reflect the change
                    fetchParkingSlots(); // Refresh list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Failed to add reservation", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error adding reservation: ", e);
                });
    }
    private void sendEmailNotification(String subject, String message) {
        MailgunClient mailgunClient = new MailgunClient();
        mailgunClient.sendEmail("bXXXb@example.com", subject, message);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    Toast.makeText(MainActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                }, year, month, day);

        datePickerDialog.show();
    }
}
