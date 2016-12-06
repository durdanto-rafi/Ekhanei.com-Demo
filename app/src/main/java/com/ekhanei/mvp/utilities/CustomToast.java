package com.ekhanei.mvp.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


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
