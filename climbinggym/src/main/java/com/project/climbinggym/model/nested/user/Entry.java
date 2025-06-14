package com.project.climbinggym.model.nested.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.UUID;

public class Entry {
    public Entry(){
        entryId = generateEntryId();
    }

    @JsonProperty("entry_id")
    @Field("entry_id")
    private String entryId;
    private String type;
    private Prices prices;
    private LocalDate deadline; //zmienione
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

    public LocalDate getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }


    private String generateEntryId() {
        return "e-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
