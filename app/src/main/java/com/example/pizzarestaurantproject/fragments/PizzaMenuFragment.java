package com.example.pizzarestaurantproject.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzarestaurantproject.Pizza;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.adapters.PizzaAdapter;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;
import com.example.pizzarestaurantproject.models.Pizzas;


import java.util.ArrayList;
import java.util.List;

public class PizzaMenuFragment extends Fragment implements PizzaAdapter.OnPizzaClickListener {

    private RecyclerView recyclerView;
    private PizzaAdapter adapter;
    private List<Pizzas> pizzaList;
    private SearchView searchView;
    private EditText priceEditText;
    private Spinner sizeSpinner;
    private Spinner categorySpinner;
    private SharedPrefManager sharedPrefManager;


    public PizzaMenuFragment() {
        // Required empty public constructor
    }

    public static PizzaMenuFragment newInstance() {
        return new PizzaMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the pizza list and load pizza types as objects
        pizzaList = new ArrayList<>();
        loadPizzas(); // Method to populate the pizza list
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_menu, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        priceEditText = view.findViewById(R.id.priceEditText);
        sizeSpinner = view.findViewById(R.id.sizeSpinner);
        categorySpinner = view.findViewById(R.id.categorySpinner);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


       // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PizzaAdapter(getContext(),pizzaList,this);
        recyclerView.setAdapter(adapter);


        setupSearchView();
        // Set up size spinner
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.size_array, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        TextWatcher filterTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(searchView.getQuery().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This can be left empty
            }
        };

        priceEditText.addTextChangedListener(filterTextWatcher);





        return view;
    }
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilters(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilters(newText);
                return false;
            }
        });
    }


    private void applyFilters(String query) {
        adapter.applyTextFilter(query);
        adapter.applyPriceFilter(priceEditText.getText().toString());
        adapter.applySizeFilter(sizeSpinner.getSelectedItem().toString());
        adapter.applyCategoryFilter(categorySpinner.getSelectedItem().toString());
    }

    @Override
    public void onPizzaDetailsClick(Pizzas pizza) {
        // Handle click on pizza details
        showPizzaDetails(pizza);
    }

    @Override
    public void onAddToFavoritesClick(Pizzas pizza) {
        // Handle click on add to favorites
        addToFavorites(pizza);
    }

    @Override
    public void onOrderClick(Pizzas pizza) {
        // Handle click on order
        orderPizza(pizza);
    }

    private void showPizzaDetails(Pizzas pizza) {
        PizzaDetailFragment fragment = PizzaDetailFragment.newInstance(pizza);
        fragment.show(getParentFragmentManager(), "Details");
    }

    private void addToFavorites(Pizzas pizza) {
        DataBaseHelper dbHelper = new DataBaseHelper(requireContext(),"PIZZA_RESTAURANT",
                null, 1);
        sharedPrefManager =SharedPrefManager.getInstance(requireContext());
        String userEmail =sharedPrefManager.readString("email","No Val") ;

            dbHelper.insertFavorite(userEmail, pizza.getName(), pizza.getDescription(), pizza.getPrice(), pizza.getSize(), pizza.getCategory(), pizza.getImageResourceId());


        Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();

    }


    private void orderPizza(Pizzas pizza) {
        OrderDialogFragment dialogFragment = OrderDialogFragment.newInstance(pizza);
        dialogFragment.show(getParentFragmentManager(), "OrderDialog");
    }


    private void loadPizzas() {
        // Get the list of pizza types from the Pizza class or any other source
        List<String> pizzaTypes = getPizzaTypes();
        // Get the list of image resources or paths for each pizza type
        List<Integer> pizzaImages = getPizzaImages(); // or List<String> pizzaImages if using paths
        // Get the list of descriptions for each pizza type
        List<String> pizzaDescriptions = getPizzaDescriptions();
        // Get the list of categories for each pizza type
        List<String> pizzaCategories = getPizzaCategories();
        // Get the list of prices for each pizza type
        List<Double> pizzaPrices = getPizzaPrices();
        // Get the list of sizes for each pizza type
        List<String> pizzaSizes = getPizzaSizes();

        // Create Pizzas objects for each pizza type
        for (int i = 0; i < pizzaTypes.size(); i++) {
            String pizzaType = pizzaTypes.get(i);
            // Get the corresponding image resource for this pizza type
            int imageResource = pizzaImages.get(i);
            // Get the corresponding description for this pizza type
            String description = pizzaDescriptions.get(i);
            // Get the corresponding category for this pizza type
            String category = pizzaCategories.get(i);
            // Get the corresponding price for this pizza type
            double price = pizzaPrices.get(i);
            // Get the corresponding size for this pizza type
            String size = pizzaSizes.get(i);

            // Create a new Pizzas object and add it to the list
            pizzaList.add(new Pizzas(pizzaType, description,
                    price, size, category, imageResource));
        }
    }

    // Example methods to retrieve lists of descriptions and categories
    private List<String> getPizzaDescriptions() {
        List<String> descriptions = new ArrayList<>();
        descriptions.add("A delicious classic pizza with mozzarella and tomato sauce.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");
        descriptions.add("Pepperoni pizza topped with spicy pepperoni slices.");


        // Add more descriptions for other pizza types
        return descriptions;
    }

    private List<String> getPizzaCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("Vegetarian");
        categories.add("Meat");
        categories.add("Meat");
        categories.add("Beef");
        categories.add("Chicken");
        categories.add("Veggies");
        categories.add("Classic");
        categories.add("Meat");
        categories.add("Vegetarian");
        categories.add("Meat");
        categories.add("Meat");
        categories.add("Beef");
        categories.add("Chicken");
        return categories;
    }




    private List<String> getPizzaTypes() {
        return Pizza.getPizzaTypes();
    }

    private List<Integer> getPizzaImages() {
        List<Integer> pizzaImages = new ArrayList<>();
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_neaplaton);
        pizzaImages.add(R.drawable.pizza_hawaiian);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        pizzaImages.add(R.drawable.pizza_pepperoni);
        return pizzaImages;
    }
    private List<Double> getPizzaPrices() {
        List<Double> prices = new ArrayList<>();
        prices.add(10.99);
        prices.add(2.99);
        prices.add(12.99);
        prices.add(8.50);
        prices.add(15.75);
        prices.add(7.25);
        prices.add(11.30);
        prices.add(9.45);
        prices.add(13.60);
        prices.add(6.80);
        prices.add(14.25);
        prices.add(5.90);
        prices.add(16.50);
        return prices;
    }

    private List<String> getPizzaSizes() {
        List<String> sizes = new ArrayList<>();
        sizes.add("Small");
        sizes.add("Medium");
        sizes.add("Large");
        sizes.add("Extra Large");
        sizes.add("Small");
        sizes.add("Medium");
        sizes.add("Large");
        sizes.add("Extra Large");
        sizes.add("Small");
        sizes.add("Medium");
        sizes.add("Large");
        sizes.add("Extra Large");
        sizes.add("Small");

        return sizes;
    }

}
