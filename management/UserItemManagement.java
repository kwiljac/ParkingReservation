package com.example.ParkingReservation.management;

import android.util.Log;
import com.example.ParkingReservation.models.Users;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserItemManagement {
    private static DatabaseReference mDatabase;

    // Constructor to initialize Firebase Realtime Database reference
    public UserItemManagement() {
        // Initialize the mDatabase reference correctly
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    }

    // Method to retrieve all parking slots from Realtime Database
    public void getAllUsers(final UserItemCallback callback) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Users> userList = new ArrayList<>();
                Log.d("Firebase", "Data snapshot received: " + snapshot.getChildrenCount() + " items.");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        Log.d("Firebase", "User fetched: " + user.getUsername());
                        userList.add(user);
                    }
                }
                // Callback to pass the retrieved list back to the caller
                callback.onSuccess(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving users: ", error.toException());
                // Callback to handle errors
                callback.onFailure(error.toException());
            }
        });
    }
                        public static Task<Void> updateUser(Users user) {
            return mDatabase.child(user.getUsername()).setValue(user)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "User updated successfully."))
                    .addOnFailureListener(e -> Log.e("Firebase", "Error updating user: ", e));
        }
        public Task<Void> deleteUser(Users user) {
            return mDatabase.child(user.getUsername()).removeValue()
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "User deleted successfully."))
                    .addOnFailureListener(e -> Log.e("Firebase", "Error deleting user: ", e));
        }
        public interface UserItemCallback {
            void onSuccess(List<Users> userList);
            void onFailure(Exception e);
        }
    }

