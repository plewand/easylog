package com.codigine.easylog

import spock.lang.Specification
import spock.lang.Unroll;


public class LoggerTest extends Specification {
    final static TAG = "tag"
    final static EXCEPTION = new Exception()

    @Unroll
    def "Logger should log debug for console logging=#consoleLoggingEnabled, file logging=#fileLoggingEnabled, loggerFilePath=#loggerFilePath"() {
        setup: "logging system us prepared, with"
        def logging = MockedLogSystem.create(TAG, loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, new DefaultLoggingConfig())

        when: "user calls debug"
        logging.logger.d("l%s%d", "a", 1)

        then: "message is properly logged"
        checkLog(logging, [logcatDebugTag: expectedConsoleTag, logcatDebugLog: expectedConsoleLog, fileDebugTag: expectedFileTag, fileDebugLog: expectedFileLog])

        where:
        [loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, expectedFileLog, expectedConsoleLog, expectedFileTag, expectedConsoleTag] << genDataForGlobalLogFileDisabled()
    }

    @Unroll
    def "Logger should log info for console logging=#consoleLoggingEnabled, file logging=#fileLoggingEnabled, loggerFilePath=#loggerFilePath"() {
        setup: "logging system us prepared, with"
        def logging = MockedLogSystem.create(TAG, loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, new DefaultLoggingConfig())

        when: "user calls info"
        logging.logger.i("l%s%d", "a", 1)

        then: "message is properly logged"
        checkLog(logging, [logcatInfoTag: expectedConsoleTag, logcatInfoLog: expectedConsoleLog, fileInfoTag: expectedFileTag, fileInfoLog: expectedFileLog])

        where:
        [loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, expectedFileLog, expectedConsoleLog, expectedFileTag, expectedConsoleTag] << genDataForGlobalLogFileDisabled()
    }

    @Unroll
    def "Logger should log warning for console logging=#consoleLoggingEnabled, file logging=#fileLoggingEnabled, loggerFilePath=#loggerFilePath"() {
        setup: "logging system us prepared, with"
        def logging = MockedLogSystem.create(TAG, loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, new DefaultLoggingConfig())

        when: "user calls warning"
        logging.logger.w("l%s%d", "a", 1)

        then: "message is properly logged"
        checkLog(logging, [logcatWarningTag: expectedConsoleTag, logcatWarningLog: expectedConsoleLog, fileWarningTag: expectedFileTag, fileWarningLog: expectedFileLog])

        where:
        [loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, expectedFileLog, expectedConsoleLog, expectedFileTag, expectedConsoleTag] << genDataForGlobalLogFileDisabled()
    }

    @Unroll
    def "Logger should log error for console logging=#consoleLoggingEnabled, file logging=#fileLoggingEnabled, loggerFilePath=#loggerFilePath"() {
        setup: "logging system us prepared, with"
        def logging = MockedLogSystem.create(TAG, loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, new DefaultLoggingConfig())

        when: "user calls warning"
        logging.logger.e("l%s%d", "a", 1)

        then: "message is properly logged"
        checkLog(logging, [logcatErrorTag: expectedConsoleTag, logcatErrorLog: expectedConsoleLog, fileErrorTag: expectedFileTag, fileErrorLog: expectedFileLog])

        where:
        [loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, expectedFileLog, expectedConsoleLog, expectedFileTag, expectedConsoleTag] << genDataForGlobalLogFileDisabled()
    }

    @Unroll
    def "Logger should log error with exception for console logging=#consoleLoggingEnabled, file logging=#fileLoggingEnabled, loggerFilePath=#loggerFilePath"() {
        setup: "logging system us prepared, with"
        def logging = MockedLogSystem.create(TAG, loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, new DefaultLoggingConfig())

        when: "user calls warning"
        logging.logger.e("l%s%d", EXCEPTION, "a", 1,)

        then: "message is properly logged"
        checkLog(logging, [logcatErrorTag: expectedConsoleTag, logcatErrorLog: expectedConsoleLog, logcatException: expectedLogcatException,
                           fileErrorTag  : expectedFileTag, fileErrorLog: expectedFileLog, fileException: expectedFileException])

        where:
        [loggerFilePath, consoleLoggingEnabled, fileLoggingEnabled, expectedFileLog, expectedConsoleLog,
         expectedFileTag, expectedConsoleTag, expectedFileException, expectedLogcatException] << genDataForGlobalLogFileDisabled()
     }

    def a() {
        "ad"
    }

    def genTags(String loccatLog, String fileLog) {
        [expectedConsoleLog ? TAG : null, expectedFileLog ? TAG : null]
    }
    def void checkLog(MockedLogSystem logging, Map expected) {
        assert logging.logcatLogger.lastDebugTag == expected.logcatDebugTag
        assert logging.logcatLogger.lastDebugLog == expected.logcatDebugLog
        assert logging.logcatLogger.lastInfoTag == expected.logcatInfoTag
        assert logging.logcatLogger.lastInfoLog == expected.logcatInfoLog
        assert logging.logcatLogger.lastWarningTag == expected.logcatWarningTag
        assert logging.logcatLogger.lastWarningLog == expected.logcatWarningLog
        assert logging.logcatLogger.lastErrorTag == expected.logcatErrorTag
        assert logging.logcatLogger.lastErrorLog == expected.logcatErrorLog
        assert logging.logcatLogger.lastException == expected.logcatException

        assert logging.fileLogger.lastDebugTag == expected.fileDebugTag
        assert logging.fileLogger.lastDebugLog == expected.fileDebugLog
        assert logging.fileLogger.lastInfoTag == expected.fileInfoTag
        assert logging.fileLogger.lastInfoLog == expected.fileInfoLog
        assert logging.fileLogger.lastWarningTag == expected.fileWarningTag
        assert logging.fileLogger.lastWarningLog == expected.fileWarningLog
        assert logging.fileLogger.lastErrorTag == expected.fileErrorTag
        assert logging.fileLogger.lastErrorLog == expected.fileErrorLog
        assert logging.fileLogger.lastException == expected.fileException
    }

    def genDataForGlobalLogFileDisabled() {
        [
                //loggerFilePath| consoleLoggingEnabled | fileLoggingEnabled ||
                //expectedFileLog | expectedConsoleLog | consoleTag | fileTag | consoleException | fileException
                [null, false, true, null, null,  null, null, null, null],
                [null, true, false, null, "la1",   null, TAG, null, EXCEPTION],
                ["path", false, false, null, null,  null, null, null, null],
                ["path", false, true, "la1", null,  TAG, null, EXCEPTION, null],
                ["path", true, true, "la1", "la1",  TAG, TAG, EXCEPTION, EXCEPTION]
        ]
    }

}
