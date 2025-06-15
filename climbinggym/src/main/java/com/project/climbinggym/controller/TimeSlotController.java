package com.project.climbinggym.controller;

import com.project.climbinggym.model.TimeSlot;
import com.project.climbinggym.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Create
    public ResponseEntity<TimeSlot> createTimeSlot(@RequestBody TimeSlot timeSlot) {
        try {
            TimeSlot createdSlot = timeSlotService.createCustomTimeSlot(timeSlot);
            return new ResponseEntity<>(createdSlot, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Update
    public ResponseEntity<TimeSlot> updateTimeSlot(@RequestParam String id, @RequestBody TimeSlot timeSlot) {
        try {
            TimeSlot updatedSlot = timeSlotService.updateTimeSlot(id, timeSlot);
            return new ResponseEntity<>(updatedSlot, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete
    public ResponseEntity<TimeSlot> deleteTimeSlot(@RequestParam String id) {
        try {
            timeSlotService.deleteTimeSlot(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
