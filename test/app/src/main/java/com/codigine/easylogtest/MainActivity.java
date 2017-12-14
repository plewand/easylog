package com.codigine.easylogtest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.codigine.easylog.DefaultLoggingConfig;
import com.codigine.easylog.EasyLog;
import com.codigine.easylog.Logger;
import com.codigine.easylog.Logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

// TODO: Nexus
// TODO: String format for file logging
// TODO: easylog/keystore.properites to gitignore
// TODO: Change using local maven to sonatype
// TODO: Clear logs, mainly plugins, test kotlin code
public class MainActivity extends AppCompatActivity {
    private static final String ANDROID_LOG = "NoWrapper";

    @EasyLog(fileLoggingEnabled = false)
    private Logger activityLogger;

    @EasyLog(tag = "Common file")
    private Logger loggerCommonFile;

    @EasyLog(tag = "Custom file", fileLogPath = "single_logger.txt")
    private Logger loggerCustomFile;


    @BindView(R.id.global_logcat_enabled)
    CheckBox globalLogcacatEnabled;

    @BindView(R.id.global_file_enabled)
    CheckBox globalFileLoggingEnabled;


    @BindView(R.id.common_log_logcat_enabled)
    CheckBox commonLogLogcatEnabled;

    @BindView(R.id.common_log_file_enabled)
    CheckBox commonLogFileEnabled;

    @BindView(R.id.common_log_write)
    Button commonLogWrite;


    @BindView(R.id.custom_log_logcat_enabled)
    CheckBox customLogLogcatEnabled;

    @BindView(R.id.custom_log_file_enabled)
    CheckBox customLogFileEnabled;

    @BindView(R.id.custom_log_write)
    Button customLogWrite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Class<?> act = Class.forName("com.codigine.easylog.EasyLogConfig");
            activityLogger.d("props: " + act);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        activityLogger.d("Hello from debug!");
        activityLogger.i("Hello from info!");
        activityLogger.w("Hello from warning!");
        activityLogger.e("Hello from error!");
        activityLogger.e("Hello from error, with exception!", new Exception());

        System.out.println("loggerCommonFile " + loggerCommonFile);
        System.out.println("loggerCustomFile " + loggerCustomFile);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        updateViews();
    }

    private void updateViews() {
        globalLogcacatEnabled.setChecked(Logging.getConfig().isGlobalLogcatEnabled());
        ;
        globalFileLoggingEnabled.setChecked(Logging.getConfig().isGlobalFileLoggingEnabled());
        ;

        commonLogLogcatEnabled.setChecked(loggerCommonFile.isLogcatEnabled());
        commonLogFileEnabled.setChecked(loggerCommonFile.isFileLoggingEnabled());

        customLogLogcatEnabled.setChecked(loggerCustomFile.isLogcatEnabled());
        customLogFileEnabled.setChecked(loggerCustomFile.isFileLoggingEnabled());
    }


    @OnCheckedChanged(R.id.global_logcat_enabled)
    void onGlobalLocacatLoggingChange(CompoundButton button, boolean checked) {
        Log.i(ANDROID_LOG, "Global logcat logging changed: " + checked);

        if (Logging.getConfig().isGlobalLogcatEnabled() == checked) {
            return;
        }

        activityLogger.i("Hello from activity logger, before change!");

        Logging.setConfig(new DefaultLoggingConfig.Builder(Logging.getConfig()).withGlobalLogcatEnabled(checked).build());

        activityLogger.i("Hello from activity logger, after change!");
    }

    @OnCheckedChanged(R.id.global_file_enabled)
    void onGlobalFileLoggingChange(CompoundButton button, boolean checked) {
        Log.i(ANDROID_LOG, "Global file logging changed:" + checked);

        if (Logging.getConfig().isGlobalFileLoggingEnabled() == checked) {
            return;
        }

        activityLogger.i("Hello from activity logger, before change!");

        Logging.setConfig(new DefaultLoggingConfig.Builder(Logging.getConfig()).withGlobalFileLoggingEnabled(checked).build());

        activityLogger.i("Hello from activity logger, after change!");
    }


    @OnCheckedChanged(R.id.common_log_logcat_enabled)
    void onCommonLogLogcatChange(CompoundButton button, boolean checked) {
        loggerCommonFile.setLogcatEnabled(checked);
    }

    @OnCheckedChanged(R.id.common_log_file_enabled)
    void onCommonLogFileChange(CompoundButton button, boolean checked) {
        loggerCommonFile.setFileLoggingEnabled(checked);
    }

    @OnClick(R.id.common_log_write)
    void onCommonLogWrite() {
        loggerCommonFile.i("On click");
    }


    @OnCheckedChanged(R.id.custom_log_logcat_enabled)
    void onCustomLogLogcatChange(CompoundButton button, boolean checked) {
        loggerCustomFile.setLogcatEnabled(checked);
    }

    @OnCheckedChanged(R.id.custom_log_file_enabled)
    void onCustomLogFileChange(CompoundButton button, boolean checked) {
        loggerCustomFile.setFileLoggingEnabled(checked);
    }

    @OnClick(R.id.custom_log_write)
    void onCustomLogWrite() {
        loggerCustomFile.i("On click");
    }

    @OnClick(R.id.custom_log_clear)
    void onCustomLogClear() {
        clearLog("single_logger.txt");
    }

    @OnClick(R.id.common_log_clear)
    void onCommonLogClear() {
        clearLog("common.txt");
    }

    private void clearLog(String fileName) {
        String dir = getLogDir();
        try {
            PrintWriter writer = new PrintWriter(dir + File.separator + fileName);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            activityLogger.e("Problem with clearing log", e);
        }
    }

    private String getLogDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "easylog_test";
    }
}
