package com.project.climbinggym.controller;


import com.project.climbinggym.model.TimeSlot;
import com.project.climbinggym.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/time-slots")
@CrossOrigin(origins = "*")
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    // Read
    @GetMapping
    public ResponseEntity<Iterable<TimeSlot>> getAllTimeSlots() {
        Iterable<TimeSlot> timeSlots = timeSlotService.getAllTimeSlots();
        return new ResponseEntity<>(timeSlots, HttpStatus.OK);
    }
}
