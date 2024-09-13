package com.example.ParkingReservation.management;


import java.util.ArrayList;
import java.util.List;
import  com.example.ParkingReservation.models.ParkingSlot;
import  com.example.ParkingReservation.models.Reservation;


public class ReservationService {

    private List<ParkingSlot> parkingSlots;
    private List<Reservation> reservations;

    // Constructor to initialize lists (simulating database)
    public ReservationService() {
        parkingSlots = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    // Method to reserve a parking slot
    public boolean reserveSlot(String slotId, String userId, String reservationDate) {
        ParkingSlot slot = findSlotById(slotId);

        if (slot != null && slot.isAvailable()) {
            // Mark the slot as reserved
            slot.setAvailable(false);

            // Create a new reservation record
            double reservationCost = slot.getCostPerDay(); // Calculate the cost, modify if needed
            Reservation reservation = new Reservation(slotId, userId, reservationDate, reservationCost);

            // Insert the reservation into the reservation list (simulate DB insert)
            reservations.add(reservation);

            System.out.println("Slot reserved successfully: " + reservation);
            return true;
        } else {
            System.out.println("Slot not available or does not exist.");
            return false;
        }
    }

    // Method to unreserve a parking slot
    public boolean unreserveSlot(String slotId, String userId) {
        ParkingSlot slot = findSlotById(slotId);
        Reservation reservation = findReservationBySlotAndUser(slotId, userId);

        if (slot != null && reservation != null) {
            // Mark the slot as available again
            slot.setAvailable(true);

            // Remove the reservation record (simulate DB delete)
            reservations.remove(reservation);

            System.out.println("Slot unreserved successfully: " + reservation);
            return true;
        } else {
            System.out.println("Slot is not reserved or reservation does not exist.");
            return false;
        }
    }

    // Helper method to find a parking slot by its ID
    private ParkingSlot findSlotById(String slotId) {
        for (ParkingSlot slot : parkingSlots) {
            if (slot.getSlotId().equals(slotId)) {
                return slot;
            }
        }
        return null;
    }

    // Helper method to find a reservation by slot ID and user ID
    private Reservation findReservationBySlotAndUser(String slotId, String userId) {
        for (Reservation reservation : reservations) {
            if (reservation.getSlotId().equals(slotId) && reservation.getUserId().equals(userId)) {
                return reservation;
            }
        }
        return null;
    }

    // Methods to add parking slots to simulate adding data to the database
    public void addParkingSlot(ParkingSlot slot) {
        parkingSlots.add(slot);
    }

    // Method to display all reservations (for testing purposes)
    public void displayReservations() {
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }
}
