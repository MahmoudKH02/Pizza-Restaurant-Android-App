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

import java.util.List;

public class FavoritePizzaAdapter extends RecyclerView.Adapter<FavoritePizzaAdapter.ViewHolder> {
    private List<Pizzas> favoritePizzas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onOrderClick(Pizzas pizza);
        void onUndoFavoriteClick(Pizzas pizza);
    }

    public FavoritePizzaAdapter(List<Pizzas> favoritePizzas, OnItemClickListener listener) {
        this.favoritePizzas = favoritePizzas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_pizza, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pizzas pizza = favoritePizzas.get(position);
        holder.pizzaNameTextView.setText(pizza.getName());
        holder.pizzaNameTextView.setTextColor(Color.WHITE);
        holder.pizzaImageView.setImageResource(pizza.getImageResourceId());

        holder.orderButton.setOnClickListener(v -> listener.onOrderClick(pizza));
        holder.undoFavoriteButton.setOnClickListener(v -> listener.onUndoFavoriteClick(pizza));
    }

    @Override
    public int getItemCount() {
        return favoritePizzas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pizzaImageView;
        TextView pizzaNameTextView;
        Button orderButton;
        Button undoFavoriteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaImageView = itemView.findViewById(R.id.pizzaImageView);
            pizzaNameTextView = itemView.findViewById(R.id.pizzaNameTextView);
            orderButton = itemView.findViewById(R.id.orderButton);
            undoFavoriteButton = itemView.findViewById(R.id.undoFavoriteButton);
        }
    }
}