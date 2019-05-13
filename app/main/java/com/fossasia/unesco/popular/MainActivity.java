package com.fossasia.unesco.popular;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.fossasia.unesco.popular.authentication.data.AuthContract;
import com.fossasia.unesco.popular.communities.CommunityFragment;
import com.fossasia.unesco.popular.communities.data.CommunityContract.WorkshopEntry;
import com.fossasia.unesco.popular.communities.data.CommunityDbHelper;
import com.fossasia.unesco.popular.favorites.FavoriteFragment;
import com.fossasia.unesco.popular.products.ProductsFragment;
import com.fossasia.unesco.popular.products.data.ProductContract.ProductEntry;
import com.fossasia.unesco.popular.products.data.ProductDbHelper;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private static final int numberOfWorkshops = 3;
    private SQLiteDatabase database;
    private SQLiteDatabase database2;
    private BottomNavigationView navigationView;
    private Boolean isBackPressed = false;

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.navigation_communities:
                    setTitle(getResources().getString(R.string.workshops));
                    fragment = new CommunityFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_favorites:
                    setTitle("Favorites communities");
                    fragment = new FavoriteFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_profile:
                    setTitle(getResources().getString(R.string.profile));
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_products:
                    setTitle("Products");
                    fragment = new ProductsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    }; // ... Listener for bottom navigation view

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommunityDbHelper communityDbHelper = new CommunityDbHelper(this);
        database = communityDbHelper.getReadableDatabase();

        ProductDbHelper productDbHelper = new ProductDbHelper(this);
        database2 = productDbHelper.getWritableDatabase();

        if (!isDataAlreadyInserted()) insetData();

        //insetData();

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(listener);

        Intent intent = getIntent();
        if (intent.getBooleanExtra(AuthContract.REDIRECTED_FROM_AUTH, false)) {
            loadFragment(new FavoriteFragment());
            navigationView.setSelectedItemId(R.id.navigation_communities);
        } else loadFragment(new CommunityFragment());
    }

    private boolean isDataAlreadyInserted() {
        boolean alreadyInserted = true;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + WorkshopEntry.TABLE_NAME, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 0) alreadyInserted = false;
        cursor.close();
        return alreadyInserted;
    } // ... Check if data is already inserted in database

    private void insetData() {

        for (int i = 1; i <= numberOfWorkshops; i++) {

            Drawable drawable = Utilities.getResourcesDrawable("ic_workshop" + i, this);
            Bitmap imageBitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap reducedBitmap = Utilities.getResizedBitmap(imageBitmap, 1024);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            reducedBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] image = stream.toByteArray();

            ContentValues values = new ContentValues();
            values.put(WorkshopEntry.COLUMN_IMAGE, image);
            values.put(WorkshopEntry.COLUMN_NAME, Utilities.getResourcesString("name_workshop" + i, this));
            values.put(WorkshopEntry.COLUMN_TITLE, Utilities.getResourcesString("title_workshop" + i, this));
            values.put(WorkshopEntry.COLUMN_DESCRIPTION, Utilities.getResourcesString("description_workshop" + i, this));
            values.put(WorkshopEntry.COLUMN_URL, Utilities.getResourcesString("url_workshop" + i, this));

            database.insert(WorkshopEntry.TABLE_NAME, null, values);
        } // ... Insert hardcode communities data into database

        for (int i = 1; i <= 3; i++) {

            Drawable drawable = Utilities.getResourcesDrawable("ic_product" + i, this);
            Bitmap imageBitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap reducedBitmap = Utilities.getResizedBitmap(imageBitmap, 1024);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            reducedBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] image = stream.toByteArray();

            ContentValues values = new ContentValues();
            values.put(ProductEntry.PRODUCT_IMAGE, image);
            values.put(ProductEntry.PRODUCT_NAME, Utilities.getResourcesString("product_name" + i, this));
            values.put(ProductEntry.PRODUCT_DESCRIPTION, Utilities.getResourcesString("product_description" + i, this));
            values.put(ProductEntry.PRODUCT_PRICE, Utilities.getResourcesString("product_price" + i, this));

            database2.insert(ProductEntry.TABLE_NAME, null, values);
        } // ... Insert hardcode communities data into database
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (currentFragment instanceof CommunityFragment) {
            if (againBackPressed()) super.onBackPressed();
        } else {
            loadFragment(new CommunityFragment());
            navigationView.setSelectedItemId(R.id.navigation_communities);
        }
    } // ... Handle on back pressed for fragments

    private boolean againBackPressed() {
        if (isBackPressed) return true;
        this.isBackPressed = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
        return false;
    }
}
