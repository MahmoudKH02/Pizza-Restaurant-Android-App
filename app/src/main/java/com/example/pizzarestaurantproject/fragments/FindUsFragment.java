package com.example.pizzarestaurantproject.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pizzarestaurantproject.R;

public class  FindUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_us, container, false);

        // Map Container Click Listener
        LinearLayout mapContainer = view.findViewById(R.id.mapContainer);
        mapContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps();
            }
        });

        // Email TextView Click Listener
        TextView emailTextView = view.findViewById(R.id.email);
        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailClient();
            }
        });

        // Phone Number TextView Click Listener
        TextView phoneTextView = view.findViewById(R.id.phoneNumber);
        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer();
            }
        });

        return view;
    }

    // Method to open Google Maps with a specific location
    private void openGoogleMaps() {
        Intent mapsIntent =new Intent();
        mapsIntent.setAction(Intent.ACTION_VIEW);
        mapsIntent.setData(Uri.parse("geo:31.961013, 35.190483"));
        startActivity(mapsIntent);
    }

    // Method to open email client with a predefined email address
    private void openEmailClient() {
        Intent gmailIntent = new Intent().setAction(Intent.ACTION_SENDTO);
        gmailIntent.setDataAndType(Uri.parse("mailto:"), "message/rfc822");
        gmailIntent.putExtra(Intent.EXTRA_EMAIL, "AdvancePizza@Pizza.com");
        gmailIntent.putExtra(Intent.EXTRA_SUBJECT,"My Subject");
        gmailIntent.putExtra(Intent.EXTRA_TEXT,"Content of the message");
        startActivity(gmailIntent);
    }

    // Method to open the phone dialer with a predefined phone number
    private void openDialer() {
        Intent dialIntent =new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:0599000000"));
        startActivity(dialIntent);
    }
}
