package com.interview.crowdfireinterview.db;

import android.provider.BaseColumns;

public final class Contract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Contract() {
    }

    public static class ShirtPantEntry implements BaseColumns {
        public static final String TABLE_NAME = "cloth";
        static final String COLUMN_NAME_CLOTH_TYPE = "cloth_type";
        static final String COLUMN_NAME_CLOTH_IMAGE_URL = "cloth_image_url";
        static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }

    public static class FavEntry implements BaseColumns {
        public static final String TABLE_NAME = "fav_cloth";
        static final String COLUMN_NAME_SHIRT_ID = "shirt_id";
        static final String COLUMN_NAME_PANT_ID = "pant_id";
        static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
