package com.example.pizzarestaurantproject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Serializable {
    private String pizzaType;
    private double price;
    private Date orderDate;
    private int quantity;
    private String size;
    private String userEmail;
    private int imageResourceId; // New attribute for image resource ID

    public Order(String pizzaType, double price, int quantity, String size, String userEmail, int imageResourceId) {
        this.pizzaType = pizzaType;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.orderDate = new Date();
        this.userEmail = userEmail;
        this.imageResourceId = imageResourceId;
    }

    public Order(String userEmail, String pizzaType, double price, String timeOfOrder, int quantity, String size, int imageResourceId) {
        this.userEmail = userEmail;
        this.pizzaType = pizzaType;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.imageResourceId = imageResourceId;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.orderDate = dateFormat.parse(timeOfOrder);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public String getPizzaType() {
        return pizzaType;
    }

    public void setPizzaType(String pizzaType) {
        this.pizzaType = pizzaType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    // Method to get formatted date and time
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(orderDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "pizzaType='" + pizzaType + '\'' +
                ", price=" + price +
                ", orderDate=" + orderDate +
                ", quantity=" + quantity +
                ", size='" + size + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", imageResourceId=" + imageResourceId +
                '}';
    }
}
