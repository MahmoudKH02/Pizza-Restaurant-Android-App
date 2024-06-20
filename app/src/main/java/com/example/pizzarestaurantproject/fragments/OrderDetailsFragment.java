package com.example.pizzarestaurantproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.Order;

import java.io.Serializable;

public class OrderDetailsFragment extends DialogFragment {

    private static final String ARG_ORDER = "order";

    private Order order;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public static OrderDetailsFragment newInstance(Order order) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable(ARG_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView = view.findViewById(R.id.image_view);
        TextView pizzaNameTextView = view.findViewById(R.id.pizza_name_text_view);
        TextView priceTextView = view.findViewById(R.id.price_text_view);
        TextView dateTextView = view.findViewById(R.id.date_text_view);
        TextView quantityTextView = view.findViewById(R.id.quantity_text_view);
        TextView sizeTextView = view.findViewById(R.id.size_text_view);
        TextView emailTextView = view.findViewById(R.id.email_text_view);
        if (order != null) {
            imageView.setImageResource(order.getImageResourceId());
            pizzaNameTextView.setText(order.getPizzaType());
            priceTextView.setText(String.format("$%.2f", order.getPrice()));
            dateTextView.setText(order.getFormattedDate());
            quantityTextView.setText(String.valueOf(order.getQuantity()));
            sizeTextView.setText(order.getSize());
            emailTextView.setText(order.getUserEmail());
        }
    }
}
