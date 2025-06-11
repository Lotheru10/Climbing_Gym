package com.project.climbinggym.model.nested.user;

import org.springframework.data.mongodb.core.mapping.Field;

public class Entry {
    public Entry(){}

    @Field("entry_id")
    private String entryId;
    private String type;
    private Prices prices;
    @Field("time_limit")
    private int timeLimit;
    private int amount;


}
