package com.example.ergasia2;

import android.provider.BaseColumns;

public class OverspeedingDbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private OverspeedingDbContract() {}
    // Inner class that defines the table contents
    public static class OverspeedingEntry implements BaseColumns {
        public static final String TABLE_NAME = "overspeedingDetails";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_SPEED = "speed";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }

    public static class LimitEntry implements BaseColumns{
        public static final String TABLE_NAME2 = "overspeedingLimit";
        public static final String COLUMN_NAME_LIMIT = "speedLimit";
    }
}