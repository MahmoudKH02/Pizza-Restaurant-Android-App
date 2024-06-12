package com.example.pizzarestaurantproject.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurantproject.Order;
import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.adapters.OrderAdapter;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;

import java.util.ArrayList;
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
        String userEmail = sharedPrefManager.readString("email","No Val");
        Log.d("EMail", userEmail);

        Cursor cursor = dbHelper.getUser(userEmail);

        cursor.moveToFirst();
        Log.d("EMail", cursor.getString(0));

        int adminIndex = cursor.getColumnIndex("IS_ADMIN");
        boolean isAdmin = cursor.getInt(adminIndex) == 1;

        List<Order> orders = null;

        if (isAdmin) {
            cursor = dbHelper.getAllOrders();
            orders = new ArrayList<>();

            if (cursor.moveToFirst()) {
                int pizzaTypeIndex = cursor.getColumnIndex("PIZZA_TYPE");
                int priceIndex = cursor.getColumnIndex("PRICE");
                int timeOfOrderIndex = cursor.getColumnIndex("TIME_OF_ORDER");
                int quantityIndex = cursor.getColumnIndex("QUANTITY");
                int sizeIndex = cursor.getColumnIndex("SIZE");
                int imageIndex = cursor.getColumnIndex("IMAGE_RESOURCE_ID");
                int emailIndex = cursor.getColumnIndex("EMAIL");

                do {
                    String pizzaType = cursor.getString(pizzaTypeIndex);
                    double price = cursor.getDouble(priceIndex);
                    String timeOfOrder = cursor.getString(timeOfOrderIndex);
                    int quantity = cursor.getInt(quantityIndex);
                    String size = cursor.getString(sizeIndex);
                    int imageResourceId = cursor.getInt(imageIndex);
                    String email = cursor.getString(emailIndex);

                    orders.add(new Order(
                            email, pizzaType, price, timeOfOrder,
                            quantity, size, imageResourceId
                    ));

                } while (cursor.moveToNext());
            }
        } else {
            orders = dbHelper.getOrders(userEmail);
        }
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
