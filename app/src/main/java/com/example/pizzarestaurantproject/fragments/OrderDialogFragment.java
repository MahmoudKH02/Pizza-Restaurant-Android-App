package com.example.pizzarestaurantproject.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pizzarestaurantproject.LoginSignupActivity;
import com.example.pizzarestaurantproject.MainActivity;
import com.example.pizzarestaurantproject.Order;
import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;
import com.example.pizzarestaurantproject.models.Pizzas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDialogFragment extends DialogFragment {

    private static final String ARG_PIZZA = "pizza";
    private TextView pizzaName;
    private Spinner sizeSpinner;
    private EditText quantityInput;
    private TextView totalPrice;
    private Button submitButton;
    private Pizzas pizza;
    private Map<String, Double> sizePriceMap = new HashMap<>();
    private SharedPrefManager sharedPrefManager;

    public OrderDialogFragment() {
        // Required empty public constructor
    }

    public static OrderDialogFragment newInstance(Pizzas pizza) {
        OrderDialogFragment fragment = new OrderDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PIZZA, pizza);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizza = (Pizzas) getArguments().getSerializable(ARG_PIZZA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pizzaName = view.findViewById(R.id.pizzaName);
        sizeSpinner = view.findViewById(R.id.sizeSpinner);
        quantityInput = view.findViewById(R.id.quantityInput);
        totalPrice = view.findViewById(R.id.totalPrice);
        submitButton = view.findViewById(R.id.submitButton);


        // Initialize the size and price mapping
        sizePriceMap.put("Small", pizza.getPrice());
        sizePriceMap.put("Medium", pizza.getPrice()+5);
        sizePriceMap.put("Large",pizza.getPrice()+10) ;
        sizePriceMap.put("Extra Large", pizza.getPrice()+15);
        // Set up the spinner with available sizes
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.size_array2, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sizeSpinner.setAdapter(sizeAdapter);

        final double[] total = {0};
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               total[0] = calculateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 total[0]=calculateTotalPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        if (pizza != null) {
            pizzaName.setText(pizza.getName());
        }

        submitButton.setOnClickListener(v -> {
            String size = sizeSpinner.getSelectedItem().toString();
            String quantityStr = quantityInput.getText().toString();
            if (size.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double price = total[0]; // Assuming price is per pizza
            // Format the price to two decimal places
            String formattedPrice = String.format("%.2f", price);
            Toast.makeText(getContext(), "Order submitted: " + quantity + " "  + pizza.getName() +" of size " + size + " " + " for $" + formattedPrice, Toast.LENGTH_LONG).show();
            dismiss();
            sharedPrefManager =SharedPrefManager.getInstance(requireContext());
            String userEmail =sharedPrefManager.readString("email","No Val") ;

            try (DataBaseHelper dbHelper = new DataBaseHelper(
                    requireContext(),
                    "PIZZA_RESTAURANT",
                    null, 1
            )){
               // Cursor user = dbHelper.getUser(email.getText().toString());
                String pizzaType = pizza.getName(); // Assuming pizza.getName() returns the type of pizza
               // String userEmail =User. ; // Add logic to get the user's email

                Order order = new Order(pizzaType, price, quantity, size, userEmail, pizza.getImageResourceId());
                dbHelper.insertOrder(order);

                List<Order> allOrders = dbHelper.getOrders(userEmail);

                // Check if there are any orders
                if (!allOrders.isEmpty()) {
                    // Iterate over each order and display or process it
                    for (Order order1 : allOrders) {
                        // For example, you can log the order details
                        Log.d("Order", order1.toString());
                    }
                } else {
                    // Handle the case where there are no orders in the database
                    Log.d("Order", "No orders found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private double calculateTotalPrice() {
        String selectedSize = sizeSpinner.getSelectedItem().toString();
        String quantityText = quantityInput.getText().toString();
        double total = 0;

        if (sizePriceMap.containsKey(selectedSize) && !quantityText.isEmpty()) {
            double pricePerUnit = sizePriceMap.get(selectedSize);
            int quantity = Integer.parseInt(quantityText);

            // Custom price calculation logic
             total = pricePerUnit * quantity; // Simple example, replace with your own equation if needed

            totalPrice.setText("Total Price: $" + String.format("%.2f", total));
        } else {
            totalPrice.setText("Total Price: $0.00");
        }
        return total;
    }

}
