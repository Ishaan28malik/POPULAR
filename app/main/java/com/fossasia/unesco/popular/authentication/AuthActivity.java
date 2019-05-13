package com.fossasia.unesco.popular.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.fossasia.unesco.popular.MainActivity;
import com.fossasia.unesco.popular.R;

public class AuthActivity extends AppCompatActivity {
    BottomNavigationView navigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.navigation_login:
                    setTitle(getResources().getString(R.string.login));
                    fragment = new LoginFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_sign_up:
                    setTitle(getResources().getString(R.string.sign_up));
                    fragment = new SignUpFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    }; // ... Listener for bottom navigation view

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container_auth, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        navigationView = findViewById(R.id.navigation_auth);
        navigationView.setOnNavigationItemSelectedListener(listener);

        loadFragment(new LoginFragment());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
