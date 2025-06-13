package com.project.climbinggym.controller;

import com.project.climbinggym.service.ReservationService;
import com.project.climbinggym.service.UserService;
import com.project.climbinggym.model.User;
import com.project.climbinggym.model.nested.user.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // do ewentualnej podmianki/usunięcia
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    //może dodać jakąś walidację do Usera? jeszcze nwm gdzie to zrobić
    // i co konkretnie walidowac tak wlasciwie

    // Basic CRUD actions ============================================================
    // Create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Read
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // lol czemu ten mapping jest taki dlugi, moze go skroce albo dodam wiecej opcji wyszukiwania
    @GetMapping("/search/lastname/{lastname}")
    public ResponseEntity<List<User>> getUsersByLastname(@PathVariable String lastname) {
        List<User> users = userService.getUsersByLastname(lastname);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    // Reservation actions =======================================================

    @PutMapping("/{userId}/reservations")
    public ResponseEntity<String> addReservationToUser(@PathVariable String userId, @RequestBody Reservation reservation) {
        try {
            boolean added = reservationService.addReservationToUser(userId, reservation);

            if(added){
                return new ResponseEntity<>("Reservation added successfully",HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Reservation could not be added",HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}/reservations/{reservationId}/cancel")
    public ResponseEntity<String> cancelUsersReservation(@PathVariable String userId, @PathVariable String reservationId) {
        try {
            boolean cancelled = reservationService.cancelReservation(userId, reservationId);

            if (cancelled) {
                return new ResponseEntity<>("Reservation cancelled successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to cancel reservation", HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}/reservations/{reservationId}")
    public ResponseEntity<String> deleteUsersReservation(@PathVariable String userId, @PathVariable String reservationId) {
        try {
            boolean deleted = reservationService.removeReservation(userId, reservationId);

            if (deleted) {
                return new ResponseEntity<>("Reservation removed successfully", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Failed to remove reservation", HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
