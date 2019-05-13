package com.fossasia.unesco.popular.authentication;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fossasia.unesco.popular.MainActivity;
import com.fossasia.unesco.popular.Preferences;
import com.fossasia.unesco.popular.R;
import com.fossasia.unesco.popular.Utilities;
import com.fossasia.unesco.popular.authentication.data.AuthContract;
import com.fossasia.unesco.popular.authentication.data.AuthContract.AuthEntry;
import com.fossasia.unesco.popular.authentication.data.AuthDbHelper;

public class SignUpFragment extends Fragment {

    private SQLiteDatabase sqLiteDatabase;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    private String userEmail;
    private String userPassword;
    private String userConfirmPassword;
    private String userFirstName;
    private String userLastName;

    private Preferences preferences;
    private Context context;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = getActivity();
    } // ... To avoid null pointer exception and get context

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        preferences = new Preferences(context);

        AuthDbHelper dbHelper = new AuthDbHelper(getContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();

        Button signUpButton = rootView.findViewById(R.id.signUp_button);
        emailEditText = rootView.findViewById(R.id.email_edit_text);
        passwordEditText = rootView.findViewById(R.id.password_edit_text);
        confirmPasswordEditText = rootView.findViewById(R.id.confirmPassword_edit_text);
        firstNameEditText = rootView.findViewById(R.id.firstName_edit_text);
        lastNameEditText = rootView.findViewById(R.id.lastName_edit_text);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.hideSoftKeyboard(activity);
                userEmail = emailEditText.getText().toString().trim();
                userPassword = passwordEditText.getText().toString().trim();
                userConfirmPassword = confirmPasswordEditText.getText().toString().trim();
                userFirstName = firstNameEditText.getText().toString().trim();
                userLastName = lastNameEditText.getText().toString().trim();

                if(verifyDetails()) {
                    if(isUserAlreadyExists()) {
                        Toast.makeText(getContext(), "Email already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        registerUser();
                        Toast.makeText(getContext(), "Signed up", Toast.LENGTH_SHORT).show();
                        redirectToDashboard();
                    }
                }

            }
        });

        return rootView;
    }

    private boolean verifyDetails() {
        Boolean isVerified = true;

        //For check empty field
        if (userEmail.equals("")) {
            emailEditText.setError("Enter email");
            isVerified = false;
        }
        if (userPassword.equals("")) {
            passwordEditText.setError("Enter password");
            isVerified = false;
        }
        if (userConfirmPassword.equals("")) {
            confirmPasswordEditText.setError("Enter confirm password");
            isVerified = false;
        }
        if (userFirstName.equals("")) {
            firstNameEditText.setError("Enter first name");
            isVerified = false;
        }
        if (userLastName.equals("")) {
            lastNameEditText.setError("Enter last name");
            isVerified = false;
        }

        if (!userConfirmPassword.equals(userPassword)) {
            confirmPasswordEditText.setError("Password not match");
            isVerified = false;
        }

        if (userPassword.length() < 6) {
            passwordEditText.setError("Enter minimum six character");
            isVerified = false;
        }

        return isVerified;
    } // ... Verify data about empty field and password constrains

    private boolean isUserAlreadyExists() {
        String [] projection = {AuthEntry.COLUMN_EMAIL};
        String selection = AuthEntry.COLUMN_EMAIL + " = '" + userEmail + "'";
        Cursor cursor = sqLiteDatabase.query(AuthEntry.TABLE_NAME, projection, selection, null,null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    } // ... Check if user already register

    private void registerUser() {
        ContentValues values = new ContentValues();
        values.put(AuthEntry.COLUMN_NAME, userFirstName + " " + userLastName);
        values.put(AuthEntry.COLUMN_EMAIL, userEmail);
        values.put(AuthEntry.COLUMN_PASSWORD, userPassword);
        sqLiteDatabase.insert(AuthEntry.TABLE_NAME, null, values);

        preferences.createLoginSession(userFirstName + " " + userLastName, userEmail);
    } // ... Insert user into database

    private void redirectToDashboard() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AuthContract.REDIRECTED_FROM_AUTH, true);
        startActivity(intent);
        activity.finish();
    } // ... Redirect user to favorites screen

}
