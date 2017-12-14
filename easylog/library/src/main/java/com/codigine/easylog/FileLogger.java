package com.codigine.easylog;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.Date;

class FileLogger implements Loggable {
    private static final String LOG_TAG = Logging.LOG_TAG;
    private static final String LOG_FILE_EXT = "log";

    private final File file;
    private final FileWriter writer;

    FileLogger(String fullPath) throws Exception {
        File file = new File(fullPath);

        if (file.exists()) {
            Log.d(LOG_TAG, "Log file exists, reusing: " + fullPath);
        }
        else {
            File dir = file.getParentFile();
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e(LOG_TAG, "Couldn't create logging directory: " + fullPath);
            }

            if (!file.createNewFile()) {
                Log.e(LOG_TAG, "Couldn't create log file: " + fullPath);
            }
        }

        this.writer = new FileWriter(fullPath, true);
        this.file = file;
    }

    synchronized private void logToFile(String fullLog) {
        try {
            if(Logging.getConfig().isGlobalFileLoggingEnabled()) {
                checkLogSize();

                String dateInfo = "[" +Logging.getConfig().getDateFormat().format(new Date()) + "]: ";
                writer.write(dateInfo + fullLog + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error writing log", e);
        }
    }

    private void checkLogSize() {
        try {
            long length = file.length();
            if (length > Logging.getConfig().getMaxLogFileSize()) {
                copyFile(file, new File(file.getAbsolutePath().replace("." + LOG_FILE_EXT, "") + ".old." + LOG_FILE_EXT));

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

    @Override
    public void debug(String tag, String msg) {
        logToFile("D: " + tag + ": " + msg);
    }

    @Override
    public void info(String tag, String msg) {
        logToFile("I: " + tag + ": " + msg);
    }

    @Override
    public void warning(String tag, String msg) {
        logToFile("W: " + tag + ": " + msg);
    }

    @Override
    public void error(String tag, String msg) {
        logToFile("E: " + tag + ": " + msg);
    }

    @Override
    public void error(String tag, String msg, Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        logToFile("E: " + tag + ": " + msg);
        logToFile(sw.toString());
    }
}
