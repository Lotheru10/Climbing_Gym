package com.project.climbinggym.service;

import com.project.climbinggym.model.User;
import com.project.climbinggym.model.nested.user.Reservation;
import com.project.climbinggym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (user.getRegisterDate() == null) {
            user.setRegisterDate(LocalDate.now());
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // Updates the entire user
    public User updateUser(String id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstname(updatedUser.getFirstname());
                    user.setLastname(updatedUser.getLastname());
                    if (updatedUser.getRegisterDate() != null) {
                        user.setRegisterDate(updatedUser.getRegisterDate());
                    }
                    if (updatedUser.getEntries() != null) {
                        user.setEntries(updatedUser.getEntries());
                    }
                    if (updatedUser.getReservations() != null) {
                        user.setReservations(updatedUser.getReservations());
                    }
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    public List<User> getUsersByLastname(String lastname) {
        return userRepository.findByLastname(lastname);
    }
}
