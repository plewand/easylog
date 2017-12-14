package test.pkg;

import com.codigine.easylog.EasyLog;

public class AnnotatedLocal {
    public void func() {
        @EasyLog
        Object log;
    }
}