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

    // GETTERS AND SETTERS
    public String getEntryId() {
        return entryId;
    }
    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Prices getPrices() {
        return prices;
    }
    public void setPrices(Prices prices) {
        this.prices = prices;
    }

    public int getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
