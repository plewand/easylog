package com.codigine.easylog;

import android.util.Log;

public class LogcatLogger implements Loggable {
    @Override
    public void debug(String tag, String msg) {
        if(Logging.getConfig().isGlobalLogcatEnabled()) {
            Log.d(tag, msg);
        }
    }

    @Override
    public void info(String tag, String msg) {
        if(Logging.getConfig().isGlobalLogcatEnabled()) {
            Log.i(tag, msg);
        }
    }

    @Override
    public void warning(String tag, String msg) {
        if(Logging.getConfig().isGlobalLogcatEnabled()) {
            Log.w(tag, msg);
        }
    }

    @Override
    public void error(String tag, String msg) {
        if(Logging.getConfig().isGlobalLogcatEnabled()) {
            Log.e(tag, msg);
        }
    }

    @Override
    public void error(String tag, String msg, Throwable ex) {
        if(Logging.getConfig().isGlobalLogcatEnabled()) {
            Log.e(tag, msg, ex);
        }
    }
}
