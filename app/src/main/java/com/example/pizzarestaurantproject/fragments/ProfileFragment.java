package com.example.pizzarestaurantproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.helper.DataBaseHelper;
import com.example.pizzarestaurantproject.helper.InputValidator;
import com.example.pizzarestaurantproject.helper.PasswordHashing;
import com.example.pizzarestaurantproject.helper.SharedPrefManager;
import com.example.pizzarestaurantproject.models.User;

import java.security.NoSuchAlgorithmException;


public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String GREEN = "#4CAF50";
    private static final String RED = "#FFFF0057";
    private static final int PICK_IMAGE = 1;

    private ImageView imageViewProfilePicture;
    private EditText editTextFirstName, editTextLastName, editTextPassword, editTextPhoneNumber,
            editTextEmailAddress, editTextConfirmPassword;
    private Button buttonUpdateProfile, buttonDiscardChanges;

    private String originalProfilePicture;
    private String oldSalt;

    private final User originalUser = new User();

    private boolean isChanged = false;
    private boolean listenersEnabled;


    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);

        editTextEmailAddress = view.findViewById(R.id.editTextEmail);
        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextPasswordConfirm);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);

        buttonUpdateProfile = view.findViewById(R.id.buttonUpdateProfile);
        buttonDiscardChanges = view.findViewById(R.id.discardButton);

        buttonUpdateProfile.setEnabled(false);
        buttonDiscardChanges.setEnabled(false);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (listenersEnabled) {

                    if (!buttonUpdateProfile.isEnabled())
                        buttonUpdateProfile.setEnabled(true);

                    if (!buttonDiscardChanges.isEnabled())
                        buttonDiscardChanges.setEnabled(true);

                    isChanged = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        editTextFirstName.addTextChangedListener(watcher);
        editTextLastName.addTextChangedListener(watcher);
        editTextPhoneNumber.addTextChangedListener(watcher);
        editTextPassword.addTextChangedListener(watcher);
        editTextConfirmPassword.addTextChangedListener(watcher);

        buttonDiscardChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFirstName.setText(originalUser.getFirstName());
                editTextLastName.setText(originalUser.getLastName());
                editTextPassword.setText("");
                editTextConfirmPassword.setText("");
                editTextPhoneNumber.setText(originalUser.getPhoneNumber());

                buttonUpdateProfile.setEnabled(false);
                buttonDiscardChanges.setEnabled(false);

                changeColor(editTextFirstName, Color.parseColor(GREEN));
                changeColor(editTextLastName, Color.parseColor(GREEN));
                changeColor(editTextPhoneNumber, Color.parseColor(GREEN));
                changeColor(editTextPassword, Color.parseColor(GREEN));
                changeColor(editTextConfirmPassword, Color.parseColor(GREEN));

                isChanged = false;
                originalUser.setProfilePicturePath(originalProfilePicture);

                setImageViewFromUri(Uri.parse(originalProfilePicture));
            }
        });

        imageViewProfilePicture.setOnClickListener(v -> openGallery());
        buttonUpdateProfile.setOnClickListener(v -> updateProfile());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        listenersEnabled = false;

        editTextEmailAddress.setText(originalUser.getEmail());
        editTextFirstName.setText(originalUser.getFirstName());
        editTextLastName.setText(originalUser.getLastName());
        editTextPhoneNumber.setText(originalUser.getPhoneNumber());

        editTextPassword.setText("");
        editTextConfirmPassword.setText("");

        listenersEnabled = true;

        Log.d("Profile Picture Path After onResume", originalProfilePicture);

        setImageViewFromUri(Uri.parse(originalUser.getProfilePicturePath()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(requireContext());
        String email = sharedPrefManager.readString("email", "No Val");

        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                requireContext(),
                "PIZZA_RESTAURANT",
                null, 1
        );

        Cursor cursor = dataBaseHelper.getUser(email);

        if (cursor != null && cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex("FIRST_NAME");
            int lastNameIndex = cursor.getColumnIndex("LAST_NAME");
            int phoneIndex = cursor.getColumnIndex("PHONE");
            int profilePictureIndex = cursor.getColumnIndex("PROFILE_PIC");
            int passwordIndex = cursor.getColumnIndex("PASSWORD");
            int saltIndex = cursor.getColumnIndex("SALT");
            int genderIndex = cursor.getColumnIndex("GENDER");
            int adminIndex = cursor.getColumnIndex("IS_ADMIN");

            listenersEnabled = false;

            originalProfilePicture = cursor.getString(profilePictureIndex);

            if (originalUser.getProfilePicturePath() == null)
                originalUser.setProfilePicturePath(originalProfilePicture);

            originalUser.setEmail(email);
            originalUser.setPassword(cursor.getString(passwordIndex));
            originalUser.setFirstName(cursor.getString(firstNameIndex));
            originalUser.setLastName(cursor.getString(lastNameIndex));
            originalUser.setPhoneNumber(cursor.getString(phoneIndex));
            originalUser.setGender(cursor.getString(genderIndex));
            originalUser.setAdmin( cursor.getInt(adminIndex) == 1 );

            Log.d("ADMIN", String.valueOf(originalUser.isAdmin()));


            oldSalt = cursor.getString(saltIndex);
        }
        dataBaseHelper.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        buttonUpdateProfile.setEnabled(false);
        buttonDiscardChanges.setEnabled(false);

        if (isChanged)
            Toast.makeText(getActivity(), "Changes were Discarded", Toast.LENGTH_SHORT).show();

        isChanged = false;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            setImageViewFromUri(selectedImageUri);

            assert selectedImageUri != null;

            originalUser.setProfilePicturePath(selectedImageUri.toString());
            Log.d("Profile Picture Path After Read", originalUser.getProfilePicturePath());
            isChanged = true;

            buttonDiscardChanges.setEnabled(true);
            buttonUpdateProfile.setEnabled(true);
        }
    }

    private void setImageViewFromUri(Uri imageUri) {

        if (imageUri != null) {
                Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image while loading
                        .error(R.drawable.ic_launcher_background) // Image for loading errors
                        .into(imageViewProfilePicture);
            } else {
                // Set a default image if no profile picture path is found
                imageViewProfilePicture.setImageResource(R.drawable.ic_launcher_foreground);
            }
    }

    private void updateProfile() {

        if (!isChanged)
            return;

        String email = editTextEmailAddress.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();

        boolean invalidInputs = false;
        boolean validPassword = false;

        if (!InputValidator.validName(firstName)) {
            changeColor(editTextFirstName, Color.parseColor(RED));
            invalidInputs = true;

        } else {
            changeColor(editTextFirstName, Color.parseColor(GREEN));
        }

        if (!InputValidator.validName(lastName)) {
            changeColor(editTextLastName, Color.parseColor(RED));
            invalidInputs = true;

        } else {
            changeColor(editTextLastName, Color.parseColor(GREEN));
        }

        if (InputValidator.invalidPassword(password) && !password.isEmpty()) {
            changeColor(editTextPassword, Color.parseColor(RED));
            invalidInputs = true;

        } else {
            changeColor(editTextPassword, Color.parseColor(GREEN));
            validPassword = true;
        }

        if (InputValidator.invalidPassword(password, confirmPassword)) {
            changeColor(editTextConfirmPassword, Color.parseColor(RED));
            invalidInputs = true;

        } else {
            changeColor(editTextConfirmPassword, Color.parseColor(GREEN));
        }

        if (InputValidator.invalidPhoneNumber(phoneNumber)) {
            changeColor(editTextPhoneNumber, Color.parseColor(RED));
            invalidInputs = true;

        } else {
            changeColor(editTextPhoneNumber, Color.parseColor(GREEN));
        }

        String currentPassword = null;

        // check if the old password is equal to the new one
        try {
            currentPassword = PasswordHashing.hashPassword(password, oldSalt);

            if (currentPassword.equals(originalUser.getPassword())) {
                changeColor(editTextPassword, Color.parseColor(RED));
                invalidInputs = true;

            } else if (validPassword) {
                changeColor(editTextPassword, Color.parseColor(GREEN));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        String toastMessage;

        if (!invalidInputs) {

            DataBaseHelper dataBaseHelper = new DataBaseHelper(
                    requireContext(),
                    "PIZZA_RESTAURANT",
                    null, 1
            );

            originalUser.setFirstName(firstName);
            originalUser.setLastName(lastName);
            originalUser.setPhoneNumber(phoneNumber);
            Log.d("Profile Picture Path in Update", originalUser.getProfilePicturePath());

            if (!password.isEmpty())
                originalUser.setPassword(currentPassword);

            dataBaseHelper.updateUser(originalUser);

            dataBaseHelper.close();

            toastMessage = "Profile updated successfully";

            buttonUpdateProfile.setEnabled(false);
            buttonDiscardChanges.setEnabled(false);
            isChanged = false;

        } else if (currentPassword.equals(originalUser.getPassword())) {
            toastMessage = "New password can't be the same as old one!";
        } else {
            toastMessage = "Please Check your inputs, and try again!";
        }

        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    public void changeColor(View view, int color) {

        view.setBackgroundTintList(ColorStateList.valueOf(color));
    }

} // end Fragment