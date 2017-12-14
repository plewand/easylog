package com.codigine.easylog

import spock.lang.Specification
import spock.lang.Unroll

import java.text.SimpleDateFormat;


public class LoggingTest extends Specification {
    static final TAG = "tag"
    static final DATE_FMT = new SimpleDateFormat("yyyy.MM.dd, hh:mm:ss z")

    @Unroll
    def "Logger path be properly build for logger path = #loggerPath, global logging dir = #globalLoggingDir and common logging file = #commonLoggingFile"() {
        when: "logger is created"
        def logging = MockedLogSystem.create(TAG, loggerPath, true, true, new DefaultLoggingConfig(true, true, globalLoggingDir, commonLoggingFile, 5000, DATE_FMT))

        then: "common directory is added to the path"
        logging.fileLogPath == expectedPath

        where:
        loggerPath | globalLoggingDir | commonLoggingFile || expectedPath
        "path"     | "global_dir"     | null              || "storage/global_dir/path"
        "path"     | null             | null              || "storage/path"
        "path"     | ""               | null              || "storage/path"
        "path"     | null             | "common_log"      || "storage/path"
        null       | null             | "common_log"      || "storage/common_log"
        null       | "global_dir"     | "common_log"      || "storage/global_dir/common_log"
        null       | null             | null              || null
    }

    def "File logger shouldn't be created, when storage error occurs during creation"() {
        setup: "logger is created"
        def logging = MockedLogSystem.createWithStorageAccessFailure(TAG, "path", true, true, new DefaultLoggingConfig(true, true, "globalLoggingDir", "commonLoggingFile", 5000, DATE_FMT))

        when:
        logging.logger.d("log")

        then: "no logcat appear"
        logging.logcatLogger.lastDebugTag == TAG
        logging.logcatLogger.lastDebugLog == "log"
        logging.fileLogger.lastDebugTag == null
        logging.fileLogger.lastDebugLog == null

    }
}