package com.example.ParkingReservation.models;

import java.io.Serializable;

public class Users implements Serializable {
    private  String username;
    private String role;
    private String password;
    private String age;
    private String phone;
    private String address;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(Users.class)
    }

    public Users(String username, String role, String password, String age, String phone, String address) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public  String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAge() { return age; }  // Changed to String
    public void setAge(String age) { this.age = age; }  // Changed to String

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

