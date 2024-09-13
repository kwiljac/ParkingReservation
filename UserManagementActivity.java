package com.example.ParkingReservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkingReservation.management.UserItemAdapter;
import com.example.ParkingReservation.management.UserItemManagement;
import com.example.ParkingReservation.models.ParkingSlot;
import com.example.ParkingReservation.models.Users;

import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT = 100;

    private RecyclerView recyclerViewUserItem;
    private UserItemAdapter userItemAdapter;
   private UserItemManagement userItemManagement;

    private final ActivityResultLauncher<Intent> addActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Refresh the list when a new user  added successfully
                    fetchUserItem();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AdminPanelActivity
                Intent intent = new Intent(UserManagementActivity.this, AdminPanelActivity.class);
                startActivity(intent);
                finish();

            }
        });
        recyclerViewUserItem = findViewById(R.id.recyclerViewUserItem);
        recyclerViewUserItem.setLayoutManager(new LinearLayoutManager(this));

        userItemManagement = new UserItemManagement();
         fetchUserItem();

    }

    private void fetchUserItem() {
        Log.d("ParkingManagement", "Fetching parking slots...");
        userItemManagement.getAllUsers(new UserItemManagement.UserItemCallback() {
            @Override
            public void onSuccess(List<Users> users)  {
                Log.d("UserManagement", "User items fetched: " + users.size());
                userItemAdapter = new UserItemAdapter(users, new UserItemAdapter.OnUserItemActionListener() {
                    @Override
                    public void onEditClick(Users user) {
                        // Create an Intent to start the EditUserItemActivity
                        Intent intent = new Intent(UserManagementActivity.this, EditUserActivity.class);
                        intent.putExtra("UserManagement userItem",  user); // Pass the selected user
                        editActivityResultLauncher.launch(intent);
                    }

                    @Override
                    public void onDeleteClick(Users user) {
                        // Handle delete button click
                        deleteUser(user);
                    }
                });
                recyclerViewUserItem.setAdapter(userItemAdapter);
                Log.d("UserManagement", "Adapter set with user items: " + userItemAdapter.getItemCount() + " items.");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("UserManagement", "Error fetching user items", e);
            }
        });
    }
    private void deleteUser(Users user) {
        userItemManagement.deleteUser(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UserManagementActivity.this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
                fetchUserItem();
                })
                .addOnFailureListener(e ->{
                    Toast.makeText(UserManagementActivity.this, "Error deleting user", Toast.LENGTH_SHORT).show();
                    Log.e("UserManagement", "Error deleting user", e);

                });
    }

    private final ActivityResultLauncher<Intent> editActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Refresh the list when a new user  added successfully
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("updatedUser")){
                        Users updatedUser = (Users) data.getSerializableExtra("updatedUser");
                       if(updatedUser !=null ){
                        //Refresh the user list
                        fetchUserItem();
                    }
                }
            }
            });
}