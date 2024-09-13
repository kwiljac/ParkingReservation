package com.example.ParkingReservation.management;

import android.util.Log;

import com.example.ParkingReservation.models.ParkingSlot;
import com.example.ParkingReservation.models.Reservation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotManagement {
    private DatabaseReference mDatabase;
    private DatabaseReference mParkingSlotDatabase;
    private DatabaseReference mReservationDatabase;


    // Constructor to initialize Firebase Realtime Database reference
    public ParkingSlotManagement() {
        // Initialize the mDatabase reference correctly
        mDatabase = FirebaseDatabase.getInstance().getReference("parking_slots");
        mReservationDatabase = FirebaseDatabase.getInstance().getReference("reservations");
    }

    // Method to add a parking slot to Realtime Database
    public Task<Void> addParkingSlot(ParkingSlot parkingSlot) {
        // Set value in the Realtime Database
        return mDatabase.child(parkingSlot.getSlotId()).setValue(parkingSlot)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Parking slot added successfully."))
                .addOnFailureListener(e -> Log.e("Firebase", "Error adding parking slot: ", e));
    }



    // Method to retrieve all parking slots from Realtime Database
    public void getAllParkingSlots(final ParkingSlotCallback callback) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ParkingSlot> parkingSlotList = new ArrayList<>();
                Log.d("Firebase", "Data snapshot received: " + snapshot.getChildrenCount() + " items.");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ParkingSlot parkingSlot = dataSnapshot.getValue(ParkingSlot.class);
                    if (parkingSlot != null) {
                        Log.d("Firebase", "Parking slot fetched: " + parkingSlot.getSlotId());
                        parkingSlotList.add(parkingSlot);
                    }
                }
                // Callback to pass the retrieved list back to the caller
                callback.onSuccess(parkingSlotList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving parking slots: ", error.toException());
                // Callback to handle errors
                callback.onFailure(error.toException());
            }
        });
    }
    public Task<Void> updateParkingSlot(ParkingSlot parkingSlot) {
        Log.d("Firebase", "Updating ParkingSlot: " + parkingSlot.getSlotId() + ", Cost per Day: " + parkingSlot.getCostPerDay());
        return mDatabase.child(parkingSlot.getSlotId()).setValue(parkingSlot)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Parking slot updated successfully."))
                .addOnFailureListener(e -> Log.e("Firebase", "Error updating parking slot: ", e));
    }
    public Task<Void> toggleParkingSlotAvailability(String slotId, boolean isAvailable) {
        Log.d("Firebase", "Toggling availability for ParkingSlot: " + slotId + ", Available: " + isAvailable);
        return mParkingSlotDatabase.child(slotId).child("isAvailable").setValue(isAvailable)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Parking slot availability toggled successfully."))
                .addOnFailureListener(e -> Log.e("Firebase", "Error toggling parking slot availability: ", e));
    }
    public Task<Void> addReservation(Reservation reservation) {
        String reservationId = mReservationDatabase.push().getKey(); // Generate a unique key for the reservation
        if (reservationId == null) {
            Log.e("Firebase", "Failed to generate reservation ID.");
            return Tasks.forException(new Exception("Failed to generate reservation ID.")); // Use Tasks.forException()
        }
        return mReservationDatabase.child(reservationId).setValue(reservation)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Reservation added successfully."))
                .addOnFailureListener(e -> Log.e("Firebase", "Error adding reservation: ", e));
    }

    public Task<Void> deleteParkingSlot(String slotId) {
        return mDatabase.child(slotId).removeValue()
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Parking slot deleted successfully."))
                .addOnFailureListener(e -> Log.e("Firebase", "Error deleting parking slot: ", e));
    }

    public String generateReservationId() {
        // Generate a unique key using Firebase
        return mReservationDatabase.push().getKey();
    }

    // Callback interface for handling success and failure of data retrieval
    public interface ParkingSlotCallback {
        void onSuccess(List<ParkingSlot> parkingSlots);

        void onFailure(Exception e);
    }
}
