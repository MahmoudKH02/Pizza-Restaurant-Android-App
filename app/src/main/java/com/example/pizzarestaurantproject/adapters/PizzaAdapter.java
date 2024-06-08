package com.example.pizzarestaurantproject.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.models.Pizzas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private List<Pizzas> pizzaList;
    private List<Pizzas> pizzaListFull;
    private final OnPizzaClickListener listener;

    private String filterText = "";
    private String filterPrice = "";
    private String filterSize = "";
    private String filterCategory = "";


    public interface OnPizzaClickListener {
        void onPizzaDetailsClick(Pizzas pizza);
        void onAddToFavoritesClick(Pizzas pizza);
        void onOrderClick(Pizzas pizza);
    }

    public PizzaAdapter(List<Pizzas> pizzaList, OnPizzaClickListener listener) {
        this.pizzaList = pizzaList;
        this.pizzaListFull = new ArrayList<>(pizzaList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pizza, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizzas pizza = pizzaList.get(position);
        holder.bind(pizza, listener);
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    public void applyTextFilter(String text) {
        filterText = text.toLowerCase();
        applyFilters();
    }


    public void applyPriceFilter(String price) {
        filterPrice = price;
        applyFilters();
    }

    public void applySizeFilter(String size) {
        filterSize = size.toLowerCase();
        applyFilters();
    }

    public void applyCategoryFilter(String category) {
        filterCategory = category.toLowerCase();
        applyFilters();
    }

    private void applyFilters() {
        List<Pizzas> filteredList = pizzaListFull.stream()
                .filter(pizza -> (filterText.isEmpty() || pizza.getName().toLowerCase().contains(filterText)) &&
                        (filterPrice.isEmpty() || Double.toString(pizza.getPrice()).contains(filterPrice)) &&
                        (filterSize.isEmpty() || pizza.getSize().equalsIgnoreCase(filterSize)) &&
                        (filterCategory.isEmpty() || pizza.getCategory().equalsIgnoreCase(filterCategory)))
                .collect(Collectors.toList());

        pizzaList.clear();
        pizzaList.addAll(filteredList);
        notifyDataSetChanged();
    }


    public class PizzaViewHolder extends RecyclerView.ViewHolder {

        private final TextView pizzaName;
        private final Button addToFavoritesButton;
        private final Button orderButton;
        private final ImageView pizzaImage;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaName = itemView.findViewById(R.id.pizzaName);
            addToFavoritesButton = itemView.findViewById(R.id.addToFavorites);
            orderButton = itemView.findViewById(R.id.orderButton);
            pizzaImage = itemView.findViewById(R.id.pizzaImage);

            // Change the text color of pizzaName to white
            pizzaName.setTextColor(Color.WHITE);
        }


        public void bind(Pizzas pizza, OnPizzaClickListener listener) {
            pizzaName.setText(pizza.getName());
            pizzaImage.setImageResource(pizza.getImageResourceId());
            pizzaName.setOnClickListener(v -> listener.onPizzaDetailsClick(pizza));
            addToFavoritesButton.setOnClickListener(v -> listener.onAddToFavoritesClick(pizza));
            orderButton.setOnClickListener(v -> listener.onOrderClick(pizza));
        }
    }

}
