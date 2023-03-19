package com.example.firstapp;

public class Item {
    String description;
    Boolean have;

    public Item(String description) {
        this.description = description;
        this.have = false;
    }

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
