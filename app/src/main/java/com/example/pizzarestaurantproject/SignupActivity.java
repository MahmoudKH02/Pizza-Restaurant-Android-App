package com.example.pizzarestaurantproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupActivity extends AppCompatActivity {

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

//        // Add the SignupFragment to the FragmentContainerView
//        if (savedInstanceState == null) {
//            // This ensures that we only add the fragment once when the activity is first created
//            SignupFragment signupFragment = new SignupFragment();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragmentContainerView, signupFragment)  // replace any existing fragment
//                    .commit();
//        }
    }

//    public boolean validInputs() {
//
//        boolean isValid = true;
//
//        if (InputValidator.validName(firstNameField.getText().toString())) {
//            changeColor(firstNameField, Color.parseColor(GREEN));
//            removeErrorMsg(namesLayout, namesErrorDisplayed);
//            namesErrorDisplayed = false;
//
//        } else {
//            changeColor(firstNameField, Color.parseColor(RED));
//            displayError(
//                    "Names Must be at least 3 characters long",
//                    namesLayout,
//                    namesErrorDisplayed
//            );
//            namesErrorDisplayed = true;
//            isValid = false;
//        }
//
//        if (InputValidator.validName(lastNameField.getText().toString())) {
//            changeColor(lastNameField, Color.parseColor(GREEN));
//            removeErrorMsg(namesLayout, namesErrorDisplayed);
//            namesErrorDisplayed = false;
//
//        } else {
//            changeColor(lastNameField, Color.parseColor(RED));
//            displayError(
//                    "Names Must be at least 3 characters long",
//                    namesLayout,
//                    namesErrorDisplayed
//            );
//            namesErrorDisplayed = true;
//            isValid = false;
//        }
//
//        if (!validGender(genderSpinner.getSelectedItem().toString()))
//            isValid = false;
//
//        if (!validEmail(emailField.getText().toString()))
//            isValid = false;
//
//        if (InputValidator.invalidPassword(passwordField.getText().toString())) {
//            displayError(
//                    "Password must be at least 8 characters long," +
//                    " and include one or more digits, " +
//                    "one or more alphabetic character",
//                    passwordField,
//                    passwordErrorDisplayed
//            );
//            passwordErrorDisplayed = true;
//            isValid = false;
//
//        } else {
//            removeErrorMsg(passwordField, passwordErrorDisplayed);
//            changeColor(passwordField, Color.parseColor(GREEN));
//            passwordErrorDisplayed = false;
//        }
//
//        if (InputValidator.invalidPassword(
//                passwordField.getText().toString(),
//                confirmPasswordField.getText().toString()))
//        {
//            displayError(
//                    "Passwords Do not Match!",
//                    confirmPasswordField,
//                    confirmPasswordErrorDisplayed
//            );
//            confirmPasswordErrorDisplayed = true;
//
//        } else {
//            removeErrorMsg(confirmPasswordField, confirmPasswordErrorDisplayed);
//            changeColor(confirmPasswordField, Color.parseColor(GREEN));
//            confirmPasswordErrorDisplayed = false;
//        }
//
//        if (InputValidator.invalidPhoneNumber(phoneNumberField.getText().toString())) {
//            displayError(
//                    "Phone number must be 10 digits long, and start with '05'",
//                    phoneNumberField,
//                    phoneErrorDisplayed
//            );
//            phoneErrorDisplayed = true;
//            isValid = false;
//
//        } else {
//            removeErrorMsg(phoneNumberField, phoneErrorDisplayed);
//            changeColor(phoneNumberField, Color.parseColor(GREEN));
//            phoneErrorDisplayed = false;
//        }
//
//        return isValid;
//    }
//
//    public boolean validGender(String gender) {
//
//        if ( !(gender.equalsIgnoreCase("Male")
//                || gender.equalsIgnoreCase("Female")) )
//        {
//            displayError("Choose a Gender", genderSpinner, genderErrorDisplayed);
//            genderErrorDisplayed = true;
//
//            return false;
//        }
//        changeColor(genderSpinner, Color.parseColor(GREEN));
//        removeErrorMsg(genderSpinner, genderErrorDisplayed);
//        genderErrorDisplayed = false;
//
//        return true;
//    }
//
//    public boolean validEmail(String email) {
//        DataBaseHelper dataBaseHelper = new DataBaseHelper(
//                SignupActivity.this,
//                "PIZZA_RESTAURANT",
//                null, 1
//        );
//
//        Cursor user = dataBaseHelper.getUser(email);
//
//        String errorMsg;
//
//        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            errorMsg = "Please Enter a Valid Email Address";
//        } else if (user != null && user.moveToFirst()) {
//            errorMsg = "This Email Already Exists";
//        } else {
//            removeErrorMsg(emailField, emailErrorDisplayed);
//            changeColor(emailField, Color.parseColor(GREEN));
//            emailErrorDisplayed = false;
//
//            return true;
//        }
//
//        displayError(errorMsg, emailField, emailErrorDisplayed);
//        emailErrorDisplayed = true;
//
//        dataBaseHelper.close();
//
//        return false;
//    }
//
//    /* Displays an error message */
//    public void displayError(String errorMsg, View field, boolean isDisplayed) {
//
//        if (!isDisplayed) {
//            LinearLayout mainLayout = findViewById(R.id.mainLayout);
//            TextView error = new TextView(SignupActivity.this);
//
//            error.setText(errorMsg);
//            error.setTextColor(Color.RED);
//
//            mainLayout.addView(error, mainLayout.indexOfChild(field));
//            changeColor(field, Color.parseColor(RED));
//        }
//    }
//
//    /* Removes the error message */
//    public void removeErrorMsg(View view, boolean isDisplayed) {
//
//        if (isDisplayed) {
//            LinearLayout mainLayout = findViewById(R.id.mainLayout);
//            mainLayout.removeViewAt(mainLayout.indexOfChild(view) - 1);
//        }
//    }
//
//    public void changeColor(View view, int color) {
//        view.setBackgroundTintList(ColorStateList.valueOf(color));
//    }
} // end class