package com.example.pizzarestaurantproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.models.SpecialOffer;

import java.util.List;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.ViewHolder> {

    private Context context;
    private List<SpecialOffer> specialOffers;
    private OnOrderClickListener onOrderClickListener;

    public SpecialOffersAdapter(Context context, List<SpecialOffer> specialOffers) {
        this.context = context;
        this.specialOffers = specialOffers;
    }

    public interface OnOrderClickListener {
        void onOrderClick(int position);
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.onOrderClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_special_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SpecialOffer specialOffer = specialOffers.get(position);
        holder.titleTextView.setText(specialOffer.getTitle());
        holder.descriptionTextView.setText(specialOffer.getDescription());
        holder.priceTextView.setText(String.format("$%.2f", specialOffer.getPrice()));
        // Load image using Picasso library
        holder.imageView.setImageResource(specialOffer.getImageUrl());

        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOrderClickListener != null) {
                    onOrderClickListener.onOrderClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return specialOffers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        ImageView imageView;
        Button orderButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageView = itemView.findViewById(R.id.imageView);
            orderButton = itemView.findViewById(R.id.orderButton);
        }
    }
}
