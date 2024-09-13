package com.example.ParkingReservation.management;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkingReservation.R;
import com.example.ParkingReservation.models.ParkingSlot;

import java.util.List;

public class ReserveSlotAdapter extends RecyclerView.Adapter<ReserveSlotAdapter.ParkingSlotViewHolder> {
    private List<ParkingSlot> parkingSlotList;
    private final OnParkingSlotActionListener onParkingSlotActionListener;

    public interface OnParkingSlotActionListener {
        void onReserveClick(ParkingSlot parkingSlot);
    }



    public ReserveSlotAdapter(List<ParkingSlot> parkingSlotList, OnParkingSlotActionListener onParkingSlotActionListener) {
        if (parkingSlotList == null) {
            throw new IllegalArgumentException("List of parking slots cannot be null");
        }
        this.parkingSlotList = parkingSlotList;
        this.onParkingSlotActionListener = onParkingSlotActionListener;
    }

    @NonNull
    @Override
    public ParkingSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_slot, parent, false);
        return new ParkingSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSlotViewHolder holder, int position) {
        ParkingSlot parkingSlot = parkingSlotList.get(position);
        holder.bind(parkingSlot);
        int buttonColor = parkingSlot.isAvailable()
                ? ContextCompat.getColor(holder.itemView.getContext(), R.color.colorAvailable)
                : ContextCompat.getColor(holder.itemView.getContext(), R.color.colorReserved);
        Log.d("ReserveSlotAdapter", "Setting button color: " + Integer.toHexString(buttonColor));
        holder.reserveButton.setBackgroundColor(buttonColor);
        holder.reserveButton.setOnClickListener(v -> {
            if (onParkingSlotActionListener != null) {
                onParkingSlotActionListener.onReserveClick(parkingSlot);
                parkingSlot.setAvailable(!parkingSlot.isAvailable());
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parkingSlotList.size();
    }

    public void updateData(List<ParkingSlot> newParkingSlots) {
        if (newParkingSlots != null) {
            this.parkingSlotList.clear();
            this.parkingSlotList.addAll(newParkingSlots);
            notifyDataSetChanged(); // Notify the adapter that the data has changed
        }
    }

    class ParkingSlotViewHolder extends RecyclerView.ViewHolder {
        private final TextView slotIdTextView;
        private final TextView statusTextView;
        private final TextView costPerDayTextView;
        private final TextView locationTextView;
        private final Button reserveButton;

        public ParkingSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            slotIdTextView = itemView.findViewById(R.id.slotIdTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            costPerDayTextView = itemView.findViewById(R.id.costPerDayTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            reserveButton = itemView.findViewById(R.id.buttonReserve);

            reserveButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ParkingSlot parkingSlot = parkingSlotList.get(position);
                    onParkingSlotActionListener.onReserveClick(parkingSlot);
                    parkingSlot.setAvailable(!parkingSlot.isAvailable());
                    notifyItemChanged(position);
                }
            });
        }

        public void bind(ParkingSlot parkingSlot) {
            slotIdTextView.setText("Slot ID: " + parkingSlot.getSlotId());
            String availabilityStatus = parkingSlot.isAvailable() ? "Available" : "Occupied";
            statusTextView.setText("Status: " + availabilityStatus);
            costPerDayTextView.setText("Cost per day: $" + parkingSlot.getCostPerDay());
            locationTextView.setText("Location: " + parkingSlot.getLocation());
            reserveButton.setText(parkingSlot.isAvailable() ? "Reserve" : "Unreserve");
            Log.d("ReserveSlotAdapter", "Binding slot: " + parkingSlot.getSlotId() + " - " + availabilityStatus);
        }

    }
}
