package com.example.pizzarestaurantproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pizzarestaurantproject.Pizza;
import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;

import java.util.Calendar;
import java.util.Locale;


public class AddSpecialOffersFragment extends Fragment {

    private EditText offerTitle, offerDescription, offerNewPrice;
    private Spinner pizzaTypeSpinner;
    private DatePicker endDatePicker;
    private ImageView imagePreview;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddSpecialOffersFragment() {
        // Required empty public constructor
    }

    public static AddSpecialOffersFragment newInstance(String param1, String param2) {
        AddSpecialOffersFragment fragment = new AddSpecialOffersFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_special_offers_admin, container, false);

        offerTitle = view.findViewById(R.id.offerTitleAdmin);
        offerDescription = view.findViewById(R.id.offerDescriptionAdmin);
        offerNewPrice = view.findViewById(R.id.offerPriceAdmin);
        pizzaTypeSpinner = view.findViewById(R.id.pizzaTypeSpinner);
        endDatePicker = view.findViewById(R.id.endDatePicker);
        imagePreview = view.findViewById(R.id.offerImageAdmin);

        Button uploadImageButton = view.findViewById(R.id.uploadImageButtonAdmin);
        Button addOfferButton = view.findViewById(R.id.addOfferButton);

        // Setup Spinner
        Log.d("Size", String.valueOf(Pizza.getPizzaTypes().size()));
        String[] pizzaTypes = new String[ Pizza.getPizzaTypes().size() + 1 ];
        pizzaTypes[0] = "Select Pizza Type";

        for (int i = 1; i < pizzaTypes.length; i++)
            pizzaTypes[i] = Pizza.getPizzaTypes().get(i - 1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, pizzaTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pizzaTypeSpinner.setAdapter(adapter);

        uploadImageButton.setOnClickListener(v -> {
            // Trigger image selection and display in imagePreview
            // You will need to add logic to open the image picker and handle the result
        });

        addOfferButton.setOnClickListener(v ->  {
            addNewOffer();
            cleanFields();
        });

        return view;
    }

    public void addNewOffer() {
        String title = offerTitle.getText().toString();
        String description = offerDescription.getText().toString();
        String pizzaType = pizzaTypeSpinner.getSelectedItem().toString();
        double price = Double.parseDouble(offerNewPrice.getText().toString());
        int image = R.drawable.special;

        int day = endDatePicker.getDayOfMonth();
        int month = endDatePicker.getMonth() + 1; // Month is 0-based
        int year = endDatePicker.getYear();

        // Format the date into a readable string
        String selectedDate = String.format(
                Locale.getDefault(),
                "%04d-%02d-%02dT00:00:00", year, month, day
        );

        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                requireContext(),
                "PIZZA_RESTAURANT",
                null, 1
        );

        dataBaseHelper.insertSpecialOffer(title, description, price, image, 2, "M", "Meat", selectedDate);
        Toast.makeText(getContext(), title + " Was Created", Toast.LENGTH_SHORT).show();
        dataBaseHelper.close();
    }

    private void cleanFields() {
        offerTitle.setText("");
        offerDescription.setText("");
        offerNewPrice.setText("");
        pizzaTypeSpinner.setSelection(0);

        Calendar calendar = Calendar.getInstance();
        endDatePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        imagePreview.setImageResource(R.drawable.ic_launcher_background);
    }
}