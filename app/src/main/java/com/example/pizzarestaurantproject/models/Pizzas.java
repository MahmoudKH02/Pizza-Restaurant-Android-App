package com.example.pizzarestaurantproject.models;

import java.io.Serializable;

public class    Pizzas implements Serializable {
    private String name;
    private String description;
    private double price;
    private String size;
    private String category;
    private int imageResourceId; // Resource ID for the pizza image

    public Pizzas(String name, String description, double price, String size, String category, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
        this.category = category;
        this.imageResourceId = imageResourceId;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public String getCategory() {
        return category;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
