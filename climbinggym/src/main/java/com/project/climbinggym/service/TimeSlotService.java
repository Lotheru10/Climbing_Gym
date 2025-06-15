package com.project.climbinggym.service;

import com.project.climbinggym.model.TimeSlot;
import com.project.climbinggym.model.nested.timeslot.SlotInfo;
import com.project.climbinggym.model.nested.timeslot.TimeSlotDetails;
import com.project.climbinggym.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

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

    public TimeSlot createCustomTimeSlot(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    public TimeSlot updateTimeSlot(String id, TimeSlot timeSlot) {
        return timeSlotRepository.findById(id)
                .map(slot -> {
                    slot.setDate(timeSlot.getDate());
                    slot.setDetails(timeSlot.getDetails());
                    return timeSlotRepository.save(slot);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteTimeSlot(String id) {
        if (timeSlotRepository.existsById(id)) {
            timeSlotRepository.deleteById(id);
        } else {
            throw new RuntimeException("Time slot not found with id: " + id);
        }
    }

    public boolean updateReservationCount(LocalDate date, String dayTime, int peopleAmount, boolean isAddition) {
        Optional<TimeSlot> timeSlotOpt = timeSlotRepository.findByDate(date);

        if (timeSlotOpt.isEmpty()) {
            TimeSlot newTimeSlot = getOrCreateTimeSlot(date);
            return updateSlotReservation(newTimeSlot, dayTime, peopleAmount, true);
        }

        TimeSlot timeSlot = timeSlotOpt.get();
        return updateSlotReservation(timeSlot, dayTime, peopleAmount, isAddition);
    }

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
                return false;
        }

        int currentReserved = targetSlot.getReservedSlots();
        int newReservedCount;
        if (isAddition) {
            newReservedCount = currentReserved + peopleAmount;
            if (newReservedCount > targetSlot.getMaxSlots()) {
                return false;
            }
        } else {
            newReservedCount = Math.max(0, currentReserved - peopleAmount);
        }

        targetSlot.setReservedSlots(newReservedCount);
        timeSlotRepository.save(timeSlot);
        return true;
    }


    public boolean checkSlotAvailability(LocalDate date, String dayTime, int peopleAmount) {
        TimeSlot timeSlot = getOrCreateTimeSlot(date);
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
