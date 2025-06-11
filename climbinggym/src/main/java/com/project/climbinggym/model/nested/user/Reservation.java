package com.project.climbinggym.model.nested.user;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class Reservation {
    public Reservation(){}

    @Field("reservation_id")
    private String reservationId;
    private LocalDate date;
    @Field("day_time")
    private String dayTime;
    @Field("people_amount")
    private int peopleAmount;
    private char status;
}
