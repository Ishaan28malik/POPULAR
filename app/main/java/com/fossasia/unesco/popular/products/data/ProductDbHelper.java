package com.fossasia.unesco.popular.products.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "product.db";
    private static final int DATABASE_VERSION = 4;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCT_TABLE = " CREATE TABLE IF NOT EXISTS " + ProductContract.ProductEntry.TABLE_NAME + "(" +
                ProductContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProductContract.ProductEntry.PRODUCT_IMAGE + " BLOB NOT NULL," +
                ProductContract.ProductEntry.PRODUCT_NAME + " TEXT NOT NULL," +
                ProductContract.ProductEntry.PRODUCT_PRICE + " TEXT NOT NULL," +
                ProductContract.ProductEntry.PRODUCT_DESCRIPTION + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
