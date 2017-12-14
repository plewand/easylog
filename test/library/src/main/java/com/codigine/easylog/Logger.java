package com.codigine.easylog;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Locale;


public class Logger {
    private static final String LOG_TAG = Logger.class.getSimpleName();
    private String tag;
    private FileWriter writer;
    private String logFilePath;
    private boolean forceFileLogging;

    Logger(String tag) {
        this.tag = tag;
    }

    public void d(String log, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        Log.d(tag, fullLog);
        logToFile(fullLog);
    }

    public void i(String log, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        Log.i(tag, fullLog);
        logToFile(fullLog);
    }

    public void w(String log, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        Log.w(tag, fullLog);
        logToFile(fullLog);
    }

    public void e(String log, Object... args) {
        String fullLog = String.format(Locale.getDefault(), log, args);
        Log.e(tag, fullLog);
        logToFile(fullLog);
    }

    private void logToFile(String fullLog) {
        if ((forceFileLogging || Logging.isFileLoggingEnabled()) && writer != null) {
            try {
                checkLogSize(logFilePath);

                String dateInfo = "[" + new Date().toString() + "]: ";
                writer.write(dateInfo + fullLog + "\n");

                writer.flush();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error writing log", e);
            }
        }
    }

    public void checkLogSize(String fileName) {
        try {
            File file = new File(fileName);
            long length = file.length();
            if (length > 50000) {
                copyFile(file, new File(fileName.replace(".txt", "") + ".old.txt"));

                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    public Logger forceFileLogging(boolean enable) {
        this.forceFileLogging = enable;
        return this;
    }

    public Logger f(String fileName) {
        if (writer != null) {
            c();
        }

        String logDir = Logging.getLogDirectoryPath();
        if (!logDir.isEmpty()) {
            String logFilePath = Logging.getLogDirectoryPath() + File.separator + fileName;
            try {
                writer = new FileWriter(logFilePath, true);
                this.logFilePath = logFilePath;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while creating writer", e);
            }
        } else {
            Log.w(LOG_TAG, "No logging dir");
        }
        return this;
    }

    public Logger c() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem with closing writer", e);
            }
            writer = null;
        }
        return this;
    }
}
