package com.example.ParkingReservation;

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

import com.example.ParkingReservation.management.UserItemManagement;
import com.example.ParkingReservation.models.Users;

public class EditUserActivity extends AppCompatActivity {
    private EditText usernameEditText, ageEditText, phoneEditText, addressEditText;
    private Button buttonSave;
    private UserItemManagement userItemManagement;
    private Users user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);

        usernameEditText = findViewById(R.id.usernameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        buttonSave = findViewById(R.id.buttonSave);

        userItemManagement = new UserItemManagement();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve the User object from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            user = (Users) intent.getSerializableExtra("user");
            if (user != null) {
                Log.d("EditUserActivity", "User retrieved: " + user.getUsername() + ", " + user.getAge() + ", " + user.getPhone() + ", " + user.getAddress());
                usernameEditText.setText(user.getUsername());
                ageEditText.setText(String.valueOf(user.getAge()));
                phoneEditText.setText(user.getPhone());
                addressEditText.setText(user.getAddress());

            } else {
                Log.e("EditUserActivity", "User not found in the Intent");
            }
        } else {
            Log.e("EditUserActivity", "User not found in the Intent");
        }

        buttonSave.setOnClickListener(v -> {
            if (user != null) {

                user.setUsername(usernameEditText.getText().toString());
                user.setAge(String.valueOf(Integer.parseInt(ageEditText.getText().toString())));
                user.setPhone(phoneEditText.getText().toString());
                user.setAddress(addressEditText.getText().toString());

                userItemManagement.updateUser(user)
                        .addOnSuccessListener(unused -> {
                            Log.d("EditUserActivity", "User updated successfully.");
                            Toast.makeText(this, "User updated successfully.", Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("Updateuser", (CharSequence) user);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("EditUserActivity", "Failed to update user: " + e.getMessage());
                            Toast.makeText(this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("EditUserActivity", "Failed to update user: " + e);
                            Toast.makeText(this, "Failed to update user: " + e, Toast.LENGTH_SHORT).show();
                        });
            } else {
                Log.e("EditUserActivity", "User is null when trying to update.");
            }
        });
    }
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                finish(); // Go back to the previous activity
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

