package com.example.firstapp;

public class Item {
    private String description;
    private Boolean have;

    public Item(String description) {
        // Constructor
        this.description = description;
        this.have = false;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public Boolean getHave() {
        return have;
    }

    public void setHave() {
        this.have = !this.have;
    }
}
