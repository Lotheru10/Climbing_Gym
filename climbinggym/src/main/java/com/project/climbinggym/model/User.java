package com.project.climbinggym.model;

import com.project.climbinggym.model.nested.user.Entry;
import com.project.climbinggym.model.nested.user.Reservation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "users")
public class User {
    public User(){}

    @Id
    private String id;
    private String firstname;
    private String lastname;
    @Field("register_date")
    private LocalDate registerDate;
    private List<Entry> entries;
    private List<Reservation> reservations;


    // GETTERS AND SETTERS
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }
    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public List<Entry> getEntries() {
        return entries;
    }
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
