package test.pkg;

import com.codigine.easylog.EasyLog;

public class AnnotatedAnonymous {
    //Not yest supported, should be ignored (JSR 308 suggestions)
    public Object obj = new Object() {
        @EasyLog
        public Object log;
    };
}