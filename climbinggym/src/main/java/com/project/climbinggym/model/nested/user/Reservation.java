package com.project.climbinggym.model.nested.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class Reservation {
    public Reservation(){}

    @Field("reservation_id")
    private String reservationId;
    private LocalDate date;
    @JsonProperty("day_time")
    @Field("day_time")
    private String dayTime;
    @JsonProperty("people_amount")
    @Field("people_amount")
    private int peopleAmount;
    private char status;

    // GETTERS AND SETTERS
    public String getReservationId() {
        return reservationId;
    }
    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDayTime() {
        return dayTime;
    }
    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public int getPeopleAmount() {
        return peopleAmount;
    }
    public void setPeopleAmount(int peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    public char getStatus() {
        return status;
    }
    public void setStatus(char status) {
        this.status = status;
    }
}
