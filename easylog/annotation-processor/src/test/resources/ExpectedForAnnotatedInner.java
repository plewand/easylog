package test.pkg.generated;

import java.lang.Exception;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import test.pkg.AnnotatedInner.Inner;

@Aspect
public class test_pkg_AnnotatedInner_Innerlog {
  @Before("initialization(test.pkg.AnnotatedInner.Inner.new(..)) && this(o)")
  public void main(JoinPoint joinPoint, Inner o) throws Exception {
    java.lang.reflect.Field declaredField = test.pkg.AnnotatedInner.Inner.class.getDeclaredField("log");
                    boolean accessible = declaredField.isAccessible();
                    declaredField.setAccessible(true);
                    declaredField.set(o, com.codigine.easylog.Logging.newLog("Inner", "", true, true));
                    declaredField.setAccessible(accessible);
  }
}
