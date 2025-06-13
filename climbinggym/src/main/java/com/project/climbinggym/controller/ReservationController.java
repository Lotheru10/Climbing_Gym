package com.project.climbinggym.controller;

import com.project.climbinggym.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

        // Check availability
    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String dayTime,
            @RequestParam int peopleAmount) {

        boolean available = reservationService.checkAvailability(date, dayTime, peopleAmount);
        return new ResponseEntity<>(available, HttpStatus.OK);
    }

    // Get available slots
    @GetMapping("/available-slots")
    public ResponseEntity<Integer> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String dayTime) {

        int availableSlots = reservationService.getAvailableSlots(date, dayTime);
        return new ResponseEntity<>(availableSlots, HttpStatus.OK);
    }
}
