package test.pkg;

import com.codigine.easylog.EasyLog;

public class FullAnnotated {
    @EasyLog(tag = "tag", fileLogPath = "path", logcatEnabled = false, fileLoggingEnabled=false)
    public Object log;
}