package com.project.climbinggym.service;


import com.project.climbinggym.model.TimeSlot;
import com.project.climbinggym.model.nested.timeslot.SlotInfo;
import com.project.climbinggym.model.nested.timeslot.TimeSlotDetails;
import com.project.climbinggym.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

// TA KLASA JEST JESZCZE DO DOCZYSZCZENIA
@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public Iterable<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    public Optional<TimeSlot> getTimeSlotByDate(LocalDate date) {
        return timeSlotRepository.findByDate(date);
    }

    public TimeSlot getOrCreateTimeSlot(LocalDate date) {
        Optional<TimeSlot> existingSlot = timeSlotRepository.findByDate(date);
        if (existingSlot.isPresent()) {
            return existingSlot.get();
        }

        TimeSlot newTimeSlot = new TimeSlot();
        newTimeSlot.setDate(date);
        newTimeSlot.setDetails(new TimeSlotDetails());

        return timeSlotRepository.save(newTimeSlot);
    }

    public boolean updateReservationCount(LocalDate date, String dayTime, int peopleAmount, boolean isAddition) {
        Optional<TimeSlot> timeSlotOpt = timeSlotRepository.findByDate(date);

        if (timeSlotOpt.isEmpty()) {
            TimeSlot newTimeSlot = getOrCreateTimeSlot(date);
            return updateSlotReservation(newTimeSlot, dayTime, peopleAmount, true);
            // nigdy nie powinno się wydarzyć
            //return false; // Can't subtract from non-existent slot
        }

        TimeSlot timeSlot = timeSlotOpt.get();
        return updateSlotReservation(timeSlot, dayTime, peopleAmount, isAddition);
    }

    // Helper method to update specific slot reservation
    private boolean updateSlotReservation(TimeSlot timeSlot, String dayTime, int peopleAmount, boolean isAddition) {
        TimeSlotDetails details = timeSlot.getDetails();
        SlotInfo targetSlot = null;

        switch (dayTime.toLowerCase()) {
            case "morning":
                targetSlot = details.getMorning();
                break;
            case "noon":
                targetSlot = details.getNoon();
                break;
            case "evening":
                targetSlot = details.getEvening();
                break;
            default:
                return false; // Invalid day time
        }

//        if (targetSlot == null) {        //  nigdy nie powinno się zdarzyć, raczej do usunięcia ale
//            targetSlot = new SlotInfo(); //  zostawiam jakby coś się wykrzaczało
//            switch (dayTime.toLowerCase()) {
//                case "morning":
//                    details.setMorning(targetSlot);
//                    break;
//                case "noon":
//                    details.setNoon(targetSlot);
//                    break;
//                case "evening":
//                    details.setEvening(targetSlot);
//                    break;
//            }
//        }

        int currentReserved = targetSlot.getReservedSlots();
        int newReservedCount;
        if (isAddition) {
            newReservedCount = currentReserved + peopleAmount;
            if (newReservedCount > targetSlot.getMaxSlots()) {
                return false; // Not enough slots available
            }
        } else {
            newReservedCount = Math.max(0, currentReserved - peopleAmount);
        }

        targetSlot.setReservedSlots(newReservedCount);
        timeSlotRepository.save(timeSlot);
        return true;
    }


    public boolean checkSlotAvailability(LocalDate date, String dayTime, int peopleAmount) {
        Optional<TimeSlot> timeSlotOpt = timeSlotRepository.findByDate(date);
        if (timeSlotOpt.isEmpty()) {
            return peopleAmount <= 30; //default ilosc ludzi
        }

        TimeSlot timeSlot = timeSlotOpt.get();
        TimeSlotDetails details = timeSlot.getDetails();
        SlotInfo targetSlot = null;

        switch (dayTime.toLowerCase()) {
            case "morning":
                targetSlot = details.getMorning();
                break;
            case "noon":
                targetSlot = details.getNoon();
                break;
            case "evening":
                targetSlot = details.getEvening();
                break;
            default:
                return false;
        }

        int availableSlots = targetSlot.getMaxSlots() - targetSlot.getReservedSlots();
        return availableSlots >= peopleAmount;
    }

}
