package com.fossasia.unesco.popular.authentication;


import android.app.Activity;
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

public class LoginFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;

    private String userEmail;
    private String userPassword;

    private SQLiteDatabase sqLiteDatabase;
    private Preferences preferences;
    private Context context;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = getActivity();
    } // ... To avoid null pointer exception and get context and activity

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        preferences = new Preferences(context);

        AuthDbHelper dbHelper = new AuthDbHelper(getContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();

        Button loginButton = rootView.findViewById(R.id.login_button);
        emailEditText = rootView.findViewById(R.id.email_edit_text);
        passwordEditText = rootView.findViewById(R.id.password_edit_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.hideSoftKeyboard(activity);
                userEmail = emailEditText.getText().toString().trim();
                userPassword = passwordEditText.getText().toString().trim();

                if (userEmail.isEmpty()) {
                    emailEditText.setError("Please enter email");
                } else if (userPassword.isEmpty()) {
                    passwordEditText.setError("Please enter password");
                } else {
                    checkUser();
                }
            }
        });

        return rootView;
    }

    private void checkUser() {
        String [] projection = {AuthEntry.COLUMN_EMAIL, AuthEntry.COLUMN_PASSWORD, AuthEntry.COLUMN_NAME};
        String selection = AuthEntry.COLUMN_EMAIL + " = '" + userEmail +
                "' AND " + AuthEntry.COLUMN_PASSWORD + " = '" + userPassword + "'";
        Cursor cursor = sqLiteDatabase.query(AuthEntry.TABLE_NAME, projection, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            String currentUserName = cursor.getString(cursor.getColumnIndex(AuthEntry.COLUMN_NAME));
            Toast.makeText(getContext(), "Logged in!", Toast.LENGTH_SHORT).show();
            preferences.createLoginSession(currentUserName, userEmail);
            directToDashboard();
        } else {
            Toast.makeText(getContext(), "Unable to Login. Please check your credentials", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    } // ... Match details from database and direct user to main activity

    private void directToDashboard() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AuthContract.REDIRECTED_FROM_AUTH, true);
        startActivity(intent);
        activity.finish();
    } // ... Direct user to favorites screed

}
