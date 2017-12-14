package com.codigine.easylog;

import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;

public class Logging {
    static final String LOG_TAG = "EasyLog";

    private static LoggingFactory loggingFactory = new LoggingFactory();
    private static ConcurrentHashMap<String, Loggable> openFileLogs = new ConcurrentHashMap<>();
    private static LoggingConfig config = getInitialConfig();
    private static EnvUtil envUtil = new EnvUtil();

    public static Logger newLog(String tag, String logFileRelativePath, boolean logcatEnabled, boolean fileLoggingEnabled) {
        String fullLogFilePath = null;
        if (fileLoggingEnabled) {
            if (logFileRelativePath == null || logFileRelativePath.isEmpty()) {
                logFileRelativePath = config.getCommonLoggingFile();
            }

            if (!logFileRelativePath.isEmpty()) {
                fullLogFilePath = initFileLogging(logFileRelativePath);
            }
        }

        if (tag == null || tag.isEmpty()) {
            tag = getDefaultTag();
        }

        return new Logger(tag, logcatEnabled, fileLoggingEnabled, loggingFactory.newLogcatLog(), fullLogFilePath != null ? openFileLogs.get(fullLogFilePath) : null);
    }

    private static String initFileLogging(String loggerPath) {
        try {
            if (!envUtil.externalStorageMounded()) {
                Log.e(LOG_TAG, "No external storage available");
                return null;
            } else {
                String fullPath = envUtil.externalStorageDirectory() + File.separator;

                if (!config.getCommonLoggingDirectory().isEmpty()) {
                    fullPath += config.getCommonLoggingDirectory() + File.separator;
                }

                fullPath += loggerPath;

                if (openFileLogs.get(fullPath) != null) {
                    return fullPath;
                }

                Log.d(LOG_TAG, "Creating new log file: " + fullPath);

                openFileLogs.put(fullPath, loggingFactory.newFileLog(fullPath));

                return fullPath;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while log file creation attempt, file logging will not be possible for the logger", e);
            return null;
        }
    }

    private static String getDefaultTag() {
        StackTraceElement callingClassStackTraceElement = null;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraceElements.length - 1; i++) {
            while (stackTraceElements[i].getClassName().equals(Logging.class.getName())) {
                callingClassStackTraceElement = stackTraceElements[i + 1];
                i++;
            }
            if (callingClassStackTraceElement != null) {
                break;
            }
        }
        if (callingClassStackTraceElement != null) {
            String fullName = callingClassStackTraceElement.getClassName();
            int lastDotPos = fullName.lastIndexOf('.');
            return fullName.substring(lastDotPos + 1, fullName.length());
        }
        return "";
    }

    public static LoggingConfig getConfig() {
        return config;
    }

    public static void setConfig(LoggingConfig config) {
        Logging.config = config;
    }

    public static void setLoggingFactory(LoggingFactory loggingFactory) {
        Logging.loggingFactory = loggingFactory;
    }

    public static void setEnvUtil(EnvUtil envUtil) {
        Logging.envUtil = envUtil;
    }

    static void reset() {
        openFileLogs.clear();
        loggingFactory = new LoggingFactory();
        openFileLogs = new ConcurrentHashMap<>();
        config = getInitialConfig();
        envUtil = new EnvUtil();
    }

    private static LoggingConfig getInitialConfig() {
        try {
            Class<?> act = Class.forName("com.codigine.easylog.EasyLogConfig");
            LoggingConfig config = (LoggingConfig) act.newInstance();
            Log.d(LOG_TAG, "Gradle config found: \n" + formatConfig(config));
            return config;
        } catch (ClassNotFoundException e) {
            Log.w(LOG_TAG, "No gradle config provided, using defaults");
            return new DefaultLoggingConfig();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while initialing gradle config, using defaults", e);
            return new DefaultLoggingConfig();
        }
    }

    private static String formatConfig(LoggingConfig config) {
        return "logcat enabled: " + config.isGlobalLogcatEnabled() + "\n" +
                "file logging enabled: " + config.isGlobalFileLoggingEnabled() + "\n" +
                "common logging directory: " + config.getCommonLoggingDirectory() + "\n" +
                "common logging file: " + config.getCommonLoggingFile() + "\n" +
                "max log file size: " + config.getMaxLogFileSize() + "\n";
    }
}
