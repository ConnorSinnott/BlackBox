package com.pluviostudios.blackbox.rest;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by spectre on 2/18/17.
 */

public class RequestQueueGenerator {

    private static RequestQueue sRequestQueue;

    public static RequestQueue getRequestQueue(Context context) {
        if (sRequestQueue == null) {
            sRequestQueue = Volley.newRequestQueue(context);
        }
        return sRequestQueue;
    }

}
