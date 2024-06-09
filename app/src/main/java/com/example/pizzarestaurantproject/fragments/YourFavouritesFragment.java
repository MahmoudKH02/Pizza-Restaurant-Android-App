package com.example.pizzarestaurantproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.adapters.FavoritePizzaAdapter;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;
import com.example.pizzarestaurantproject.models.Pizzas;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourFavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourFavouritesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DataBaseHelper dbHelper;
    private List<Pizzas> favoritePizzas;
    private SharedPrefManager sharedPrefManager;

    private FavoritePizzaAdapter adapter;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourFavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourFavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourFavouritesFragment newInstance(String param1, String param2) {
        YourFavouritesFragment fragment = new YourFavouritesFragment();
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
        sharedPrefManager = SharedPrefManager.getInstance(requireContext());
        String userEmail =sharedPrefManager.readString("email","No Val") ;
        dbHelper = new DataBaseHelper(requireContext(),"PIZZA_RESTAURANT",null, 1);
        favoritePizzas = dbHelper.getFavorites(userEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_favourites, container, false);

        // Set up RecyclerView or ListView to display favoritePizzas
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Create a list to hold the Pizzas objects corresponding to favorite pizza names


         adapter = new FavoritePizzaAdapter(favoritePizzas, new FavoritePizzaAdapter.OnItemClickListener() {
            @Override
            public void onOrderClick(Pizzas pizza) {
                OrderDialogFragment dialogFragment = OrderDialogFragment.newInstance(pizza);
                dialogFragment.show(getParentFragmentManager(), "OrderDialog");
            }

            @Override
            public void onUndoFavoriteClick(Pizzas pizza) {
                sharedPrefManager = SharedPrefManager.getInstance(requireContext());
                String userEmail =sharedPrefManager.readString("email","No Val") ;
                dbHelper.deleteFavorite(userEmail, pizza.getName());
                favoritePizzas.remove(pizza);
                adapter.notifyDataSetChanged();
            }

        });
        recyclerView.setAdapter(adapter);

        return view;
    }
    //  Adapter


}