package com.fossasia.unesco.popular;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fossasia.unesco.popular.authentication.AuthActivity;
import com.fossasia.unesco.popular.authentication.data.AuthContract.AuthEntry;
import com.fossasia.unesco.popular.communities.data.CommunityContract.WorkshopEntry;
import com.fossasia.unesco.popular.favorites.data.FavoriteContract.DashboardEntry;
import com.fossasia.unesco.popular.favorites.data.FavoriteDbHelper;

/**
 * CommunityCursorAdapter class is adapter for the communities list view
 * that uses cursor of the communities data as its data source
 * and create list items for each row of communities.
 */
public class CommunityCursorAdapter extends android.widget.CursorAdapter {
    private Preferences preferences;
    private SQLiteDatabase dashboardDatabase;

    public CommunityCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        preferences = new Preferences(view.getContext());
        FavoriteDbHelper favoriteDbHelper = new FavoriteDbHelper(view.getContext());
        dashboardDatabase = favoriteDbHelper.getWritableDatabase();

        ImageView imageView = view.findViewById(R.id.list_item_image_view);
        TextView nameTextView = view.findViewById(R.id.name_text_view);
        TextView titleTextView = view.findViewById(R.id.title_text_view);
        final TextView appliedButton = view.findViewById(R.id.applied_button);
        ImageView infoButton = view.findViewById(R.id.info_button);

        final int _ID = cursor.getInt(cursor.getColumnIndex(AuthEntry._ID));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(WorkshopEntry.COLUMN_IMAGE));
        final Bitmap workshopImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        final String workshopName = cursor.getString(cursor.getColumnIndex(WorkshopEntry.COLUMN_NAME));
        final String workshopTitle = cursor.getString(cursor.getColumnIndex(WorkshopEntry.COLUMN_TITLE));
        final String workshopDescription = cursor.getString(cursor.getColumnIndex(WorkshopEntry.COLUMN_DESCRIPTION));
        final String workshopUrl = cursor.getString(cursor.getColumnIndex(WorkshopEntry.COLUMN_URL));

        imageView.setImageBitmap(workshopImage);
        nameTextView.setText(workshopName);
        titleTextView.setText(workshopTitle);

        handleAppliedVisibility(isAlreadyApplied(_ID), appliedButton);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showApplyAlertDialog(view, context, _ID, workshopName, workshopDescription, workshopUrl, appliedButton);
            }
        });

        appliedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAbandonedAlertDialog(context, _ID, appliedButton);
            }
        });
    }

    private void handleAppliedVisibility(boolean alreadyApplied, TextView appliedButton) {
        if (alreadyApplied) appliedButton.setVisibility(View.VISIBLE);
        else appliedButton.setVisibility(View.GONE);
    } // ... Handel visibility for applied text view

    private void showApplyAlertDialog(final View view, final Context context,
                                      final int _ID, final String workshopName,
                                      final String workshopDescription,
                                      final String workshopUrl,
                                      final TextView appliedButton) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(workshopName);
        builder.setMessage(workshopDescription);
        builder.setIcon(R.drawable.ic_info);

        builder.setNeutralButton("MORE INFO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri webpage = Uri.parse(workshopUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        if (!isAlreadyApplied(_ID)) {
            builder.setPositiveButton("Add to favorites", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (preferences.isLoggedIn()) {
                        handleAppliedVisibility(true, appliedButton);
                        insertIntoDashboard(context, _ID, workshopName);
                    } else {
                        Toast.makeText(context, "Please login to continue", Toast.LENGTH_SHORT).show();
                        view.getContext().startActivity(new Intent(view.getContext(), AuthActivity.class));
                    }
                }
            });
        } // .. Set apply button on dialog if user not applied for the communities

        AlertDialog dialog = builder.create();
        dialog.show();
    } // ... Show dialog for communities information

    private void showAbandonedAlertDialog(Context context, final int _ID, final TextView appliedButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to want to abandoned this communities?");
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selection = DashboardEntry.COLUMN_WORKSHOP_ID + " = " + _ID + " AND " +
                        DashboardEntry.COLUMN_EMAIL + " = '" + preferences.getCurrentUserEmail() + "'";
                dashboardDatabase.delete(DashboardEntry.TABLE_NAME, selection, null);
                handleAppliedVisibility(false, appliedButton);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    } // ... Alert dialog for abandoned the communities

    private boolean isAlreadyApplied(int _ID) {
        String[] projection = {DashboardEntry.COLUMN_EMAIL, DashboardEntry.COLUMN_WORKSHOP_ID};
        String selection = DashboardEntry.COLUMN_EMAIL + " = '" + preferences.getCurrentUserEmail() + "' AND " +
                DashboardEntry.COLUMN_WORKSHOP_ID + " = " + _ID;
        Cursor dashboardCursor = dashboardDatabase.query(DashboardEntry.TABLE_NAME, projection, selection, null, null, null, null);
        Boolean applied = false;
        if (dashboardCursor.moveToFirst()) {
            applied = true;
        }
        dashboardCursor.close();
        return applied;
    } // ... Check if user is already applied for the communities

    private void insertIntoDashboard(Context context, int _ID, String workshopName) {
        String currentUserEmail = preferences.getCurrentUserEmail();
        ContentValues values = new ContentValues();
        values.put(DashboardEntry.COLUMN_EMAIL, currentUserEmail);
        values.put(DashboardEntry.COLUMN_WORKSHOP_ID, _ID);
        dashboardDatabase.insert(DashboardEntry.TABLE_NAME, null, values);
        Toast.makeText(context, "Successfully applied for: " + workshopName, Toast.LENGTH_SHORT).show();
    } // ... Insert user email with applied communities id into favorites table

}
