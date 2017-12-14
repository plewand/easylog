package com.codigine.easylog;

public interface Loggable {
    void debug(String tag, String msg);
    void info(String tag, String msg);
    void warning(String tag, String msg);
    void error(String tag, String msg);
    void error(String tag, String msg, Throwable ex);
}
