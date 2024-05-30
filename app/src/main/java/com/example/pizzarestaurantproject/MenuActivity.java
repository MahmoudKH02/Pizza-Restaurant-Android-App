package com.example.pizzarestaurantproject;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzarestaurantproject.helper.DataBaseHelper;

public class MenuActivity extends AppCompatActivity {

    private static final int BACK_PRESS_INTERVAL = 2000; // 2 seconds
    private long lastBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView view = findViewById(R.id.view);

        StringBuilder s = new StringBuilder();

        try (DataBaseHelper dbHelper = new DataBaseHelper(
                MenuActivity.this,
                "PIZZA_RESTAURANT",
                null, 1
        )) {
            Cursor allUsers = dbHelper.getAllUsers();

            if (allUsers != null && allUsers.moveToFirst()) {
                s.append("[\n");

                do {
                    s.append("(");

                    s.append("Email: ").append(allUsers.getString(0)).append("\n");
                    s.append("Password: ").append(allUsers.getString(1)).append("\n");
                    s.append("Salt: ").append(allUsers.getString(2)).append("\n");
                    s.append("FirstName: ").append(allUsers.getString(3)).append("\n");
                    s.append("LastName: ").append(allUsers.getString(4)).append("\n");
                    s.append("Phone: ").append(allUsers.getString(5)).append("\n");
                    s.append("Gender: ").append(allUsers.getString(6)).append("\n");
//                    s.append("Profile Pic").append(allUsers.getString(7));
                    s.append("Is Admin: ").append(allUsers.getInt(8));

                    s.append(")\n");

                } while (allUsers.moveToNext());

                s.append("]");
            }
        }

        view.setText(s.toString());
    }

    @Override
    public void onBackPressed() {
        if (lastBackPressedTime + BACK_PRESS_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            lastBackPressedTime = System.currentTimeMillis();
        }
    }
} // end class