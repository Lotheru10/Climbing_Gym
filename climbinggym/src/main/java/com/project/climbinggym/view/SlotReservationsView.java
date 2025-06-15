package com.project.climbinggym.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class SlotReservationsView {

    private LocalDate date;

    @JsonProperty("morning_reservations")
    private List<ReservationSummary> morningReservations;
    @JsonProperty("noon_reservations")
    private List<ReservationSummary> noonReservations;
    @JsonProperty("evening_reservations")
    private List<ReservationSummary> eveningReservations;
    @JsonProperty("morning_stats")
    private SlotStats morningStats;
    @JsonProperty("noon_stats")
    private SlotStats noonStats;
    @JsonProperty("evening_stats")
    private SlotStats eveningStats;

    public SlotReservationsView() {}

    public SlotReservationsView(LocalDate date) {
        this.date = date;
    }

    // GETTERS AND SETTERS
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ReservationSummary> getMorningReservations() {
        return morningReservations;
    }
    public void setMorningReservations(List<ReservationSummary> morningReservations) {
        this.morningReservations = morningReservations;
    }

    public List<ReservationSummary> getNoonReservations() {
        return noonReservations;
    }
    public void setNoonReservations(List<ReservationSummary> noonReservations) {
        this.noonReservations = noonReservations;
    }

    public List<ReservationSummary> getEveningReservations() {
        return eveningReservations;
    }
    public void setEveningReservations(List<ReservationSummary> eveningReservations) {
        this.eveningReservations = eveningReservations;
    }

    public SlotStats getMorningStats() {
        return morningStats;
    }
    public void setMorningStats(SlotStats morningStats) {
        this.morningStats = morningStats;
    }

    public SlotStats getNoonStats() {
        return noonStats;
    }
    public void setNoonStats(SlotStats noonStats) {
        this.noonStats = noonStats;
    }

    public SlotStats getEveningStats() {
        return eveningStats;
    }
    public void setEveningStats(SlotStats eveningStats) {
        this.eveningStats = eveningStats;
    }

    // Inner classes
    public static class ReservationSummary {
        @JsonProperty("reservation_id")
        private String reservationId;
        @JsonProperty("user_name")
        private String userName;
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("people_amount")
        private int peopleAmount;

        private char status;

        public ReservationSummary() {}

        public ReservationSummary(String reservationId, String userName, String userId, int peopleAmount, char status) {
            this.reservationId = reservationId;
            this.userName = userName;
            this.userId = userId;
            this.peopleAmount = peopleAmount;
            this.status = status;
        }

        // GETTERS AND SETTERS
        public String getReservationId() {
            return reservationId;
        }
        public void setReservationId(String reservationId) {
            this.reservationId = reservationId;
        }

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getPeopleAmount() {
            return peopleAmount;
        }
        public void setPeopleAmount(int peopleAmount) {
            this.peopleAmount = peopleAmount;
        }

        public char getStatus() {
            return status;
        }
        public void setStatus(char status) {
            this.status = status;
        }
    }

    public static class SlotStats {
        @JsonProperty("max_slots")
        private int maxSlots;
        @JsonProperty("reserved_slots")
        private int reservedSlots;
        @JsonProperty("available_slots")
        private int availableSlots;
        @JsonProperty("total_people")
        private int totalPeople;
        @JsonProperty("active_reservations_count")
        private int activeReservationsCount;

        public SlotStats() {}

        public SlotStats(int maxSlots, int reservedSlots, int totalPeople, int activeReservationsCount) {
            this.maxSlots = maxSlots;
            this.reservedSlots = reservedSlots;
            this.availableSlots = maxSlots - reservedSlots;
            this.totalPeople = totalPeople;
            this.activeReservationsCount = activeReservationsCount;
        }

        // GETTERS AND SETTERS
        public int getMaxSlots() {
            return maxSlots;
        }
        public void setMaxSlots(int maxSlots) {
            this.maxSlots = maxSlots;
        }

        public int getReservedSlots() {
            return reservedSlots;
        }
        public void setReservedSlots(int reservedSlots) {
            this.reservedSlots = reservedSlots;
        }

        public int getAvailableSlots() {
            return availableSlots;
        }
        public void setAvailableSlots(int availableSlots) {
            this.availableSlots = availableSlots;
        }

        public int getTotalPeople() {
            return totalPeople;
        }
        public void setTotalPeople(int totalPeople) {
            this.totalPeople = totalPeople;
        }

        public int getActiveReservationsCount() {
            return activeReservationsCount;
        }
        public void setActiveReservationsCount(int activeReservationsCount) {
            this.activeReservationsCount = activeReservationsCount;
        }
    }
}