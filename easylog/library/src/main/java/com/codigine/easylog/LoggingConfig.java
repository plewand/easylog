package com.codigine.easylog;

import java.text.SimpleDateFormat;

public interface LoggingConfig {

    boolean isGlobalLogcatEnabled();

    boolean isGlobalFileLoggingEnabled();

    String getCommonLoggingDirectory();

    String getCommonLoggingFile();

    int getMaxLogFileSize();

    SimpleDateFormat getDateFormat();
}
