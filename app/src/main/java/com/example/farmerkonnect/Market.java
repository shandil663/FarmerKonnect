package com.example.farmerkonnect;

public class Market {
    private String name;

    public Market() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Market(String name, int price) {
        this.name = name;
        this.price = price;
    }

    private int price;

    // Constructor, Getters, Setters
}
