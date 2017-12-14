package test.pkg;

import com.codigine.easylog.EasyLog;

public class AnnotatedInner {
    public class Inner {
        @EasyLog
        public Object log;
    }
}