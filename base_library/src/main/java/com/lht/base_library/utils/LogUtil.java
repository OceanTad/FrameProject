package com.lht.base_library.utils;

import android.util.Log;

import com.lht.base_library.base.BaseApplication;

public class LogUtil {

    private static final String LOG_TAG = "LogUtil";
    private static boolean releaseEnable = false;
    private static boolean debugEnable = true;

    public static void debugEnable(boolean enable) {
        debugEnable = enable;
    }

    public static void releaseEnable(boolean enable) {
        releaseEnable = enable;
    }

    public static void e(String log) {
        e(tag(), log);
    }

    public static void e(String tag, String log) {
        if (canLog()) {
            for (String str : splitLog(log)) {
                Log.e(tag, str);
            }
        }
    }

    public static void d(String log) {
        d(tag(), log);
    }

    public static void d(String tag, String log) {
        if (canLog()) {
            for (String str : splitLog(log)) {
                Log.d(tag, str);
            }
        }
    }

    public static void i(String log) {
        i(tag(), log);
    }

    public static void i(String tag, String log) {
        if (canLog()) {
            for (String str : splitLog(log)) {
                Log.i(tag, str);
            }
        }
    }

    public static void w(String log) {
        w(tag(), log);
    }

    public static void w(String tag, String log) {
        if (canLog()) {
            for (String str : splitLog(log)) {
                Log.w(tag, str);
            }
        }
    }

    private static boolean canLog() {
        return AppConfigUtil.isDebug(BaseApplication.getInstance()) ? debugEnable : releaseEnable;
    }

    private static String[] splitLog(String str) {
        int length = str.length();
        String[] logs = new String[length / (3 * 1024) + 1];
        int start = 0;
        for (int i = 0; i < logs.length; i++) {
            if ((start + 3 * 1024) < length) {
                logs[i] = str.substring(start, start + 3 * 1024);
                start += 3 * 1024;
            } else {
                logs[i] = str.substring(start, length);
                start = length;
            }
        }
        return logs;
    }

    private static String tag() {
        StackTraceElement[] caller = Thread.currentThread().getStackTrace();
        if (caller.length > 4 && caller[4] != null) {
            return LOG_TAG + ":(" + caller[4].getClassName() + "/" + caller[4].getLineNumber() + ")";
        } else {
            return LOG_TAG;
        }
    }

}
