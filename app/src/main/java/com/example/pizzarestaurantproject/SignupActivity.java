package com.example.pizzarestaurantproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.InputValidator;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;
import com.example.pizzarestaurantproject.models.User;

import java.security.NoSuchAlgorithmException;

public class SignupActivity extends AppCompatActivity {

    private static final String GREEN = "#4CAF50";
    private static final String RED = "#FFFF0057";

    private LinearLayout namesLayout;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText phoneNumberField;
    private Spinner genderSpinner;

    private static boolean namesErrorDisplayed = false;
    private static boolean emailErrorDisplayed = false;
    private static boolean passwordErrorDisplayed = false;
    private static boolean confirmPasswordErrorDisplayed = false;
    private static boolean phoneErrorDisplayed = false;
    private static boolean genderErrorDisplayed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        namesLayout = findViewById(R.id.namesLayout);
        firstNameField = findViewById(R.id.firstNameText);
        lastNameField = findViewById(R.id.lastNameText);
        emailField = findViewById(R.id.emailTextSignup);
        passwordField = findViewById(R.id.passwordTextSignup);
        confirmPasswordField = findViewById(R.id.confirmPasswordText);
        phoneNumberField = findViewById(R.id.phoneText);

        Button createAccount = findViewById(R.id.createAccountButton);

        String[] options = {"Select Gender", "Male", "Female"};
        genderSpinner = findViewById(R.id.genderSpinner);

        ArrayAdapter<String> objGenderArr = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);

        genderSpinner.setAdapter(objGenderArr);

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();

                if (validInputs()) {
                    user.setFirstName(firstNameField.getText().toString());
                    user.setLastName(lastNameField.getText().toString());
                    user.setGender(genderSpinner.getSelectedItem().toString());
                    user.setEmail(emailField.getText().toString());
                    user.setPassword(passwordField.getText().toString());
                    user.setPhoneNumber(phoneNumberField.getText().toString());
                    user.setAdmin(false);
                    user.setProfilePicturePath("AUGH");

                    try (DataBaseHelper dbHelper = new DataBaseHelper(
                            SignupActivity.this,
                            "PIZZA_RESTAURANT",
                            null, 1
                    )) {
                        dbHelper.insertUser(user, null);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }

                    // write the registered email to the shared preferences
                    sharedPrefManager.writeString("email", user.getEmail());

                    Toast.makeText(
                            SignupActivity.this,
                            "Account Created Successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    // start the Login activity
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public boolean validInputs() {

        boolean isValid = true;

        if (InputValidator.validName(firstNameField.getText().toString())) {
            changeColor(firstNameField, Color.parseColor(GREEN));
            removeErrorMsg(namesLayout, namesErrorDisplayed);
            namesErrorDisplayed = false;

        } else {
            changeColor(firstNameField, Color.parseColor(RED));
            displayError(
                    "Names Must be at least 3 characters long",
                    namesLayout,
                    namesErrorDisplayed
            );
            namesErrorDisplayed = true;
            isValid = false;
        }

        if (InputValidator.validName(lastNameField.getText().toString())) {
            changeColor(lastNameField, Color.parseColor(GREEN));
            removeErrorMsg(namesLayout, namesErrorDisplayed);
            namesErrorDisplayed = false;

        } else {
            changeColor(lastNameField, Color.parseColor(RED));
            displayError(
                    "Names Must be at least 3 characters long",
                    namesLayout,
                    namesErrorDisplayed
            );
            namesErrorDisplayed = true;
            isValid = false;
        }

        if (!validGender(genderSpinner.getSelectedItem().toString()))
            isValid = false;

        if (!validEmail(emailField.getText().toString()))
            isValid = false;

        if (InputValidator.invalidPassword(passwordField.getText().toString())) {
            displayError(
                    "Password must be at least 8 characters long," +
                    " and include one or more digits, " +
                    "one or more alphabetic character",
                    passwordField,
                    passwordErrorDisplayed
            );
            passwordErrorDisplayed = true;
            isValid = false;

        } else {
            removeErrorMsg(passwordField, passwordErrorDisplayed);
            changeColor(passwordField, Color.parseColor(GREEN));
            passwordErrorDisplayed = false;
        }

        if (InputValidator.invalidPassword(
                passwordField.getText().toString(),
                confirmPasswordField.getText().toString()))
        {
            displayError(
                    "Passwords Do not Match!",
                    confirmPasswordField,
                    confirmPasswordErrorDisplayed
            );
            confirmPasswordErrorDisplayed = true;

        } else {
            removeErrorMsg(confirmPasswordField, confirmPasswordErrorDisplayed);
            changeColor(confirmPasswordField, Color.parseColor(GREEN));
            confirmPasswordErrorDisplayed = false;
        }

        if (InputValidator.invalidPhoneNumber(phoneNumberField.getText().toString())) {
            displayError(
                    "Phone number must be 10 digits long, and start with '05'",
                    phoneNumberField,
                    phoneErrorDisplayed
            );
            phoneErrorDisplayed = true;
            isValid = false;

        } else {
            removeErrorMsg(phoneNumberField, phoneErrorDisplayed);
            changeColor(phoneNumberField, Color.parseColor(GREEN));
            phoneErrorDisplayed = false;
        }

        return isValid;
    }

    public boolean validGender(String gender) {

        if ( !(gender.equalsIgnoreCase("Male")
                || gender.equalsIgnoreCase("Female")) )
        {
            displayError("Choose a Gender", genderSpinner, genderErrorDisplayed);
            genderErrorDisplayed = true;

            return false;
        }
        changeColor(genderSpinner, Color.parseColor(GREEN));
        removeErrorMsg(genderSpinner, genderErrorDisplayed);
        genderErrorDisplayed = false;

        return true;
    }

    public boolean validEmail(String email) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                SignupActivity.this,
                "PIZZA_RESTAURANT",
                null, 1
        );

        Cursor user = dataBaseHelper.getUser(email);

        String errorMsg;

        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMsg = "Please Enter a Valid Email Address";
        } else if (user != null && user.moveToFirst()) {
            errorMsg = "This Email Already Exists";
        } else {
            removeErrorMsg(emailField, emailErrorDisplayed);
            changeColor(emailField, Color.parseColor(GREEN));
            emailErrorDisplayed = false;

            return true;
        }

        displayError(errorMsg, emailField, emailErrorDisplayed);
        emailErrorDisplayed = true;

        dataBaseHelper.close();

        return false;
    }

    /* Displays an error message */
    public void displayError(String errorMsg, View field, boolean isDisplayed) {

        if (!isDisplayed) {
            LinearLayout mainLayout = findViewById(R.id.mainLayout);
            TextView error = new TextView(SignupActivity.this);

            error.setText(errorMsg);
            error.setTextColor(Color.RED);

            mainLayout.addView(error, mainLayout.indexOfChild(field));
            changeColor(field, Color.parseColor(RED));
        }
    }

    /* Removes the error message */
    public void removeErrorMsg(View view, boolean isDisplayed) {

        if (isDisplayed) {
            LinearLayout mainLayout = findViewById(R.id.mainLayout);
            mainLayout.removeViewAt(mainLayout.indexOfChild(view) - 1);
        }
    }

    public void changeColor(View view, int color) {
        view.setBackgroundTintList(ColorStateList.valueOf(color));
    }
} // end class