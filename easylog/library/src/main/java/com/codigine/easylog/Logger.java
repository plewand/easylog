package com.codigine.easylog;

import java.util.Locale;


public class Logger {
    private String tag;
    private boolean logcatEnabled;
    private boolean fileLoggingEnabled;
    private final Loggable logcatLogger;
    private final Loggable fileLogger;

    Logger(String tag, boolean logcatEnabled, boolean fileLoggingEnabled, Loggable logcatLogger, Loggable fileLogger) {
        this.tag = tag;
        this.logcatEnabled = logcatEnabled;
        this.fileLoggingEnabled = fileLoggingEnabled;
        this.logcatLogger = logcatLogger;
        this.fileLogger = fileLogger;
    }

    public void d(String log, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        if (logcatEnabled && logcatLogger != null) {
            logcatLogger.debug(tag, fullLog);
        }
        if (fileLoggingEnabled && fileLogger != null) {
            fileLogger.debug(tag, fullLog);
        }
    }

    public void i(String log, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        if (logcatEnabled && logcatLogger != null) {
            logcatLogger.info(tag, fullLog);
        }
        if (fileLoggingEnabled && fileLogger != null) {
            fileLogger.info(tag, fullLog);
        }
    }

    public void w(String log, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        if (logcatEnabled && logcatLogger != null) {
            logcatLogger.warning(tag, fullLog);
        }
        if (fileLoggingEnabled && fileLogger != null) {
            fileLogger.warning(tag, fullLog);
        }
    }

    public void e(String log, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        if (logcatEnabled && logcatLogger != null) {
            logcatLogger.error(tag, fullLog);
        }
        if (fileLoggingEnabled && fileLogger != null) {
            fileLogger.error(tag, fullLog);
        }
    }

    public void e(String log, Throwable ex, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        if (logcatEnabled && logcatLogger != null) {
            logcatLogger.error(tag, fullLog, ex);
        }
        if (fileLoggingEnabled && fileLogger != null) {
            fileLogger.error(tag, fullLog, ex);
        }
    }

    public String getTag() {
        return tag;
    }

    public boolean isLogcatEnabled() {
        return logcatEnabled;
    }

    public boolean isFileLoggingEnabled() {
        return fileLoggingEnabled;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setLogcatEnabled(boolean logcatEnabled) {
        this.logcatEnabled = logcatEnabled;
    }

    public void setFileLoggingEnabled(boolean fileLoggingEnabled) {
        this.fileLoggingEnabled = fileLoggingEnabled;
    }
}
