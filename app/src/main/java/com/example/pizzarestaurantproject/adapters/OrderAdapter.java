package com.example.pizzarestaurantproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public OrderAdapter(List<Order> orders, OnItemClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.pizzaNameTextView.setText(order.getPizzaType());
        holder.dateTextView.setText(order.getFormattedDate());
        holder.imageView.setImageResource(order.getImageResourceId());

        holder.itemView.setOnClickListener(v -> {
            // Handle item click by calling onItemClick method of the listener
            listener.onItemClick(order);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaNameTextView, priceTextView, dateTextView, quantityTextView, sizeTextView, categoryTextView;
        ImageView imageView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaNameTextView = itemView.findViewById(R.id.pizza_name_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}

