package com.androidtime.mvp.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtime.mvp.R;


/**
 * Created by ibrar on 5/25/2016.
 */
public class CustomToast {

    public static void m(String message) {
        Log.d("md", "" + message);
    }

    public static void M(String str, String message) {
        Log.e(str, message);
    }

    public static void T(Context ctx, String message) {
        Toast.makeText(ctx, message + "", Toast.LENGTH_LONG).show();
    }
}
