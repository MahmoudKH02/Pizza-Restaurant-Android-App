package com.example.pizzarestaurantproject.adapters;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;
import com.example.pizzarestaurantproject.models.Pizzas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private List<Pizzas> pizzaList;
    private Context context;

    private List<Pizzas> pizzaListFull;
    private final OnPizzaClickListener listener;
    private SharedPrefManager sharedPrefManager;




    private String filterText = "";
    private String filterPrice = "";
    private String filterSize = "";
    private String filterCategory = "";


    public interface OnPizzaClickListener {
        void onPizzaDetailsClick(Pizzas pizza);

        void onAddToFavoritesClick(Pizzas pizza);

        void onOrderClick(Pizzas pizza);
    }

    public PizzaAdapter(Context context, List<Pizzas> pizzaList, OnPizzaClickListener listener) {
        this.context = context;
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
        filterSize = size.equals("All") ? "" : size;
        applyFilters();
    }

    public void applyCategoryFilter(String category) {
        filterCategory = category.equals("All") ? "" : category;
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
        private final Button orderButton;
        private final ImageView pizzaImage;
        private final ImageView favoriteIcon; // Use ImageView for favorite icon
        Animation scaleUpAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_up);

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaName = itemView.findViewById(R.id.pizzaName);
            orderButton = itemView.findViewById(R.id.orderButton);
            pizzaImage = itemView.findViewById(R.id.pizzaImage);
            favoriteIcon = itemView.findViewById(R.id.imageView2); // Initialize the favorite icon


            // Change the text color of pizzaName to white
           // pizzaName.setTextColor(Color.WHITE);
        }

        public void bind(Pizzas pizza, OnPizzaClickListener listener) {
            pizzaName.setText(pizza.getName());
            pizzaImage.setImageResource(pizza.getImageResourceId());
            pizzaName.setOnClickListener(v -> listener.onPizzaDetailsClick(pizza));

            // Use dbHelper object
            DataBaseHelper dbHelper = new DataBaseHelper(itemView.getContext(), "PIZZA_RESTAURANT", null, 1);

            // Check if the pizza is already in favorites and set icon accordingly
            SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(itemView.getContext());
            String userEmail = sharedPrefManager.readString("email", "No Val");
            if (dbHelper.isPizzaInFavorites(userEmail, pizza.getName())) {
                favoriteIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.red)); // Change color to red
            } else {
                favoriteIcon.setColorFilter(null); // Remove any previous color filters
            }

            favoriteIcon.setOnClickListener(v -> {
                if (dbHelper.isPizzaInFavorites(userEmail, pizza.getName())) {
                    // Remove from favorites
                    dbHelper.deleteFavorite(userEmail, pizza.getName());
                    pizza.setFavorite(false); // Mark the pizza as not favorite
                    favoriteIcon.setColorFilter(null); // Change color to default (black)
                    // Bring the favorite icon to the front
                    favoriteIcon.bringToFront();
                    // Apply the animation
                    favoriteIcon.startAnimation(scaleUpAnimation);
                } else {
                    // Add to favorites
                    listener.onAddToFavoritesClick(pizza);
                    pizza.setFavorite(true); // Mark the pizza as favorite
                    favoriteIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.red)); // Change color to red
                    // Bring the favorite icon to the front
                    favoriteIcon.bringToFront();
                    // Apply the animation
                    favoriteIcon.startAnimation(scaleUpAnimation);
                }
            });



            orderButton.setOnClickListener(v -> listener.onOrderClick(pizza));
        }


    }
}

