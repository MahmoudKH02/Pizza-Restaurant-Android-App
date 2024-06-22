package com.example.pizzarestaurantproject.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;

import java.text.DecimalFormat;


public class OrdersReportFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OrdersReportFragment() { }

    public static OrdersReportFragment newInstance(String param1, String param2) {
        OrdersReportFragment fragment = new OrdersReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_report, container, false);
        TextView textView = view.findViewById(R.id.orderReportView);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                requireContext(),
                "PIZZA_RESTAURANT",
                null, 1
        );

        Cursor cursor = dataBaseHelper.getOrderInfoForEachType();
        StringBuilder stringBuilder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");

        if (cursor != null && cursor.moveToFirst()) {

            do {
                String pizzaType = cursor.getString(0);
                int numOrders = cursor.getInt(1);
                double income = cursor.getDouble(2);

                stringBuilder.append(pizzaType).append(",\t").append("Orders: ").append(numOrders)
                        .append("\n").append("Total Income: ").append(df.format(income)).append(" $\n\n");

            } while (cursor.moveToNext());
        }

        cursor = dataBaseHelper.getIncomeAllTypes();

        if (cursor != null && cursor.moveToFirst()) {
            double totalIncome = cursor.getDouble(0);

            stringBuilder.append("\n\n").append("Total Income From All Pizza Types: ")
                    .append(df.format(totalIncome)).append(" $");
        }

        dataBaseHelper.close();

        textView.setText(stringBuilder.toString());

        return view;
    }
}