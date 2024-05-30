package com.example.pizzarestaurantproject.helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonParser {
    public static List<String> getObjectFromJson(String json) {
        List<String> pizzaTypes;

        try {
            JSONObject jsonObject = new JSONObject(json);
            pizzaTypes = new ArrayList<>();

            JSONArray array = jsonObject.getJSONArray("types");

            for (int i = 0; i < array.length(); i++)
                pizzaTypes.add(array.getString(i));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return pizzaTypes;
    }
} // end class
