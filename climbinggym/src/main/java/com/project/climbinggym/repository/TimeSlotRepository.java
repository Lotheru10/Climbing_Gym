package com.project.climbinggym.repository;

import com.project.climbinggym.model.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends MongoRepository<TimeSlot, String> {

    Optional<TimeSlot> findByDate(LocalDate date);

}
