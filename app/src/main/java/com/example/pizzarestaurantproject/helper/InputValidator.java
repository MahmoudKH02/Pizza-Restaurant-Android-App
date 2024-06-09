package com.example.pizzarestaurantproject.helper;

import java.util.regex.Pattern;

public class InputValidator {

    public static boolean validName(String name) {
        return name.length() >= 3;
    }

    public static boolean invalidPassword(String password) {
        String regexPattern = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(regexPattern);

        return !pattern.matcher(password).matches();
    }

    public static boolean invalidPassword(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }

    public static boolean invalidPhoneNumber(String number) {
        String regexPattern = "^05\\d{8}$";
        Pattern pattern = Pattern.compile(regexPattern);

        return !pattern.matcher(number).matches();
    }

} // end class
