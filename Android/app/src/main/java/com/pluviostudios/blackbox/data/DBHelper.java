package com.pluviostudios.blackbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by spectre on 2/18/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    public static final String DATABASE_NAME = "BlackBox.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ContactEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.MessageEntry.TABLE_NAME);

        final String CreateContactTable = "CREATE TABLE " + DBContract.ContactEntry.TABLE_NAME + " ("
                + DBContract.ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBContract.ContactEntry.NAME_COL + " TEXT NOT NULL,"
                + DBContract.ContactEntry.PHONE_COL + " TEXT NOT NULL)";

        final String CreateMessageTable = "CREATE TABLE " + DBContract.MessageEntry.TABLE_NAME + " ("
                + DBContract.MessageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBContract.MessageEntry.MESSAGE_COL + " TEXT NOT NULL)";

        db.execSQL(CreateContactTable);
        db.execSQL(CreateMessageTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ContactEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.MessageEntry.TABLE_NAME);

    }

}
