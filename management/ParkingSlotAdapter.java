package com.example.ParkingReservation.management;

// ParkingSlotAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;

import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkingReservation.R;
import com.example.ParkingReservation.models.ParkingSlot;

import java.util.List;

public class ParkingSlotAdapter extends RecyclerView.Adapter<ParkingSlotAdapter.ParkingSlotViewHolder> {

    private final List<ParkingSlot> parkingSlotList;

    private AdapterView.OnItemClickListener onItemClickListener;

        private final OnParkingSlotActionListener onParkingSlotActionListener;


    public interface OnParkingSlotActionListener {
        void onEditClick(ParkingSlot parkingSlot);
        void onDeleteClick(ParkingSlot parkingSlot);
    }

    public ParkingSlotAdapter(List<ParkingSlot> parkingSlotList, OnParkingSlotActionListener onParkingSlotActionListener) {
        if (parkingSlotList == null) {
            throw new IllegalArgumentException("List of parking slots cannot be null");
        }
        this.parkingSlotList = parkingSlotList;
        this.onParkingSlotActionListener = onParkingSlotActionListener;
    }


    @NonNull
    @Override
    public ParkingSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parking_slot, parent, false);
        return new ParkingSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSlotViewHolder holder, int position) {
        ParkingSlot parkingSlot = parkingSlotList.get(position);
        holder.bind(parkingSlot);
    }

    @Override
    public int getItemCount() {
        return parkingSlotList.size();
    }


    class ParkingSlotViewHolder extends RecyclerView.ViewHolder {
        private final TextView slotIdTextView;
        private final TextView statusTextView;
        private final TextView costPerDayTextView;
        private final TextView locationTextView;
        private final Button editButton;
        private final Button deleteButton;

        public ParkingSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            slotIdTextView = itemView.findViewById(R.id.slotIdTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            costPerDayTextView = itemView.findViewById(R.id.costPerDayTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onParkingSlotActionListener.onEditClick(parkingSlotList.get(position));
                }
            });



            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onParkingSlotActionListener.onDeleteClick(parkingSlotList.get(position));
                }
            });
        }

        public void bind(ParkingSlot parkingSlot) {
            slotIdTextView.setText("Slot ID: " + parkingSlot.getSlotId());
            String availabilityStatus = parkingSlot.isAvailable() ? "Available" : "Occupied";
            statusTextView.setText("Status: " + availabilityStatus);
            costPerDayTextView.setText("Cost per day: $" + parkingSlot.getCostPerDay());
            locationTextView.setText("Location: " + parkingSlot.getLocation());
        }
    }

}