package com.pluviostudios.blackbox.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by spectre on 2/18/17.
 */

public class DBContentProvider extends ContentProvider {

    public static final String TAG = "DBContentProvider";

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mOpenHelper;

    static final int CONTACT = 100;
    static final int MESSAGE = 200;

    public static final String CONTACT_ID = "configId";

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DBContract.PATH_CONTACT, CONTACT);
        matcher.addURI(authority, DBContract.PATH_MESSAGE, MESSAGE);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CONTACT:
                return DBContract.ContactEntry.CONTENT_TYPE;
            case MESSAGE:
                return DBContract.MessageEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case CONTACT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.ContactEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case MESSAGE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.MessageEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CONTACT: {
                long _id = db.insert(DBContract.ContactEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DBContract.ContactEntry.buildContactUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row int " + uri);
                break;
            }
            case MESSAGE: {
                long _id = db.insert(DBContract.MessageEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DBContract.MessageEntry.buildMessageUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row int " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {

            case CONTACT: {
                rowsDeleted = db.delete(DBContract.ContactEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case MESSAGE: {
                rowsDeleted = db.delete(DBContract.ContactEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);

        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {

            case CONTACT: {
                rowsUpdated = db.update(DBContract.ContactEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            case MESSAGE: {
                rowsUpdated = db.update(DBContract.ContactEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);

        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }

}
