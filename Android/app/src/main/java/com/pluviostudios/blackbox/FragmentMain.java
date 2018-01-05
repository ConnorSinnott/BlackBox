package com.pluviostudios.blackbox;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pluviostudios.blackbox.data.DBContract;
import com.pluviostudios.blackbox.rest.RequestQueueGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by spectre on 2/18/17.
 */

public class FragmentMain extends Fragment implements View.OnClickListener {

    public static final String TAG = "FragmentMain";

    public static final String PREF_MESSAGE = "pref-message";

    private static final int CHANGE_DUR = 5;

    private static final String id = UUID.randomUUID().toString();

    private View mRoot;
    private TextView mDuration;
    private EditText mEditText;
    private ProgressBar mProgressBar;
    private Button mConnectButton;
    private Button mButtonAdd;
    private Button mButtonSubtract;
    private Button mButtonPing;

    private int currentProgress = 10;
    private int startDuration = currentProgress;
    private boolean running = false;

    public static FragmentMain buildFragmentMain() {
        return new FragmentMain();
    }

    private void init() {
        mConnectButton = (Button) mRoot.findViewById(R.id.fragment_main_connect);
        mProgressBar = (ProgressBar) mRoot.findViewById(R.id.fragment_main_progress);
        mDuration = (TextView) mRoot.findViewById(R.id.fragment_main_duration_edit);
        mButtonPing = (Button) mRoot.findViewById(R.id.fragment_main_ping);
        mButtonAdd = (Button) mRoot.findViewById(R.id.fragment_main_add);
        mButtonSubtract = (Button) mRoot.findViewById(R.id.fragment_main_subtract);
        mEditText = (EditText) mRoot.findViewById(R.id.fragment_main_message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_main, null, false);
        init();

        mConnectButton.setOnClickListener(this);
        mButtonAdd.setOnClickListener(this);
        mButtonSubtract.setOnClickListener(this);
        mButtonPing.setOnClickListener(this);
        mEditText.setText(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(PREF_MESSAGE, "SOS: I need somebody!"));

        updateGUI();

        getActivity().runOnUiThread(new Runnable() {

            Handler mHandler = new Handler();

            @Override
            public void run() {

                mHandler.postDelayed(this, 1000);

                if (running) {
                    currentProgress--;
                    mProgressBar.setProgress(currentProgress);
                    mDuration.setText(String.valueOf(currentProgress));

                    if (currentProgress <= 0) {
                        stop();
                    }
                }

            }

        });

        return mRoot;
    }

    @Override
    public void onStop() {
        super.onStop();

        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putString(PREF_MESSAGE, mEditText.getText().toString())
                .apply();

    }

    private void updateDuration(int newDuration) {
        currentProgress = newDuration;
        updateGUI();
    }

    private void updateDuration(boolean add) {

        if (currentProgress > 5 && !add) {
            currentProgress -= CHANGE_DUR;
        }
        if (add) {
            currentProgress += CHANGE_DUR;
        }

        updateGUI();

    }

    private void updateGUI() {
        mProgressBar.setMax(currentProgress);
        mProgressBar.setProgress(currentProgress);
        mDuration.setText(String.valueOf(currentProgress));
    }

    private void connect() {

        try {

            JSONObject requestObject = new JSONObject();

            requestObject.put("userID", id);

            Cursor c = getContext().getContentResolver().query(
                    DBContract.ContactEntry.CONTENT_URI,
                    new String[]{DBContract.ContactEntry.PHONE_COL},
                    null, null, null);

            JSONArray array = new JSONArray();
            if (c.moveToFirst()) {
                do {
                    array.put(c.getString(0));
                } while (c.moveToNext());
            }
            c.close();

            requestObject.put("contactList", array);
            requestObject.put("message", mEditText.getText().toString());
            requestObject.put("duration", currentProgress);

            Log.d(TAG, "connect: " + requestObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.connection_create_string),
                    requestObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                            start();
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onError: " + error);
                        }
                    });

            RequestQueueGenerator.getRequestQueue(getContext()).add(jsonObjectRequest);

        } catch (JSONException e) {
            Log.e(TAG, "connect: ", e);
        }

    }

    private void ping() {

        try {

            JSONObject requestObject = new JSONObject();

            requestObject.put("userID", id);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.connection_ping_string),
                    requestObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                            updateDuration(startDuration);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onError: " + error);
                        }
                    });

            RequestQueueGenerator.getRequestQueue(getContext()).add(jsonObjectRequest);

        } catch (JSONException e) {
            Log.e(TAG, "connect: ", e);
        }

    }

    private void disconnect() {

        try {

            JSONObject requestObject = new JSONObject();

            requestObject.put("userID", id);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.connection_delete_string),
                    requestObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                            stop();
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onError: " + error);
                        }
                    });

            RequestQueueGenerator.getRequestQueue(getContext()).add(jsonObjectRequest);

        } catch (JSONException e) {
            Log.e(TAG, "connect: ", e);
        }

    }

    private void start() {

        startDuration = currentProgress;
        mConnectButton.setText("Disconnect");
        mButtonPing.setEnabled(true);
        mButtonAdd.setEnabled(false);
        mButtonSubtract.setEnabled(false);

        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putString(PREF_MESSAGE, mEditText.getText().toString())
                .apply();

        running = true;

    }

    private void stop() {

        running = false;

        currentProgress = startDuration;
        mConnectButton.setText("Connect");
        mButtonPing.setEnabled(false);
        mButtonAdd.setEnabled(true);
        mButtonSubtract.setEnabled(true);

        updateGUI();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_main_connect: {
                if (!running)
                    connect();
                else
                    disconnect();
                break;
            }
            case R.id.fragment_main_add: {
                updateDuration(true);
                break;
            }
            case R.id.fragment_main_subtract: {
                updateDuration(false);
                break;
            }
            case R.id.fragment_main_ping: {
                ping();
            }
        }

    }
}
