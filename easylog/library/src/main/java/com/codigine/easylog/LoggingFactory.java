package com.codigine.easylog;

class LoggingFactory {
    private static final Loggable LOGCAT_LOG = new LogcatLogger();

    Loggable newFileLog(String path) throws Exception {
        return new FileLogger(path);
    }

    Loggable newLogcatLog() {
        return LOGCAT_LOG;
    }
}
