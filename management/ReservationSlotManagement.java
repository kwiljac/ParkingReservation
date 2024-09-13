package com.example.ParkingReservation.management;


import android.util.Log;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.example.ParkingReservation.models.Reservation;

public class ReservationSlotManagement {
    private DatabaseReference mDatabase;

    // Constructor to initialize Firebase Realtime Database reference
    public ReservationSlotManagement() {
        // Initialize the mDatabase reference correctly
        mDatabase = FirebaseDatabase.getInstance().getReference("reservation_slots");
    }

    // Method to add a reservation slot to Realtime Database
    public Task<Void> addReservationSlot(Reservation reservationSlot) {
        // Set value in the Realtime Database
        return mDatabase.child(reservationSlot.getSlotId()).setValue(reservationSlot)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Reservation slot added successfully."))
                .addOnFailureListener(e -> Log.e("Firebase", "Error adding reservation slot: ", e));
    }

    // Method to retrieve all reservation slots from Realtime Database
    public void getAllReservationSlots(final ReservationSlotCallback callback) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Reservation> reservationSlotList = new ArrayList<>();
                Log.d("Firebase", "Data snapshot received: " + snapshot.getChildrenCount() + " items.");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);
                    if (reservation != null) {
                        Log.d("Firebase", "Reservation slot fetched: " + reservation.getSlotId());
                        reservationSlotList.add(reservation);
                    }
                }
                // Notify the UI about data change
                callback.onSuccess(reservationSlotList);
                callback.onDataChanged(); // Notify about data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving reservation slots: ", error.toException());
                callback.onFailure(error.toException());
            }
        });
    }

    public Task<Void> updateReservationSlot(Reservation reservationSlot) {
        Log.d("Firebase", "Updating ReservationSlot: " + reservationSlot.getSlotId());
        return mDatabase.child(reservationSlot.getSlotId()).setValue(reservationSlot)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Reservation slot updated successfully.");
                    // Notify UI to refresh data
                    // This might involve calling the callback or notifying the adapter
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error updating reservation slot: ", e));
    }



    // Method to update a reservation slot in Realtime Database


    // Callback interface for handling success and failure of data retrieval
    public interface ReservationSlotCallback {
        void onSuccess(List<Reservation> reservationSlots);
        void onFailure(Exception e);
        void onDataChanged(); // New method to notify UI about data changes
    }

}
