package com.example.mylingual.data;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mylingual.HomeFragment;

public class RecentData extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "recent_database";
        public static final String TABLE_NAME = "recent translations";
        public static final String PRIMARY_LANG = "_tag";
        public static final String ORIGINAL_TEXT = "name";
        public static final String SECONDARY_LANG = "age";
        public static final String TRANSLATED_TEXT = "gender";

        public RecentData(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT)", TABLE_NAME, PRIMARY_LANG, ORIGINAL_TEXT, SECONDARY_LANG, TRANSLATED_TEXT));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int old, int newer) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int old, int newer) {
            onUpgrade(db, old, newer);
        }


}

