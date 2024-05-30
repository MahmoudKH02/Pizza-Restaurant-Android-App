package com.example.pizzarestaurantproject;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private static List<String> pizzaTypes = new ArrayList<>();

    public static List<String> getPizzaTypes() {
        return pizzaTypes;
    }

    public static void setPizzaTypes(List<String> types) {
        Pizza.pizzaTypes = types;
    }

} // end class
