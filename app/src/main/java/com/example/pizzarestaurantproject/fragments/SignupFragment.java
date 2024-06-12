package com.example.pizzarestaurantproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pizzarestaurantproject.AdminActivity;
import com.example.pizzarestaurantproject.LoginActivity;
import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.SignupActivity;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.InputValidator;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;
import com.example.pizzarestaurantproject.models.User;

import java.security.NoSuchAlgorithmException;

public class SignupFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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

    private View view;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        namesLayout = view.findViewById(R.id.namesLayout);
        firstNameField = view.findViewById(R.id.firstNameText);
        lastNameField = view.findViewById(R.id.lastNameText);
        emailField = view.findViewById(R.id.emailTextSignup);
        passwordField = view.findViewById(R.id.passwordTextSignup);
        confirmPasswordField = view.findViewById(R.id.confirmPasswordText);
        phoneNumberField = view.findViewById(R.id.phoneText);

        Button createAccount = view.findViewById(R.id.createAccountButton);

        String[] options = {"Select Gender", "Male", "Female"};
        genderSpinner = view.findViewById(R.id.genderSpinner);

        ArrayAdapter<String> objGenderArr = new
                ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, options);

        genderSpinner.setAdapter(objGenderArr);

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(requireContext());

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
                    user.setProfilePicturePath("AUGH");

                    user.setAdmin(requireActivity() instanceof AdminActivity);

                    try (DataBaseHelper dbHelper = new DataBaseHelper(
                            requireContext(),
                            "PIZZA_RESTAURANT",
                            null, 1
                    )) {
                        dbHelper.insertUser(user);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(
                            requireContext(),
                            "Account Created Successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    Activity activity = requireActivity();

                    if (activity instanceof SignupActivity) {
                        // write the registered email to the shared preferences
                        sharedPrefManager.writeString("email", user.getEmail());
                        sharedPrefManager.writeBoolean("new_account", true);
                        Intent intent = new Intent(activity, LoginActivity.class);
                        startActivity(intent);
                        requireActivity().finish();

                    } else {
                        clearAllFields();
                    }
                }
            }
        });

        return view;
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
                requireContext(),
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
            LinearLayout mainLayout = view.findViewById(R.id.signup_main);
            TextView error = new TextView(requireActivity());

            error.setText(errorMsg);
            error.setTextColor(Color.RED);

            mainLayout.addView(error, mainLayout.indexOfChild(field));
            changeColor(field, Color.parseColor(RED));
        }
    }

    /* Removes the error message */
    public void removeErrorMsg(View field, boolean isDisplayed) {

        if (isDisplayed) {
            LinearLayout mainLayout = view.findViewById(R.id.signup_main);
            mainLayout.removeViewAt(mainLayout.indexOfChild(field) - 1);
        }
    }

    public void changeColor(View field, int color) {
        field.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void clearAllFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        phoneNumberField.setText("");

        genderSpinner.setSelection(0);
    }
}