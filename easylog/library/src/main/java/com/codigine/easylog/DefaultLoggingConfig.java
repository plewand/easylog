package com.codigine.easylog;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DefaultLoggingConfig implements LoggingConfig {
    private static final int DEFAULT_MAX_LOG_FILE_SIZE = 150000;
    private static final String DEFAULT_DATE_FORMAT = "yyyy.MM.dd, hh:mm:ss.SSS";

    private final boolean globalLogcatEnabled;
    private final boolean globalFileLoggingEnabled;
    private final String commonLoggingBaseDirectory;
    private final String commonLoggingFile;
    private final int maxLogFileSize;
    private final SimpleDateFormat dateFormat;

    public DefaultLoggingConfig(boolean globalLogcatEnabled, boolean globalFileLoggingEnabled, String commonLoggingBaseDirectory, String commonLoggingFile, int maxLogFileSize, SimpleDateFormat dateFormat) {
        if(commonLoggingBaseDirectory == null) {
            commonLoggingBaseDirectory = "";
        }
        if(commonLoggingFile == null) {
            commonLoggingFile = "";
        }
        this.globalLogcatEnabled = globalLogcatEnabled;
        this.globalFileLoggingEnabled = globalFileLoggingEnabled;
        this.commonLoggingBaseDirectory = commonLoggingBaseDirectory;
        this.commonLoggingFile = commonLoggingFile;
        this.maxLogFileSize = maxLogFileSize;
        this.dateFormat = dateFormat == null ? defaultDateFormat() : dateFormat;
    }

    DefaultLoggingConfig() {
        this.globalLogcatEnabled =true;
        this.globalFileLoggingEnabled = true;
        this.commonLoggingBaseDirectory = "";
        this.commonLoggingFile = "";
        this.maxLogFileSize = DEFAULT_MAX_LOG_FILE_SIZE;
        this.dateFormat = defaultDateFormat();
    }

    private SimpleDateFormat defaultDateFormat() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
    }

    @Override
    public boolean isGlobalFileLoggingEnabled() {
        return globalFileLoggingEnabled;
    }

    @Override
    public String getCommonLoggingDirectory() {
        return commonLoggingBaseDirectory;
    }

    @Override
    public String getCommonLoggingFile() {
        return commonLoggingFile;
    }

    @Override
    public boolean isGlobalLogcatEnabled() {
        return globalLogcatEnabled;
    }

    @Override
    public int getMaxLogFileSize() {
        return maxLogFileSize;
    }

    @Override
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }


    public static class Builder {
        private boolean globalLogcatEnabled;
        private boolean globalFileLoggingEnabled;
        private String commonLoggingBaseDirectory;
        private String commonLoggingFile;
        private int maxLogFileSize;
        private SimpleDateFormat dateFormat;

        public Builder() {

        }

        public Builder(LoggingConfig config) {
            this.globalLogcatEnabled = config.isGlobalLogcatEnabled();
            this.globalFileLoggingEnabled = config.isGlobalFileLoggingEnabled();
            this.commonLoggingBaseDirectory = config.getCommonLoggingDirectory();
            this.commonLoggingFile = config.getCommonLoggingFile();
            this.maxLogFileSize = config.getMaxLogFileSize();
            this.dateFormat = config.getDateFormat();
        }

        public Builder withGlobalLogcatEnabled(boolean globalLogcatEnabled) {
            this.globalLogcatEnabled = globalLogcatEnabled;
            return this;
        }

        public Builder withGlobalFileLoggingEnabled(boolean globalFileLoggingEnabled) {
            this.globalFileLoggingEnabled = globalFileLoggingEnabled;
            return this;
        }

        public Builder withCommonLoggingBaseDirectory(String commonLoggingBaseDirectory) {
            this.commonLoggingBaseDirectory = commonLoggingBaseDirectory;
            return this;
        }

        public Builder withCommonLoggingFile(String commonLoggingFile) {
            this.commonLoggingFile = commonLoggingFile;
            return this;
        }

        public Builder withMaxLogFileSize(int maxLogFileSize) {
            this.maxLogFileSize = maxLogFileSize;
            return this;
        }

        public Builder withDateFormat(SimpleDateFormat dateFormat) {
            this.dateFormat = dateFormat;
            return this;
        }

        public DefaultLoggingConfig build() {
            return new DefaultLoggingConfig(globalLogcatEnabled, globalFileLoggingEnabled, commonLoggingBaseDirectory, commonLoggingFile, maxLogFileSize, dateFormat);
        }
    }
}
