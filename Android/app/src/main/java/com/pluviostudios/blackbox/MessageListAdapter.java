package com.pluviostudios.blackbox;

import android.content.Context;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by spectre on 2/19/17.
 */

public class MessageListAdapter extends CursorAdapter {

    private Cursor c;

    public MessageListAdapter(Context context, Cursor c) {
        super(context, c);
        this.c = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_message, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name = (TextView) view.findViewById(R.id.listview_message_text);
        name.setText(cursor.getString(1));

        view.setOnClickListener(new View.OnClickListener() {

            private Context context;
            private String text;

            @Override
            public void onClick(View v) {

                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putString(FragmentMain.PREF_MESSAGE, text).apply();

            }

            public View.OnClickListener set(Context context, String text) {
                this.context = context;
                this.text = text;
                return this;
            }

        }.set(context, cursor.getString(1)));

    }

}
