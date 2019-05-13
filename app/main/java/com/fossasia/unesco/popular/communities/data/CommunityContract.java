package com.fossasia.unesco.popular.communities.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for the popular table.
 */
public class CommunityContract {
    private static final String CONTENT_AUTHORITY = "com.fossasia.unesco.popular";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private CommunityContract() {
    }

    /**
     * This class defines constant values for the communities database table.
     * Each entry in the table represents a single communities details.
     */
    public static final class WorkshopEntry implements BaseColumns {
        public static final String TABLE_NAME = "popular";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_IMAGE = "workshop_image";
        public static final String COLUMN_NAME = "workshop_name";
        public static final String COLUMN_TITLE = "workshop_title";
        public static final String COLUMN_DESCRIPTION = "workshop_description";
        public static final String COLUMN_URL = "workshop_url";
    }
}
