package test.pkg.generated;

import java.lang.Exception;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import test.pkg.Annotated;

@Aspect
public class test_pkg_Annotatedlog {
  @Before("initialization(test.pkg.Annotated.new(..)) && this(o)")
  public void main(JoinPoint joinPoint, Annotated o) throws Exception {
    java.lang.reflect.Field declaredField = test.pkg.Annotated.class.getDeclaredField("log");
                    boolean accessible = declaredField.isAccessible();
                    declaredField.setAccessible(true);
                    declaredField.set(o, com.codigine.easylog.Logging.newLog("Annotated", "", true, true));
                    declaredField.setAccessible(accessible);
  }
}
