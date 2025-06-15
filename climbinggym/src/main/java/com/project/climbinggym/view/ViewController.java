package com.project.climbinggym.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/view")
@CrossOrigin(origins = "*")
public class ViewController {

    @Autowired
    private ViewService viewService;

    @GetMapping
    public ResponseEntity<SlotReservationsView> getTimeSlotReservations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            SlotReservationsView view = viewService.getTimeSlotReservationsView(date);
            return new ResponseEntity<>(view, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/today")
    public ResponseEntity<SlotReservationsView> getTodayTimeSlotReservations() {
        try {
            SlotReservationsView view = viewService.getTimeSlotReservationsView(LocalDate.now());
            return new ResponseEntity<>(view, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/month")
    public ResponseEntity<List<SlotReservationsView>> monthTimeSlotReservations() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate weekEnd = today.plusDays(30);
            List<SlotReservationsView> views = viewService.getTimeSlotReservationsViewRange(today, weekEnd);
            return new ResponseEntity<>(views, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}