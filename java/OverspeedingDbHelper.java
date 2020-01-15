package com.example.ergasia2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OverspeedingDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "OverspeedingDB.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_OVERSPEEDING_POINTS_TABLE =
            "CREATE TABLE " + OverspeedingDbContract.OverspeedingEntry.TABLE_NAME + " (" +
                    OverspeedingDbContract.OverspeedingEntry._ID + " INTEGER PRIMARY KEY," +
                    OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP +
                    OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEP +
                    OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_SPEED + TEXT_TYPE + COMMA_SEP +
                    OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_TIMESTAMP + TEXT_TYPE + " )";

    private static final String SQL_CREATE_OVERSPEEDING_LIMIT_TABLE =
            "CREATE TABLE " + OverspeedingDbContract.LimitEntry.TABLE_NAME2 + " (" +
                    OverspeedingDbContract.LimitEntry._ID + " INTEGER PRIMARY KEY," +
                    OverspeedingDbContract.LimitEntry.COLUMN_NAME_LIMIT + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + OverspeedingDbContract.OverspeedingEntry.TABLE_NAME;

    public OverspeedingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_OVERSPEEDING_POINTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_OVERSPEEDING_LIMIT_TABLE);

        ContentValues values = new ContentValues();
        values.put(OverspeedingDbContract.LimitEntry.COLUMN_NAME_LIMIT, "80");
        sqLiteDatabase.insert(OverspeedingDbContract.LimitEntry.TABLE_NAME2,null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}