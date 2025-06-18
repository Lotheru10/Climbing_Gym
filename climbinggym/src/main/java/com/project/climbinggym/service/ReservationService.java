package com.project.climbinggym.service;

import com.project.climbinggym.model.User;
import com.project.climbinggym.model.nested.user.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ReservationService {

    @Autowired
    private UserService userService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private EntryService entryService;

    private final ConcurrentHashMap<String, ReentrantLock> timeSlotLocks = new ConcurrentHashMap<>();
    private ReentrantLock getTimeSlotLock(LocalDate date, String dayTime) {
        String lockKey = date.toString() + "-" + dayTime.toLowerCase();
        return timeSlotLocks.computeIfAbsent(lockKey, k -> new ReentrantLock());
    }


    @Transactional
    public boolean addReservationToUser(String userId, Reservation reservation) {
        ReentrantLock lock = getTimeSlotLock(reservation.getDate(), reservation.getDayTime());
        lock.lock();

        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            if(reservation.getStatus() == 0){
                reservation.setStatus('A');
            }
            reservation.setReservationId(generateReservationId());
            LocalDate date = reservation.getDate();
            if(date.isBefore(LocalDate.now())){
                throw new RuntimeException("Reservations cannot be made for the past");
            }
            String dayTime = reservation.getDayTime();
            int peopleAmount = reservation.getPeopleAmount();
            if (!timeSlotService.checkSlotAvailability(date, dayTime, peopleAmount)) {
                throw new RuntimeException("Not enough slots available for reservation");
            }

            int userEntries = entryService.getUsersEntryCountForDate(userId, date)
                    .values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            if(userEntries < reservation.getPeopleAmount()){
                throw new RuntimeException("Not enough entries available for people reserved");
            }

            user.getReservations().add(reservation);
            boolean timeSlotUpdated = timeSlotService.updateReservationCount(date, dayTime, peopleAmount, true);
            if (!timeSlotUpdated) {
                throw new RuntimeException("Failed to update time slot");
            }

            userService.updateUser(userId, user);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add reservation: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public boolean cancelReservation(String userId, String reservationId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            Reservation reservation = user.getReservations().stream()
                    .filter(r -> r.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));

            ReentrantLock lock = getTimeSlotLock(reservation.getDate(), reservation.getDayTime());
            lock.lock();


            if(reservation.getDate().isAfter(LocalDate.now().minusDays(1))) {
                throw new RuntimeException("Reservation can be cancelled 3 days prior at latest");
            }
            reservation.setStatus('C');

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
            lock.unlock();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel reservation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public boolean removeReservation(String userId, String reservationId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            Reservation reservation = user.getReservations().stream()
                    .filter(r -> r.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));

            if (reservation.getStatus() == 'A') {
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

    public boolean checkAvailability(LocalDate date, String dayTime, int peopleAmount) {
        return timeSlotService.checkSlotAvailability(date, dayTime, peopleAmount);
    }

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
                .orElse(30);
    }

    private String generateReservationId() {
        return "r-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}