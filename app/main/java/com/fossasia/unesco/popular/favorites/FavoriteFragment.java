package com.fossasia.unesco.popular.favorites;


import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fossasia.unesco.popular.CommunityCursorAdapter;
import com.fossasia.unesco.popular.Preferences;
import com.fossasia.unesco.popular.R;
import com.fossasia.unesco.popular.authentication.AuthActivity;
import com.fossasia.unesco.popular.favorites.data.FavoriteContract.DashboardEntry;
import com.fossasia.unesco.popular.favorites.data.FavoriteDbHelper;
import com.fossasia.unesco.popular.communities.data.CommunityContract;
import com.fossasia.unesco.popular.communities.data.CommunityContract.WorkshopEntry;
import com.fossasia.unesco.popular.communities.data.CommunityDbHelper;

/**
 * Displays list of applied popular.
 */
public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int PRODUCT_LOADER_DASHBOARD = 1;
    private Preferences preferences;
    private SQLiteDatabase workshopDatabase;
    private SQLiteDatabase dashboardDatabase;
    private CommunityCursorAdapter adapter;

    private TextView emptyTextView;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    } // ... To avoid null pointer exception and get context

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workshop_dashboard, container, false);

        emptyTextView = rootView.findViewById(R.id.empty_text_view);
        preferences = new Preferences(rootView.getContext());

        CommunityDbHelper communityDbHelper = new CommunityDbHelper(rootView.getContext());
        workshopDatabase = communityDbHelper.getReadableDatabase();

        FavoriteDbHelper favoriteDbHelper = new FavoriteDbHelper(rootView.getContext());
        dashboardDatabase = favoriteDbHelper.getReadableDatabase();

        ListView workshopListView = rootView.findViewById(R.id.workshop_list);
        adapter = new CommunityCursorAdapter(getContext(), null);
        workshopListView.setAdapter(adapter);

        if (!preferences.isLoggedIn()) {
            Toast.makeText(getContext(), "Please login to continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), AuthActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            getLoaderManager().initLoader(PRODUCT_LOADER_DASHBOARD, null, this);
        }

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        final String workshopSelection = getWorkshopSelection();

        final String[] workshopProjection = {CommunityContract.WorkshopEntry._ID,
                WorkshopEntry.COLUMN_IMAGE,
                WorkshopEntry.COLUMN_NAME,
                WorkshopEntry.COLUMN_TITLE,
                WorkshopEntry.COLUMN_DESCRIPTION,
                WorkshopEntry.COLUMN_URL};

        return new CursorLoader(context, CommunityContract.BASE_CONTENT_URI, workshopProjection, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                return workshopDatabase.query(WorkshopEntry.TABLE_NAME, workshopProjection, workshopSelection, null, null, null, null);
            }
        };
    } // ... Execute the query method on a background thread

    private String getWorkshopSelection() {
        String[] dashboardProjection = {DashboardEntry.COLUMN_WORKSHOP_ID};
        String dashboardSelection = DashboardEntry.COLUMN_EMAIL + " = '" + preferences.getCurrentUserEmail() + "'";

        Cursor dashboardCursor = dashboardDatabase.query(DashboardEntry.TABLE_NAME, dashboardProjection, dashboardSelection, null, null, null, null);

        StringBuilder workshopSelectionBuilder = new StringBuilder();
        while (dashboardCursor.moveToNext()) {
            workshopSelectionBuilder.append(WorkshopEntry._ID + " = ")
                    .append(dashboardCursor.getInt(dashboardCursor.getColumnIndex(DashboardEntry.COLUMN_WORKSHOP_ID)))
                    .append(" OR ");
        }
        dashboardCursor.close();
        String workshopSelection = String.valueOf(workshopSelectionBuilder);
        if (workshopSelection.isEmpty()) workshopSelection = WorkshopEntry._ID + " = " + 0;
        else workshopSelection = workshopSelection.substring(0, workshopSelection.length() - 3);

        return workshopSelection;
    } // ... Select the communities id for which current user is registered

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
        handleEmptyTextView(cursor);
    } // ...  Update with this new cursor containing updated communities data

    private void handleEmptyTextView(Cursor cursor) {
        if (cursor.moveToFirst()) emptyTextView.setVisibility(View.GONE);
        else emptyTextView.setVisibility(View.VISIBLE);
    } // ... Set empty massage if user not applied for any communities

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    } // ...  Called when the data needs to be deleted
}
