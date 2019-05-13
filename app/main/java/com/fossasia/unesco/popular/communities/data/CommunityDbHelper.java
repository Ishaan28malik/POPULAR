package com.fossasia.unesco.popular.communities.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fossasia.unesco.popular.communities.data.CommunityContract.WorkshopEntry;

/**
 * Database helper for popular table. Manages database creation and version management.
 */
public class CommunityDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popular.db";
    private static final int DATABASE_VERSION = 1;

    public CommunityDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCT_TABLE = " CREATE TABLE IF NOT EXISTS " + WorkshopEntry.TABLE_NAME + "(" +
                WorkshopEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WorkshopEntry.COLUMN_IMAGE + " BLOB NOT NULL," +
                WorkshopEntry.COLUMN_NAME + " TEXT NOT NULL," +
                WorkshopEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                WorkshopEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                WorkshopEntry.COLUMN_URL + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    } // ... Create popular table if not exists

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    } // ... Empty because no need to upgrade database
}
