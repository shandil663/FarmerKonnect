package com.example.farmerkonnect;

import java.util.Map;

// Commodity.java
public class Commodity {
    private String imageUrl;
    private Map<String, Map<String, Map<String, Integer>>> prices; // State -> District -> Market -> Price

    // Constructor, Getters, Setters

    public Commodity(String imageUrl, Map<String, Map<String, Map<String, Integer>>> prices) {
        this.imageUrl = imageUrl;
        this.prices = prices;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Map<String, Map<String, Integer>>> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Map<String, Map<String, Integer>>> prices) {
        this.prices = prices;
    }

    public Commodity() {
    }
}


