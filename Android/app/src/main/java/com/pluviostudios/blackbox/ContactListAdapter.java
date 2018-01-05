package com.pluviostudios.blackbox;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by spectre on 2/18/17.
 */
public class ContactListAdapter extends CursorAdapter {

    private FragmentManager mFragmentManager;

    public ContactListAdapter(Context context, Cursor c, FragmentManager fragmentManager) {
        super(context, c, 0);
        mFragmentManager = fragmentManager;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_contact, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView name = (TextView) view.findViewById(R.id.listview_contact_name);
        name.setText(cursor.getString(1));

        TextView phone = (TextView) view.findViewById(R.id.listview_contact_phone);
        phone.setText(cursor.getString(2));

        view.setOnClickListener(new View.OnClickListener() {

            private long id;
            private FragmentManager fragmentManager;

            @Override
            public void onClick(View v) {

                EditContactDialog editContactDialog = EditContactDialog.buildEditContactDialogFragment(id);
                editContactDialog.show(fragmentManager, EditContactDialog.TAG);

            }

            public View.OnClickListener set(long id, FragmentManager manager) {
                this.id = id;
                fragmentManager = manager;
                return this;
            }

        }.set(cursor.getLong(0), mFragmentManager));

    }


}
