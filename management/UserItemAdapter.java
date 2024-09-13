package com.example.ParkingReservation.management;

import android.annotation.SuppressLint;

import com.example.ParkingReservation.models.ParkingSlot;
import com.example.ParkingReservation.models.Users;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkingReservation.R;


import java.util.List;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder> {

    private final List<Users> userList;
    private AdapterView.OnItemClickListener onItemClickListener;
    private final OnUserItemActionListener onUserItemActionListener;


    public interface OnUserItemActionListener {
        void onEditClick(Users user);
        void onDeleteClick(Users user);

    }

    public UserItemAdapter(List<Users> userItemList, OnUserItemActionListener onUserItemActionListener) {
        if (userItemList == null) {
            throw new IllegalArgumentException("List of user items cannot be null");
        }
        this.userList = userItemList;
        this.onUserItemActionListener = onUserItemActionListener;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        Users user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class   UserItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView usernameTextView,roleTextView,phoneTextView,addressTextView;
        private final Button editButton,deleteButton;

        public UserItemViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);


                editButton.setOnClickListener(v ->  {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onUserItemActionListener.onEditClick(userList.get(position));

                    }
                });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onUserItemActionListener != null) {
                    onUserItemActionListener.onDeleteClick(userList.get(position));
                }
            });
    }


        @SuppressLint("SetTextI18n")
        public void bind(Users user) {
            usernameTextView.setText("username: " + user.getUsername());
            roleTextView.setText("role: " + user.getRole());
            phoneTextView.setText("phone: " + user.getPhone());
            addressTextView.setText("address: " + user.getAddress());
        }
    }

}
