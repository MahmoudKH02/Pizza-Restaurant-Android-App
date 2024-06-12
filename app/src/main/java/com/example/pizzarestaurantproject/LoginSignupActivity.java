package com.example.pizzarestaurantproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzarestaurantproject.helper.DataBaseHelper;

import java.util.Arrays;

public class LoginSignupActivity extends AppCompatActivity {

    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login = (Button) findViewById(R.id.loginButton);
        Button signup = (Button) findViewById(R.id.signupButton);

        view = findViewById(R.id.info);

        view.setText(Arrays.toString(Pizza.getPizzaTypes().toArray()));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignupActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        StringBuilder s = new StringBuilder();

        try (DataBaseHelper dbHelper = new DataBaseHelper(
                LoginSignupActivity.this,
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
} // end class