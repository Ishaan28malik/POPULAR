package com.fossasia.unesco.popular.authentication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fossasia.unesco.popular.authentication.data.AuthContract.AuthEntry;

/**
 * Database helper for users table. Manages database creation and version management.
 */
public class AuthDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;

    public AuthDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCT_TABLE = " CREATE TABLE IF NOT EXISTS " + AuthEntry.TABLE_NAME + "(" +
                AuthEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AuthEntry.COLUMN_NAME + " TEXT NOT NULL," +
                AuthEntry.COLUMN_EMAIL + " TEXT NOT NULL," +
                AuthEntry.COLUMN_PASSWORD + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    } // ... Create users table ig not exists.

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    } // ... Empty because no need to upgrade database
}
