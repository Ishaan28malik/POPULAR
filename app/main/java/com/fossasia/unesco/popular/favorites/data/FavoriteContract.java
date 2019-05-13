package com.fossasia.unesco.popular.favorites.data;

import android.provider.BaseColumns;

/**
 * Contract for the favorites table.
 */
public class FavoriteContract {

    private FavoriteContract() { }

    /**
     * This class defines constant values for the favorites database table.
     * Each entry in the table represents a single applied communities details.
     */
    public static final class DashboardEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_EMAIL = "user_email";
        public static final String COLUMN_WORKSHOP_ID = "workshop_id";
    }
}