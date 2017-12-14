package com.codigine.easylog

public class MockedLogSystem {
    def logcatLogger = new TestLogger()
    def fileLogger = new TestLogger()
    Logger logger;
    String fileLogPath

    static MockedLogSystem create(String tag, String loggerFilePath, boolean logcatEnabled, boolean fileLoggingEnabled, DefaultLoggingConfig config) {
        new MockedLogSystem(tag, loggerFilePath, logcatEnabled, fileLoggingEnabled, config, new MockedEnvUtil())
    }

    static MockedLogSystem createWithStorageAccessFailure(String tag, String loggerFilePath, boolean logcatEnabled, boolean fileLoggingEnabled, DefaultLoggingConfig config) {
        new MockedLogSystem(tag, loggerFilePath, logcatEnabled, fileLoggingEnabled, config, new MockedEnvUtilFailure())
    }

    private MockedLogSystem(String tag, String loggerFilePath, boolean logcatEnabled, boolean fileLoggingEnabled, DefaultLoggingConfig config, EnvUtil envUtil) {
        Logging.reset();
        Logging.setLoggingFactory(new MockedLoggingFactory())
        Logging.setEnvUtil(envUtil)
        Logging.setConfig(config)
        logger = Logging.newLog(tag, loggerFilePath, logcatEnabled, fileLoggingEnabled)
    }


    class TestLogger implements Loggable {
        String lastDebugTag
        String lastDebugLog
        String lastInfoTag
        String lastInfoLog
        String lastWarningTag
        String lastWarningLog
        String lastErrorTag
        String lastErrorLog
        Throwable lastException

        @Override
        void debug(String tag, String msg) {
            lastDebugTag = tag
            lastDebugLog = msg;
        }

        @Override
        void info(String tag, String msg) {
            lastInfoTag = tag
            lastInfoLog = msg;
        }

        @Override
        void warning(String tag, String msg) {
            lastWarningTag = tag
            lastWarningLog = msg;
        }

        @Override
        void error(String tag, String msg) {
            lastErrorTag = tag
            lastErrorLog = msg;
        }

        @Override
        void error(String tag, String msg, Throwable ex) {
            lastErrorTag = tag
            lastErrorLog = msg;
            lastException = ex
        }
    }

    class MockedLoggingFactory extends LoggingFactory {
        @Override
        Loggable newFileLog(String path) throws Exception {
            fileLogPath = path
            return fileLogger
        }

        @Override
        Loggable newLogcatLog() {
            return logcatLogger
        }
    }

    static class MockedEnvUtil extends EnvUtil {
        @Override
        boolean externalStorageMounded() {
            return true
        }

        @Override
        String externalStorageDirectory() {
            return "storage"
        }
    }

    static class MockedEnvUtilFailure extends EnvUtil {
        @Override
        boolean externalStorageMounded() {
            throw new IOException()
        }

        @Override
        String externalStorageDirectory() {
            throw new IOException()
        }
    }
}