package com.pluviostudios.blackbox.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by spectre on 2/18/17.
 */

public class DBContract {

    public static final String TAG = "DBContract";

    public static final String CONTENT_AUTHORITY = "com.pluviostudios.blackbox.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CONTACT = "contact";
    public static final String PATH_MESSAGE = "message";

    public static final class ContactEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;

        public static final String TABLE_NAME = PATH_CONTACT;

        public static final String NAME_COL = "name";
        public static final String PHONE_COL = "phone";

        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class MessageEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;

        public static final String TABLE_NAME = PATH_MESSAGE;

        public static final String MESSAGE_COL = "message";

        public static Uri buildMessageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


}
