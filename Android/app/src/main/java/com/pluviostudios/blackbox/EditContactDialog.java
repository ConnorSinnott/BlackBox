package com.pluviostudios.blackbox;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pluviostudios.blackbox.data.DBContract;

/**
 * Created by spectre on 2/19/17.
 */

public class EditContactDialog extends DialogFragment {

    public static final String TAG = "EditContactDialog";

    private View mRoot;

    private static final String EXTRA_ID = "Extra_Id";

    private EditText mNameEditText;
    private EditText mNumberEditText;
    private Button mSave;
    private Button mDelete;

    public static EditContactDialog buildEditContactDialogFragment(long id) {

        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ID, id);

        EditContactDialog dialogFragment = buildEditContactDialogFragment();
        dialogFragment.setArguments(bundle);

        return dialogFragment;

    }

    public static EditContactDialog buildEditContactDialogFragment() {

        EditContactDialog dialogFragment = new EditContactDialog();
        return dialogFragment;

    }

    private void init() {
        mNameEditText = (EditText) mRoot.findViewById(R.id.edit_dialog_fragment_edit_name);
        mNumberEditText = (EditText) mRoot.findViewById(R.id.edit_dialog_fragment_edit_number);
        mSave = (Button) mRoot.findViewById(R.id.edit_dialog_fragment_save);
        mDelete = (Button) mRoot.findViewById(R.id.edit_dialog_fragment_delete);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().setTitle("Edit Contact");

        mRoot = inflater.inflate(R.layout.edit_dialog_fragment, container, false);
        init();

        if (getArguments() != null) {

            long id = getArguments().getLong(EXTRA_ID);

            Cursor c = getContext().getContentResolver().query(
                    DBContract.ContactEntry.CONTENT_URI,
                    null,
                    DBContract.ContactEntry._ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null);

            if (c != null && c.moveToFirst()) {

                String name = c.getString(1);
                String number = c.getString(2);

                mNameEditText.setText(name);
                mNumberEditText.setText(number);

                mSave.setOnClickListener(new View.OnClickListener() {

                    private long id;

                    @Override
                    public void onClick(View v) {

                        ContentValues values = new ContentValues();
                        values.put(DBContract.ContactEntry.NAME_COL, mNameEditText.getText().toString());
                        values.put(DBContract.ContactEntry.PHONE_COL, mNumberEditText.getText().toString());

                        getContext().getContentResolver().update(DBContract.ContactEntry.CONTENT_URI, values,
                                DBContract.ContactEntry._ID + " = ?", new String[]{String.valueOf(id)});

                        dismiss();

                    }

                    public View.OnClickListener setId(long id) {
                        this.id = id;
                        return this;
                    }

                }.setId(c.getLong(0)));

                mDelete.setOnClickListener(new View.OnClickListener() {

                    private long id;

                    @Override
                    public void onClick(View v) {

                        getContext().getContentResolver().delete(DBContract.ContactEntry.CONTENT_URI,
                                DBContract.ContactEntry._ID + " = ?", new String[]{String.valueOf(id)});

                        dismiss();

                    }

                    public View.OnClickListener setId(long id) {
                        this.id = id;
                        return this;
                    }

                }.setId(c.getLong(0)));

                c.close();

            }

        } else {

            mSave.setOnClickListener(new View.OnClickListener() {

                private long id;

                @Override
                public void onClick(View v) {

                    ContentValues values = new ContentValues();
                    values.put(DBContract.ContactEntry.NAME_COL, mNameEditText.getText().toString());
                    values.put(DBContract.ContactEntry.PHONE_COL, mNumberEditText.getText().toString());

                    getContext().getContentResolver().insert(DBContract.ContactEntry.CONTENT_URI, values);

                    dismiss();

                }

            });

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

        return mRoot;

    }

}
