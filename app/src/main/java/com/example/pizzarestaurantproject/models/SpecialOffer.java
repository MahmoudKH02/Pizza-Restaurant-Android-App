package com.example.pizzarestaurantproject.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SpecialOffer {
    private int offerId;
    private String title;
    private String description;
    private double price;
    private int imageUrl;
    private int quantity;
    private String size;
    private String category; // Add this field
    private String offer_period; // Add this field



    public SpecialOffer(int offerId, String title, String description, double price, int imageUrl, int quantity, String size, String category,String offer_period) {
        this.offerId = offerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.size = size;
        this.category = category; // Initialize the new field
        this.offer_period= offer_period;

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

    public String getOffer_period() {
        return offer_period;
    }

    public void setOffer_period(String offer_period) {
        this.offer_period = offer_period;
    }
    public Date getOfferEndDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            return sdf.parse(offer_period);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
