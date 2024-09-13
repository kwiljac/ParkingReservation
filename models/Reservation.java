package com.example.ParkingReservation.models;

    public  class Reservation {

        private String reservationId;
        private String slotId;
        private String userId;
        private String reservationDate;
        private Double reservationCost;



        public Reservation(String reservationId, String slotId, String userId, String reservationDate, Double reservationCost) {
            this.reservationId = reservationId;
            this.slotId = slotId;
            this.userId = userId;
            this.reservationDate = reservationDate;
            this.reservationCost = reservationCost;
        }

        public Reservation() {
            // Default constructor does not need to initialize fields
        }

        public Reservation(String slotId, String userId, String reservationDate, double reservationCost) {
        }


        // Getters and setters
    public String getReservationId() {
        return reservationId;
    }


        public String getSlotId() {
            return slotId;
        }

        public void setSlotId(String slotId) {
            this.slotId = slotId;
        }
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
        public String getReservationDate() {
            return reservationDate;
        }
        public void setReservationDate(String reservationDate) {
            this.reservationDate = reservationDate;
        }

        public Double getReservationCost() {
            return reservationCost;
        }

        public void setReservationCost(Double reservationCost) {
            this.reservationCost = reservationCost;
        }
        @Override
        public String toString() {
            return "Reservation{" +
                    ", slotId='" + slotId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", reservationDate='" + reservationDate + '\'' +
                    ", reservationCost=" + reservationCost +
                    '}';
        }
    }
