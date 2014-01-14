package com.example.lesson7.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: PWR
 * Date: 14.01.14
 * Time: 0:07
 * To change this template use File | Settings | File Templates.
 */
public class EntryDatabase {

    private static final String DB_NAME = "rssdatabase";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "entry_table";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CID = "cid";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_DATE = "date";

    public static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_CID + " integer, " +
                    COLUMN_TITLE + " text, " +
                    COLUMN_URL + " text, " +
                    COLUMN_DESC + " text, " +
                    COLUMN_DATE + " integer " +
                    ");";

    private final Context context;

    private DB mDBHelper;
    private SQLiteDatabase mDB;

    public EntryDatabase(Context context) {
        this.context = context;
    }

    public void open() {
        mDBHelper = new DB(context, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper != null)
            mDBHelper.close();
    }

    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public Cursor getChannelData(long channel) {
        return mDB.query(DB_TABLE, null, COLUMN_CID + " = " + channel, null, null, null, COLUMN_DATE + " DESC");
    }

    public void addEntry(long channel, String title, String url, String desc, String date) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_CID, channel);
        cv.put(COLUMN_URL, url);
        cv.put(COLUMN_DESC, desc);
        cv.put(COLUMN_DATE, date);
        mDB.insert(DB_TABLE, null, cv);
    }

    public void deleteChannel(long cid) {
        mDB.delete(DB_TABLE, COLUMN_CID + " = " + cid, null);
    }
}
