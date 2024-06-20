package com.example.pizzarestaurantproject.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pizzarestaurantproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textViewDescription;
    private String fullDescription;
    private int currentIndex = 0;
    private Handler handler;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Initialize the full description
        fullDescription = "Established in 1995, our restaurant began as a humble family-owned endeavor in the heart of downtown. " +
                "With a passion for culinary excellence and a commitment to serving authentic flavors, we quickly gained a loyal following. " +
                "Over the years, our menu has evolved, inspired by both traditional recipes and innovative culinary trends. " +
                "What started as a cozy diner with a few tables has grown into a bustling eatery known for its warm hospitality and unforgettable dishes. " +
                "We take pride in sourcing local ingredients and crafting each meal with care, ensuring every bite reflects our dedication to quality. " +
                "As we celebrate our journey.";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Initialize views
        textViewDescription = view.findViewById(R.id.textViewDescription);

        // Start text animation
        handler = new Handler();
        // Start text animation
        animateText();

        return view;

    }

    private void animateText() {
        if (currentIndex <= fullDescription.length()) {
            String substring = fullDescription.substring(0, currentIndex);
            textViewDescription.setText(Html.fromHtml(substring)); // Use Html.fromHtml to preserve formatting if needed
            currentIndex++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animateText();
                }
            }, 50); // Adjust delay (milliseconds) to control the speed of animation
        }
    }
}
