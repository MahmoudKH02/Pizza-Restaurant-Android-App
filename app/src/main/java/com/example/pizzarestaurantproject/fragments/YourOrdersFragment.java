package com.example.pizzarestaurantproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.adapters.OrderAdapter;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.Order;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;

import java.util.List;

public class YourOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private DataBaseHelper dbHelper;
    private SharedPrefManager sharedPrefManager;


    public YourOrdersFragment() {
        // Required empty public constructor
    }

    public static YourOrdersFragment newInstance() {
        return new YourOrdersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new DataBaseHelper(requireContext(),"PIZZA_RESTAURANT",
                null, 1);
        sharedPrefManager = SharedPrefManager.getInstance(requireContext());
        String userEmail =sharedPrefManager.readString("email","No Val") ;


        List<Order> orders = dbHelper.getOrders(userEmail);

        adapter = new OrderAdapter(orders, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                // Handle order click event, possibly open a new fragment with order details
                OrderDetailsFragment fragment = OrderDetailsFragment.newInstance(order);
                fragment.show(getParentFragmentManager(), "Details");
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
