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
import com.example.pizzarestaurantproject.models.Pizzas;

public class PizzaDetailFragment extends DialogFragment {

    private static final String ARG_PIZZA = "pizza";
    private Pizzas pizza;

    public PizzaDetailFragment() {
        // Required empty public constructor
    }

    public static PizzaDetailFragment newInstance(Pizzas pizza) {
        PizzaDetailFragment fragment = new PizzaDetailFragment();
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pizza_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView pizzaName = view.findViewById(R.id.pizzaName);
        ImageView pizzaImage = view.findViewById(R.id.pizzaImage);
        TextView pizzaDescription = view.findViewById(R.id.pizzaDescription);
        TextView pizzaPrice = view.findViewById(R.id.pizzaPrice);
        TextView pizzaSize = view.findViewById(R.id.pizzaSize);
        TextView pizzaCategory = view.findViewById(R.id.pizzaCategory);

        if (pizza != null) {
            pizzaName.setText(pizza.getName());
            pizzaImage.setImageResource(pizza.getImageResourceId());
            pizzaDescription.setText(pizza.getDescription());
            pizzaPrice.setText(String.valueOf(pizza.getPrice()));
            pizzaSize.setText(pizza.getSize());
            pizzaCategory.setText(pizza.getCategory());
        }
    }
}
