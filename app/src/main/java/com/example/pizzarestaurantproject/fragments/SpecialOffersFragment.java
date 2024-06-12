package com.example.pizzarestaurantproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.adapters.SpecialOffersAdapter;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.models.Pizzas;
import com.example.pizzarestaurantproject.models.SpecialOffer;

import java.util.ArrayList;
import java.util.List;

public class SpecialOffersFragment extends Fragment {

    private List<SpecialOffer> specialOffers;
    private RecyclerView recyclerViewSpecialOffers;
    private SpecialOffersAdapter specialOffersAdapter;

    public SpecialOffersFragment() {
        // Required empty public constructor
    }

    public static SpecialOffersFragment newInstance(String param1, String param2) {
        SpecialOffersFragment fragment = new SpecialOffersFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize special offers list
        specialOffers = new ArrayList<>();
        // Load special offers from the database
        DataBaseHelper dbHelper = new DataBaseHelper(getActivity(), "PIZZA_RESTAURANT", null, 1);
        // Insert static special offers
        dbHelper.insertSpecialOffer("Special Pizza", "Delicious pizza with special toppings", 12.99, R.drawable.special, 1, "Medium", "meat", "2027-06-11T20:12:00");
        dbHelper.insertSpecialOffer("Combo Deal", "Get a pizza, drink, and dessert for a great price", 19.99, R.drawable.special, 1, "Large", "meat", "2027-06-11T20:28:00");
        dbHelper.insertSpecialOffer("Family Meal", "Large pizza with sides for the whole family", 24.99, R.drawable.special, 1, "Family Size", "meat", "2027-06-11T20:27:00");

        specialOffers = dbHelper.getSpecialOffers();
    }

    private void removeExpiredSpecialOffers() {
        DataBaseHelper dbHelper = new DataBaseHelper(getActivity(), "PIZZA_RESTAURANT", null, 1);
        dbHelper.deleteExpiredSpecialOffers();
        List<SpecialOffer> updatedSpecialOffers = dbHelper.getSpecialOffers();
        specialOffersAdapter.updateData(updatedSpecialOffers);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_special_offers, container, false);

        // Initialize RecyclerView
        recyclerViewSpecialOffers = view.findViewById(R.id.recyclerViewSpecialOffers);
        recyclerViewSpecialOffers.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter
        specialOffersAdapter = new SpecialOffersAdapter(getContext(), specialOffers);
        recyclerViewSpecialOffers.setAdapter(specialOffersAdapter);

        // Remove expired special offers and update RecyclerView
        removeExpiredSpecialOffers();

        specialOffersAdapter.setOnOrderClickListener(new SpecialOffersAdapter.OnOrderClickListener() {
            @Override
            public void onOrderClick(int position) {
                // Handle order button click here
                SpecialOffer specialOffer = specialOffers.get(position);
                Pizzas pizza = Pizzas.fromSpecialOffer(specialOffer);
                OrderDialogFragment dialogFragment = OrderDialogFragment.newInstance(pizza);
                dialogFragment.show(getParentFragmentManager(), "OrderDialog");
            }
        });

        return view;
    }


}
