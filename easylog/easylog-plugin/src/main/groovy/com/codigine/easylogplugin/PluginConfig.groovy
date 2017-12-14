package com.codigine.easylogplugin

import groovy.transform.ToString

import java.text.SimpleDateFormat

@ToString(includeNames=true)
class PluginConfig {
    private static final def DATE_FORMAT = "yyyy.MM.dd, hh:mm:ss.SSS"

    String commonLoggingDirectory = ""
    String commonLoggingFile = ""
    boolean logcatEnabled = true
    boolean fileLoggingEnabled = true
    int maxFileLogSize = 50000
    String dateFormat = DATE_FORMAT

    void setDateFormat(fmt) {
        try {
            dateFormat = new SimpleDateFormat(fmt ?: defaultDateFormat() , Locale.getDefault()).toPattern()
        } catch (Exception e) {
            dateFormat = DATE_FORMAT
        }
    }
}