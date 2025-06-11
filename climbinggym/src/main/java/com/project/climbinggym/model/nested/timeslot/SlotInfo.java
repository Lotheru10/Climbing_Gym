package com.project.climbinggym.model.nested.timeslot;

import org.springframework.data.mongodb.core.mapping.Field;

public class SlotInfo {
    public SlotInfo(){}

    @Field("max_slots")
    private int maxSlots;
    @Field("reserved_slots")
    private int reservedSlots;

}
