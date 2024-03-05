package dev.tmpfs.oplus.redonekiller.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Log {

    private Log() {
    }

    public static final String TAG = "RedOneKiller";

    public static void log(int level, @Nullable String msg, @Nullable Throwable tr) {
        if (msg != null) {
            android.util.Log.println(level, TAG, msg);
        }
        if (tr != null) {
            android.util.Log.println(level, TAG, android.util.Log.getStackTraceString(tr));
        }
    }

    public static void v(@NonNull String msg) {
        log(android.util.Log.VERBOSE, msg, null);
    }

    public static void v(@NonNull Throwable tr) {
        log(android.util.Log.VERBOSE, null, tr);
    }

    public static void v(@NonNull String msg, @NonNull Throwable tr) {
        log(android.util.Log.VERBOSE, msg, tr);
    }

    public static void d(@NonNull String msg) {
        log(android.util.Log.DEBUG, msg, null);
    }

    public static void d(@NonNull Throwable tr) {
        log(android.util.Log.DEBUG, null, tr);
    }

    public static void d(@NonNull String msg, @NonNull Throwable tr) {
        log(android.util.Log.DEBUG, msg, tr);
    }

    public static void i(@NonNull String msg) {
        log(android.util.Log.INFO, msg, null);
    }

    public static void i(@NonNull Throwable tr) {
        log(android.util.Log.INFO, null, tr);
    }

    public static void i(@NonNull String msg, @NonNull Throwable tr) {
        log(android.util.Log.INFO, msg, tr);
    }

    public static void w(@NonNull String msg) {
        log(android.util.Log.WARN, msg, null);
    }

    public static void w(@NonNull Throwable tr) {
        log(android.util.Log.WARN, null, tr);
    }

    public static void w(@NonNull String msg, @NonNull Throwable tr) {
        log(android.util.Log.WARN, msg, tr);
    }

    public static void e(@NonNull String msg) {
        log(android.util.Log.ERROR, msg, null);
    }

    public static void e(@NonNull Throwable tr) {
        log(android.util.Log.ERROR, null, tr);
    }

    public static void e(@NonNull String msg, @NonNull Throwable tr) {
        log(android.util.Log.ERROR, msg, tr);
    }


}
