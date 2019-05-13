package com.fossasia.unesco.popular.communities;


import android.annotation.SuppressLint;
import android.content.Context;
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

import com.fossasia.unesco.popular.CommunityCursorAdapter;
import com.fossasia.unesco.popular.R;
import com.fossasia.unesco.popular.communities.data.CommunityContract;
import com.fossasia.unesco.popular.communities.data.CommunityDbHelper;

/**
 * Displays list of available popular.
 */
public class CommunityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int PRODUCT_LOADER = 0;
    private CommunityCursorAdapter adapter;
    private CommunityDbHelper communityDbHelper;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    } // ... To avoid null pointer exception and get context

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workshop_dashboard, container, false);

        communityDbHelper = new CommunityDbHelper(getContext());

        ListView workshopListView = rootView.findViewById(R.id.workshop_list);
        adapter = new CommunityCursorAdapter(getContext(), null);
        workshopListView.setAdapter(adapter);

        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        final String[] projection = {CommunityContract.WorkshopEntry._ID,
                CommunityContract.WorkshopEntry.COLUMN_IMAGE,
                CommunityContract.WorkshopEntry.COLUMN_NAME,
                CommunityContract.WorkshopEntry.COLUMN_TITLE,
                CommunityContract.WorkshopEntry.COLUMN_DESCRIPTION,
                CommunityContract.WorkshopEntry.COLUMN_URL};
        return new CursorLoader(context, CommunityContract.BASE_CONTENT_URI, projection, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                SQLiteDatabase sqLiteDatabase = communityDbHelper.getReadableDatabase();
                return sqLiteDatabase.query(CommunityContract.WorkshopEntry.TABLE_NAME, projection, null, null, null, null, null);
            }
        };

    } // ... Execute the query method on a background thread

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    } // ...  Update with this new cursor containing updated communities data

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    } // ...  Called when the data needs to be deleted
}
