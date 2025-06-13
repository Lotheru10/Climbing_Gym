package com.project.climbinggym.service;

import com.project.climbinggym.model.User;
import com.project.climbinggym.model.nested.user.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private UserService userService;

    @Autowired
    private TimeSlotService timeSlotService;


    // Add reservation to user and update time slot
    public boolean addReservationToUser(String userId, Reservation reservation) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            if(reservation.getStatus() == 0){
                reservation.setStatus('A');
            }
            LocalDate date = reservation.getDate();
            String dayTime = reservation.getDayTime();
            int peopleAmount = reservation.getPeopleAmount();
            if (!timeSlotService.checkSlotAvailability(date, dayTime, peopleAmount)) {
                throw new RuntimeException("Not enough slots available for reservation");
            }

            user.getReservations().add(reservation);
            boolean timeSlotUpdated = timeSlotService.updateReservationCount(date, dayTime, peopleAmount, true);
            if (!timeSlotUpdated) {
                throw new RuntimeException("Failed to update time slot");
            }

            userService.updateUser(userId, user);
            return true;

        } catch (Exception e) {
            // Transaction will automatically rollback due to @Transactional
            throw new RuntimeException("Failed to add reservation: " + e.getMessage(), e);
        }
    }


    // AIowa wersja, moja wyżej^ tą na razie zachowuję jakby okazała się że jest wygodniejsza
//    @Transactional
//    public boolean addReservationToUser(String userId, LocalDate date, String dayTime, int peopleAmount) {
//        try {
//            // 1. Check if user exists
//            User user = userService.getUserById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//
//            // 2. Check slot availability
//            if (!timeSlotService.checkSlotAvailability(date, dayTime, peopleAmount)) {
//                throw new RuntimeException("Not enough slots available for reservation");
//            }
//
//            // 3. Create reservation object
//            Reservation reservation = new Reservation();
//            reservation.setReservationId(generateReservationId());
//            reservation.setDate(date);
//            reservation.setDayTime(dayTime);
//            reservation.setPeopleAmount(peopleAmount);
//            reservation.setStatus('A'); // A = Active, C = Cancelled, P = Pending
//
//            // 4. Add reservation to user
//            user.getReservations().add(reservation);
//
//            // 5. Update time slot (this will create the slot if it doesn't exist)
//            boolean timeSlotUpdated = timeSlotService.updateReservationCount(date, dayTime, peopleAmount, true);
//            if (!timeSlotUpdated) {
//                throw new RuntimeException("Failed to update time slot");
//            }
//
//            // 6. Save user with new reservation
//            userService.updateUser(userId, user);
//
//            return true;
//
//        } catch (Exception e) {
//            // Transaction will automatically rollback due to @Transactional
//            throw new RuntimeException("Failed to add reservation: " + e.getMessage(), e);
//        }
//    }

    // Cancel reservation
    @Transactional
    public boolean cancelReservation(String userId, String reservationId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            Reservation reservation = user.getReservations().stream()
                    .filter(r -> r.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));

            reservation.setStatus('C'); // Cancelled

            boolean timeSlotUpdated = timeSlotService.updateReservationCount(
                    reservation.getDate(),
                    reservation.getDayTime(),
                    reservation.getPeopleAmount(),
                    false
            );

            if (!timeSlotUpdated) {
                throw new RuntimeException("Failed to update time slot");
            }

            userService.updateUser(userId, user);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel reservation: " + e.getMessage(), e);
        }
    }

    // Remove reservation completely (for admin purposes)
    @Transactional
    public boolean removeReservation(String userId, String reservationId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            Reservation reservation = user.getReservations().stream()
                    .filter(r -> r.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));

            // Only update time slot if reservation was active
            if (reservation.getStatus() == 'A') {
//                boolean timeSlotUpdated = timeSlotService.updateReservationCount(
//                        reservation.getDate(),
//                        reservation.getDayTime(),
//                        reservation.getPeopleAmount(),
//                        false
//                );

                boolean cancelled = cancelReservation(userId, reservationId);
                if (!cancelled) {
                    throw new RuntimeException("Failed to cancel the reservation");
                }
            }

            user.getReservations().removeIf(r -> r.getReservationId().equals(reservationId));
            userService.updateUser(userId, user);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to remove reservation: " + e.getMessage(), e);
        }
    }

    // Check availability for a specific date and time
    public boolean checkAvailability(LocalDate date, String dayTime, int peopleAmount) {
        return timeSlotService.checkSlotAvailability(date, dayTime, peopleAmount);
    }

    // Get available slots for a specific date and time
    public int getAvailableSlots(LocalDate date, String dayTime) {
        return timeSlotService.getTimeSlotByDate(date)
                .map(timeSlot -> {
                    switch (dayTime.toLowerCase()) {
                        case "morning":
                            return timeSlot.getDetails().getMorning().getMaxSlots() -
                                    timeSlot.getDetails().getMorning().getReservedSlots();
                        case "noon":
                            return timeSlot.getDetails().getNoon().getMaxSlots() -
                                    timeSlot.getDetails().getNoon().getReservedSlots();
                        case "evening":
                            return timeSlot.getDetails().getEvening().getMaxSlots() -
                                    timeSlot.getDetails().getEvening().getReservedSlots();
                        default:
                            return 0;
                    }
                })
                .orElse(30); // Default available slots if no time slot exists yet
    }

    // Generate unique reservation ID
    // DO PRZEMYŚLENIA CZY TRZYMAMY SIĘ "rN" (N-liczba)
    // CZY UPROSZCZAMY NA LOSOWE WARTOŚCI
    private String generateReservationId() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}