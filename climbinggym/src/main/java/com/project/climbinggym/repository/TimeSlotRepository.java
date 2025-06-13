package com.project.climbinggym.repository;

import com.project.climbinggym.model.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TimeSlotRepository extends MongoRepository<TimeSlot, String> {

    // Find time slot by date
    Optional<TimeSlot> findByDate(LocalDate date);

    // Check if time slot exists for a specific date
    boolean existsByDate(LocalDate date);

    // Delete time slot by date
    void deleteByDate(LocalDate date);
}
