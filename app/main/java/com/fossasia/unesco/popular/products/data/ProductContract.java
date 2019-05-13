package com.fossasia.unesco.popular.products.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContract {
    private static final String CONTENT_AUTHORITY = "com.fossasia.unesco.popular";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String _ID = BaseColumns._ID;
        public static final String PRODUCT_IMAGE = "product_image";
        public static final String PRODUCT_NAME = "product_name";
        public static final String PRODUCT_PRICE = "product_price";
        public static final String PRODUCT_DESCRIPTION = "product_description";
    }
}
