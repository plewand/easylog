package com.codigine.easylog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAspect {
   // @DeclareParents("codigine.com.easylogtest.MainActivity")
    //public Logger log;
    /*
        @Pointcut("within(@com.codigine.easylog.Log *)")
        public void classWithLogger() {

        }


        @Around("classWithLogger()")
        public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
            Object result = joinPoint.proceed();
            System.out.println("aspect");
            return result;
        }

    @After("staticinitialization(*)")
    public void weaveJoinPoint1(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aspect");
    }
 */
   // this interface can be outside of the aspect
   public interface Moody {
       String getMood();
   };

    // this implementation can be outside of the aspect
    public static class MoodyImpl implements Moody {
        private String mood = "asa";

        public String getMood() {
            return mood;
        }
    }

    // the field type must be the introduced interface. It can't be a class.
    @DeclareParents(value="codigine.com..*",defaultImpl=MoodyImpl.class)
    private Moody implementedInterface;

    @Before("execution(* *.*(..)) && this(m)")
    void feelingMoody(Moody m) {
        System.out.println("I'm feeling " + m.getMood());
    }
}
