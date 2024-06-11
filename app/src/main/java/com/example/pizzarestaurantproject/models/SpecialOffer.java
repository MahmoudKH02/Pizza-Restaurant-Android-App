package com.example.pizzarestaurantproject.models;

public class SpecialOffer {
    private int offerId;
    private String title;
    private String description;
    private double price;
    private int imageUrl;
    private int quantity;
    private String size;
    private String category; // Add this field

    public SpecialOffer(int offerId, String title, String description, double price, int imageUrl, int quantity, String size, String category) {
        this.offerId = offerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.size = size;
        this.category = category; // Initialize the new field
    }

    // Getters and setters
    public int getOfferId() { return offerId; }
    public void setOfferId(int offerId) { this.offerId = offerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getImageUrl() { return imageUrl; }
    public void setImageUrl(int imageUrl) { this.imageUrl = imageUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
