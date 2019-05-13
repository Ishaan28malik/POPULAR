package com.fossasia.unesco.popular.authentication.data;

import android.provider.BaseColumns;

/**
 * Contract for the users table.
 */
public class AuthContract {
    public static final String REDIRECTED_FROM_AUTH = "redirectedFromAuth"; //Key for intent after authentication

    private AuthContract() {
    }

    /**
     * This class defines constant values for the users database table.
     * Each entry in the table represents a single applied user details.
     */
    public static final class AuthEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_EMAIL = "user_email";
        public static final String COLUMN_NAME = "user_name";
        public static final String COLUMN_PASSWORD = "user_password";
    }
}