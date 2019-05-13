package com.fossasia.unesco.popular;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fossasia.unesco.popular.authentication.AuthActivity;

public class ProfileFragment extends Fragment {
    Preferences preferences;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    } // ... To avoid null pointer exception and get context

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        preferences = new Preferences(context);

        TextView nameTextView = rootView.findViewById(R.id.name_text_view);
        TextView emailTextView = rootView.findViewById(R.id.email_text_view);

        if (!preferences.isLoggedIn()) {
            Toast.makeText(getContext(), "Please login to continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), AuthActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            nameTextView.setText(preferences.getCurrentUserName());
            emailTextView.setText(preferences.getCurrentUserEmail());
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                preferences.logoutUser();
                redirectToMain();
        }
        return true;
    }

    private void redirectToMain() {
        startActivity(new Intent(getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
