package com.codigine.easylog;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Logging {
    private static final Object LOG_DIR = "automute_logs";
    private static String logDirectoryPath = "";
    private static boolean fileLoggingEnabled;

    public static Logger newLog() {
        if (logDirectoryPath.isEmpty()) {
            createLoggingDir();
        }
        return new Logger(getDefaultTag());
    }

    private static void createLoggingDir() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e("Logging", "No sd card");
        } else {
            String dir = Environment.getExternalStorageDirectory() + File.separator + LOG_DIR;
            //create folder
            File folder = new File(dir); //folder name
            folder.mkdirs();

            logDirectoryPath = dir;
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

    public static String getLogDirectoryPath() {
        return logDirectoryPath;
    }

    public static boolean isFileLoggingEnabled() {
        return fileLoggingEnabled;
    }

    public static void setFileLoggingEnabled(boolean fileLoggingEnabled) {
        Logging.fileLoggingEnabled = fileLoggingEnabled;
    }
}
