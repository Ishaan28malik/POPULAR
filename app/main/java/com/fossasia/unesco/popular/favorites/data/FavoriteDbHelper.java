package com.fossasia.unesco.popular.favorites.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fossasia.unesco.popular.favorites.data.FavoriteContract.DashboardEntry;

/**
 * Database helper for favorites table. Manages database creation and version management.
 */
public class FavoriteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 3;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCT_TABLE = " CREATE TABLE IF NOT EXISTS " + DashboardEntry.TABLE_NAME + "(" +
                DashboardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DashboardEntry.COLUMN_EMAIL + " TEXT NOT NULL," +
                DashboardEntry.COLUMN_WORKSHOP_ID + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    } // ... Create favorites table if not exists

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    } // ... Empty because no need to upgrade database
}
