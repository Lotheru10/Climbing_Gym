package com.project.climbinggym.model;

import com.project.climbinggym.model.nested.timeslot.TimeSlotDetails;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "time_slots")
public class TimeSlot {
    public TimeSlot(){}

    @Id
    private String id;
    private LocalDate date;
    private TimeSlotDetails details;


    // GETTERS AND SETTERS
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TimeSlotDetails getDetails() {
        return details;
    }
    public void setDetails(TimeSlotDetails details) {
        this.details = details;
    }
}
