package com.example.pizzarestaurantproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.models.Pizzas;

public class OrderDialogFragment extends DialogFragment {

    private static final String ARG_PIZZA = "pizza";
    private Pizzas pizza;

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

        TextView pizzaName = view.findViewById(R.id.pizzaName);
        EditText sizeInput = view.findViewById(R.id.sizeInput);
        EditText quantityInput = view.findViewById(R.id.quantityInput);
        Button submitButton = view.findViewById(R.id.submitButton);

        if (pizza != null) {
            pizzaName.setText(pizza.getName());
        }

        submitButton.setOnClickListener(v -> {
            String size = sizeInput.getText().toString();
            String quantityStr = quantityInput.getText().toString();
            if (size.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double price = pizza.getPrice() * quantity; // Assuming price is per pizza
            Toast.makeText(getContext(), "Order submitted: " + quantity + " " + size + " " + pizza.getName() + " for $" + price, Toast.LENGTH_LONG).show();
            dismiss();
        });
    }
}
