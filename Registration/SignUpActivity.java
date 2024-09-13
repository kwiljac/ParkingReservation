package com.example.ParkingReservation.Registration;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParkingReservation.R;
import com.example.ParkingReservation.management.ParkingSlotAdapter;
import com.example.ParkingReservation.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.ParkingReservation.models.Users;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView nameStar;
    private EditText emailEditText, passwordEditText,roleEditText,usernameEditText,ageEditText,phoneEditText,addressEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        nameStar = findViewById(R.id.star_name);
        roleEditText = findViewById(R.id.roleEditText);
        ageEditText = findViewById(R.id.ageEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        Button signUpButton = findViewById(R.id.signUpButton);
        TextView registerTextView = findViewById(R.id.registerLink);

        signUpButton.setOnClickListener(v -> RegisterUser());

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void RegisterUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String ageStr = ageEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        boolean isValid = true;

        // Validate email
//        if (email.isEmpty()) {
//            emailStar.setVisibility(View.VISIBLE);
//            isValid = false;
//        } else {
//            emailStar.setVisibility(View.GONE);
//        }
//
//        // Validate password
//        if (password.isEmpty()) {
//            passwordStar.setVisibility(View.VISIBLE);
//            isValid = false;
//        } else {
//            passwordStar.setVisibility(View.GONE);
//        }
//
//        // Validate role
//        if (role.isEmpty()) {
//            roleStar.setVisibility(View.VISIBLE);
//            isValid = false;
//        } else {
//            roleStar.setVisibility(View.GONE);
//        }

        // Validate username
        if (username.isEmpty()) {
            Log.d(TAG, "Username is empty, setting star visibility to VISIBLE.");
            nameStar.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            Log.d(TAG, "Username is provided, hiding star.");
            nameStar.setVisibility(View.GONE);
        }

//        // Validate age
//        if (ageStr.isEmpty()) {
//            ageStar.setVisibility(View.VISIBLE);
//            isValid = false;
//        } else {
//            ageStar.setVisibility(View.GONE);
//        }
//
//        // Validate phone
//        if (phone.isEmpty()) {
//            phoneStar.setVisibility(View.VISIBLE);
//            isValid = false;
//        } else {
//            phoneStar.setVisibility(View.GONE);
//        }

        // Proceed with registration if all fields are valid
        if (isValid) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                saveUserToDatabase(user.getUid(), username, role, ageStr, phone, address);
                            }
                            Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(SignUpActivity.this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserToDatabase(String userId, String username, String role, String age, String phone, String address) {
       Users newUser = new Users(username, role, "SECURE_PASSWORD", age, phone, address);  // Use your custom User class
        mDatabase.child("Users").child(userId).setValue(newUser)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "User data saved successfully."))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to save user data.", e));
    }

}

