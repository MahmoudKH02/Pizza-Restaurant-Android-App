package com.example.pizzarestaurantproject;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private String profilePicturePath;
    private boolean isAdmin;
    private final ArrayList<Order> orderHistory = new ArrayList<>();

    public User() {}

    public User(String email, String password, String firstName, String lastName,
                String phoneNumber, String gender, String profilePicturePath, boolean isAdmin)
    {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.profilePicturePath = profilePicturePath;
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
