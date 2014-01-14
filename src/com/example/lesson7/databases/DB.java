package com.example.lesson7.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DB extends SQLiteOpenHelper {

    public DB(Context context, String dbName, Object o, int dbVersion) {
        super(context, dbName, (SQLiteDatabase.CursorFactory) o, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(RSSDatabase.DB_CREATE);
        sqLiteDatabase.execSQL(EntryDatabase.DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}