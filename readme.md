## EasyLog ##

This library allows to simplify logging for Android applications. It wraps default Log funcitonality and adds ability to log to permanent memory.

### Features ### 
* Annotation based logger injection
* Automatic tag generation with class name
* Logging to a common or a logger specific file
* Easy configuration and integration for Java and Kotlin

### Configuration ###

Add to main build.gradle file:
```
buildscript {
    repositories {
        ...
        mavenCentral()
        ...
    }
    ...
    dependencies {
        ...
        classpath 'com.codigine:easylog-plugin:1.0.0'
        ..
    }
}
```

Apply plugin to modules build.gradle files:

```
apply plugin: 'easylog'
```

Add the below permission to AndroidManifest.xml:

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

Configure logging by adding clause to module build.gradle file.

* commonLoggingDirectory - default directory added to every log, also common file
* commonLoggingFile - default file for all logs, that does not specify their own
* fileLoggingEnabled - global flag to control file logging
* logcatEnabled - global flag to control logcat logging
* maxFileLogSize - maximum log file size in bytes before rotating
* dateFormat - date format, see SimpleDateFormat for more inormation

Defaults:


* commonLoggingDirectory - "" - directly to external storage
* commonLoggingFile - "" - no logging to a file
* fileLoggingEnabled - true
* logcatEnabled - true
* maxFileLogSize - 150000
* dateFormat - "yyyy/MM/dd hh:mm:ss.SSS"

Each parameter is optional. Below configuration from example program:
```
easylog {
    commonLoggingDirectory = "easylog_test"
    commonLoggingFile = "common.txt"
    fileLoggingEnabled = true
    logcatEnabled = true
    maxFileLogSize = 70000
    dateFormat = "yyyy.MM.dd, hh:mm:ss z"
}

```

### Usage ###

Each logger is injected for annotated class fields. EasyLog annotation has parameters:

* tag - android Log tag, also added to file logs, if empty or not give - field class name
* fileLogPath - if specified, a file for this log is created
* logcatEnabled - initial flag controlling logcat logging
* fileLoggingEnabled - initial flag controlling file logging

Defaults:

* tag - ""
* fileLogPath - ""
* logcatEnabled - true
* fileLoggingEnabled - true

#### Examples: ####

See 2 projects:
* test - only Java
* testkotlin - Kotlin with Java

Log only to logcat with field class name as a log cat:
```
@EasyLog(fileLoggingEnabled = false)
private Logger activityLogger;
```
Log to a common file with tag given (common file must be specified in build.gradle):
```
@EasyLog(tag = "Common file")
private Logger loggerCommonFile;
```
Custom file logger with tag given:
```
@EasyLog(tag = "Custom file", fileLogPath = "single_logger.txt")
private Logger loggerCustomFile;
```

### Kotlin remarks ###

Beside easylog plugin add Kotlin annotation processor for your modules:

```
apply plugin: 'kotlin-kapt'

apply plugin: 'easylog'
```

Use ```lateinit var``` fields to declare loggers. It will allow to inject the logger after initialization:
```
@EasyLog
lateinit var log:Logger
```

### Notes ###
Global parameters have precedence over logger configuration. If you specify the global logging directory,
it will precede the logger file. File an logcat logging for a specific logger will be applied only, if
corresponding global flags are set.

You can also create Logger object with Logging.newLogger(...) method, but
recommended way is to use annotation.

Add mavenLocal repository, if you build the library yourself (see the examples).