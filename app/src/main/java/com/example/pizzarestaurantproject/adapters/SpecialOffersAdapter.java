package com.example.pizzarestaurantproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.models.SpecialOffer;

import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        holder.titleTextView.setTextColor(Color.WHITE);
        holder.descriptionTextView.setTextColor(Color.WHITE);
        holder.priceTextView.setTextColor(Color.WHITE);
        // Load image using Picasso library
        holder.imageView.setImageResource(specialOffer.getImageUrl());
        updateCountdownTimer(holder, specialOffer, position);


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

    public void updateData(List<SpecialOffer> newSpecialOffers) {
        this.specialOffers.clear();
        this.specialOffers.addAll(newSpecialOffers);
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        int offerId = specialOffers.get(position).getOfferId();
        specialOffers.remove(position);
        notifyItemRemoved(position);
        removeExpiredOfferFromDatabase(offerId);
    }
    private void removeExpiredOfferFromDatabase(int offerId) {
        DataBaseHelper dbHelper = new DataBaseHelper(context,   "PIZZA_RESTAURANT", null, 1);
        dbHelper.deleteSpecialOffer(offerId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        ImageView imageView;
        Button orderButton;
        TextView countdownTimerTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageView = itemView.findViewById(R.id.imageView);
            orderButton = itemView.findViewById(R.id.orderButton);
            countdownTimerTextView = itemView.findViewById(R.id.countdownTimerTextView);

        }
    }
    private void updateCountdownTimer(ViewHolder holder, SpecialOffer specialOffer, int position) {
        Date offerEndDate = specialOffer.getOfferEndDate();
        if (offerEndDate == null) return;

        long remainingTime = offerEndDate.getTime() - System.currentTimeMillis();

        if (remainingTime > 0) {
            new CountDownTimer(remainingTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long hours = millisUntilFinished / (1000 * 60 * 60);
                    long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                    long seconds = (millisUntilFinished % (1000 * 60)) / 1000;

                    String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                    holder.countdownTimerTextView.setText(timeLeftFormatted);
                    holder.countdownTimerTextView.setTextColor(Color.WHITE);
                }

                @Override
                public void onFinish() {
                    holder.countdownTimerTextView.setText("00:00:00");
                    // Remove the expired offer from the list
                    removeItem(holder.getAdapterPosition());
                }
            }.start();
        } else {
            holder.countdownTimerTextView.setText("00:00:00");
            // Remove the expired offer from the list
            removeItem(holder.getAdapterPosition());
        }
    }
}
