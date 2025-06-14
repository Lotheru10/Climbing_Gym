package com.project.climbinggym.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.climbinggym.model.nested.user.Prices;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="entry_types")
public class EntryType {
    public EntryType(){}

    @Id
    private String id;
    @JsonProperty("entry_type")
    @Field("entry_type")
    private String entryType;
    private String name;
    private Prices prices;
    private int uses;
    @JsonProperty("day_limit")
    @Field("day_limit")
    private int dayLimit;

    // GETTERS AND SETTERS
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getEntryType() {
        return entryType;
    }
    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Prices getPrices() {
        return prices;
    }
    public void setPrices(Prices prices) {
        this.prices = prices;
    }

    public int getUses() {
        return uses;
    }
    public void setUses(int uses) {
        this.uses = uses;
    }

    public int getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(int dayLimit) {
        this.dayLimit = dayLimit;
    }
}
