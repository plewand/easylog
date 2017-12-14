package test.pkg.generated;

import java.lang.Exception;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import test.pkg.FullAnnotated;

@Aspect
public class test_pkg_FullAnnotatedlog {
  @Before("initialization(test.pkg.FullAnnotated.new(..)) && this(o)")
  public void main(JoinPoint joinPoint, FullAnnotated o) throws Exception {
    java.lang.reflect.Field declaredField = test.pkg.FullAnnotated.class.getDeclaredField("log");
                    boolean accessible = declaredField.isAccessible();
                    declaredField.setAccessible(true);
                    declaredField.set(o, com.codigine.easylog.Logging.newLog("tag", "path", false, false));
                    declaredField.setAccessible(accessible);
  }
}
