package com.example.ParkingReservation.models;

import java.io.Serializable;

public class ParkingSlot implements Serializable {
    private String slotId;
    private String location;
    private boolean isAvailable;
    private double costPerDay;


   ParkingSlot() {};

    // Constructor
    public ParkingSlot(String slotId, String location, boolean isAvailable, double costPerDay) {
        this.slotId = slotId;
        this.location = location;
        this.isAvailable = isAvailable;
        this.costPerDay = costPerDay;
    }

    // Getters and Setters
    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getCostPerDay() {
        return costPerDay;
    }

    public void setCostPerDay(double costPerDay) {
        this.costPerDay = costPerDay;
    }


}
