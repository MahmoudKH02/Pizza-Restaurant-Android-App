package com.example.pizzarestaurantproject;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzarestaurantproject.helper.ConnectionAsyncTask;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.models.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class GetStartedActivity extends AppCompatActivity {

    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setProgress(false);

        getStartedButton = (Button) findViewById(R.id.getStarted);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask connectionAsyncTask = new
                        ConnectionAsyncTask(GetStartedActivity.this);

                connectionAsyncTask.execute("https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/");

                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        GetStartedActivity.this,
                        "PIZZA_RESTAURANT",
                        null, 1
                );

                String adminEmail = "admin@gmail.com";
                Cursor cursor = dataBaseHelper.getUser(adminEmail);

                if (cursor != null && !cursor.moveToFirst()) { /* Admin does not exist */
                    User admin = new User(
                            adminEmail, "admin123",
                            "Mahmoud", "Khatib",
                            "0599988877", "Male",
                            "", true
                    );

                    try {
                        dataBaseHelper.insertUser(admin);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } finally {
                        dataBaseHelper.close();
                    }
                }
            } // end onCLick()
        });
    }

    public void setButtonText(String text) {
        getStartedButton.setText(text);
    }

    public void fillPizzaTypes(List<String> pizzaTypes) {
        Pizza.setPizzaTypes(pizzaTypes);
    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

} // end class