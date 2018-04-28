package com.example.fay.uidemo.utils;

import android.util.Log;

/**
 * Created by xiaofei9 on 2018/4/28.
 * 设置log_level级别，用于不同场景的日志输出
 */
public class LogUtil {
    public final int LOG_LEVEL = ASSERT;

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    public static int v(String tag, String log) {
        if (LOG_LEVEL <= Log.VERBOSE) {
            return Log.v(tag, log);
        }
        return 0;
    }

    public static int d(String tag, String log) {
        if (LOG_LEVEL <= Log.DEBUG) {
            return Log.d(tag, log);
        }
        return 0;
    }

    public static int i(String tag, String log) {
        if (LOG_LEVEL <= Log.INFO) {
            return Log.i(tag, log);
        }
        return 0;
    }

    public static int w(String tag, String log) {
        if (LOG_LEVEL <= Log.WARN) {
            return Log.w(tag, log);
        }
        return 0;
    }

    public static int e(String tag, String log) {
        if (LOG_LEVEL <= Log.ERROR) {
            return Log.e(tag, log);
        }
        return 0;
    }
}
