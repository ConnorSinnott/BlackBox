package com.pluviostudios.blackbox;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.pluviostudios.blackbox.data.DBContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "MainActivity";

    private DrawerLayout mDrawer;
    private ListView mDrawerList;

    private void init() {
        mDrawer = (DrawerLayout) findViewById(R.id.activity_main_drawer);
        mDrawerList = (ListView) findViewById(R.id.activity_main_contact_list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_frame, FragmentMain.buildFragmentMain(), FragmentMain.TAG).commit();

        getSupportLoaderManager().initLoader(0, null, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_main_hamburger: {
                if (!mDrawer.isDrawerOpen(GravityCompat.END)) {
                    mDrawer.openDrawer(GravityCompat.END);
                } else {
                    mDrawer.closeDrawer(GravityCompat.END);
                }
                break;
            }
            case R.id.menu_main_add: {
                EditContactDialog editDialog = EditContactDialog.buildEditContactDialogFragment();
                editDialog.show(getSupportFragmentManager(), EditContactDialog.TAG);
                break;
            }
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader =
                new CursorLoader(this,
                        DBContract.ContactEntry.CONTENT_URI,
                        null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDrawerList.setAdapter(new ContactListAdapter(this, data, getSupportFragmentManager()));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
