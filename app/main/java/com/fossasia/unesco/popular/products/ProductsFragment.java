package com.fossasia.unesco.popular.products;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fossasia.unesco.popular.Preferences;
import com.fossasia.unesco.popular.R;
import com.fossasia.unesco.popular.authentication.AuthActivity;
import com.fossasia.unesco.popular.products.data.ProductContract;
import com.fossasia.unesco.popular.products.data.ProductDbHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int PRODUCT_LOADER = 0;
    private ProductCursorAdapter adapter;
    private ProductDbHelper productDbHelper;
    private Context context;
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    } // ... To avoid null pointer exception and get context

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_products, container, false);

        productDbHelper = new ProductDbHelper(getContext());

        ListView productListView = rootView.findViewById(R.id.product_list_view);
        adapter = new ProductCursorAdapter(getContext(), null);
        productListView.setAdapter(adapter);


        Preferences preferences = new Preferences(rootView.getContext());
        if (!preferences.isLoggedIn()) {
            Toast.makeText(getContext(), "Please login to continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), AuthActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        }

        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        final String[] projection = {ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.PRODUCT_IMAGE,
                ProductContract.ProductEntry.PRODUCT_NAME,
                ProductContract.ProductEntry.PRODUCT_PRICE,
                ProductContract.ProductEntry.PRODUCT_DESCRIPTION};
        return new CursorLoader(context, ProductContract.BASE_CONTENT_URI, projection, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                SQLiteDatabase sqLiteDatabase = productDbHelper.getReadableDatabase();
                return sqLiteDatabase.query(ProductContract.ProductEntry.TABLE_NAME, projection, null, null, null, null, null);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
