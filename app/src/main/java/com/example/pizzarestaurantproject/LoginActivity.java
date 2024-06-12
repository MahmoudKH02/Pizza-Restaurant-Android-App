package com.example.pizzarestaurantproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.PasswordHashing;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;

import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity {

    private SharedPrefManager sharedPrefManager;
    private CheckBox checkBox;
    private EditText email;
    private EditText password;
    private boolean emailErrorDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = (EditText) findViewById(R.id.emailTextLogin);
        checkBox = (CheckBox) findViewById(R.id.rememberMeBox);
        password = (EditText) findViewById(R.id.passwordLogin);

        Button login = (Button) findViewById(R.id.createAccountButton);
        Button signup = (Button) findViewById(R.id.signupButtonFromLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        LoginActivity.this,
                        "PIZZA_RESTAURANT",
                        null, 1
                );

                Cursor cursor = dataBaseHelper.getUser(email.getText().toString());

                try {

                    if (cursor != null && cursor.moveToFirst() && checkCorrectPassword(cursor)) {
                        int adminIndex = cursor.getColumnIndex("IS_ADMIN");
                        boolean isAdmin = cursor.getInt(adminIndex) == 1;

                        Intent intent;

                        if (isAdmin) {
                            intent = new Intent(LoginActivity.this, AdminActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                        }
                        sharedPrefManager.writeBoolean("remember", checkBox.isChecked());
                        sharedPrefManager.writeString("email", email.getText().toString());

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        displayError("Please Check your Email and Password, then try again");
                    }

                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } finally {
                    dataBaseHelper.close();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkCorrectPassword(Cursor user) throws NoSuchAlgorithmException {
        int passIndex = user.getColumnIndex("PASSWORD");
        int saltIndex = user.getColumnIndex("SALT");

        String enteredPassword = password.getText().toString();
        String storedHashedPassword = user.getString(passIndex);
        String storedSalt = user.getString(saltIndex);

        String hashedPassword = PasswordHashing.hashPassword(enteredPassword, storedSalt);

        return hashedPassword.equals(storedHashedPassword);
    }

    public void displayError(String errorMsg) {
        LinearLayout mainLayout = findViewById(R.id.linearLayout);
        TextView error = new TextView(LoginActivity.this);

        error.setText(errorMsg);
        error.setTextColor(Color.RED);

        if (!emailErrorDisplayed) {
            mainLayout.addView(error, 0);
            emailErrorDisplayed = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPrefManager = SharedPrefManager.getInstance(this);

        boolean checked = sharedPrefManager.readBoolean("remember",false);
        boolean newAccount = sharedPrefManager.readBoolean("new_account", false);

        checkBox.setChecked(checked);

        if (checked || newAccount)
            email.setText(sharedPrefManager.readString("email", ""));
        else
            email.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();

        sharedPrefManager.writeBoolean("remember", checkBox.isChecked());
        sharedPrefManager.writeString("email", email.getText().toString());
        sharedPrefManager.writeBoolean("new_account", false);
    }

} // end class