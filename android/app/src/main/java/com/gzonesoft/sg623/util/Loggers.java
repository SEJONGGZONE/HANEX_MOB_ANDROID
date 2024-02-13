package com.gzonesoft.sg623.util;

import android.util.Log;

public final class Loggers {
    private static final String TAG = "TOP";
    public static boolean SEND_LOG = false;

    public static final void d(String msg) {
        if (Config.BuildType.DEBUG.equals(Config.BUILD_TYPE)) {
            Log.d(Loggers.TAG, msg);
        }
    }

    public static final void d2(Object obj, String msg) {
        if (Config.BuildType.DEBUG.equals(Config.BUILD_TYPE)) {
//            Log.d(Loggers.TAG, "[" + obj.getClass().toString() + "]" + msg);

            if (msg.length() > 4000) {
                Log.v(TAG, "sb.length = " + msg.length());
                int chunkCount = msg.length() / 4000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= msg.length()) {
                        Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i));
                    } else {
                        Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i, max));
                    }
                }
            } else {
                Log.d(Loggers.TAG, "[" + obj.getClass().toString() + "]" + msg);
            }
        }
    }

    public static final void i(String msg) {
        if (Config.BuildType.DEBUG.equals(Config.BUILD_TYPE)) {
            Log.i(Loggers.TAG, msg);
        }
    }

    public static final void e(String msg, Throwable e) {
        Log.e(Loggers.TAG, msg, e);
    }

    public static final void e(Throwable e) {
        Loggers.e("Throwable: " + e, e);
    }
}
