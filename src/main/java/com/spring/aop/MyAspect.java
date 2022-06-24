package com.spring.aop;

import com.util.PrintUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class MyAspect {

//    @Pointcut("execution(* com.spring.aop.AopTestBean.*(..))")
    @Pointcut("execution(* *.doExe(..))")
    public void test(){

    }

    @Before("test()")
    public void beforeTest(){
        PrintUtil.println("beforeTest...");
    }

    @After("test()")
    public void afterTest(){
        PrintUtil.println("afterTest...");
    }
    @Around("test()")
    public Object aroundTest(ProceedingJoinPoint point) throws Throwable {
        PrintUtil.println("aroundTest...start...");

        Object proceed = point.proceed();
        PrintUtil.println("aroundTest...end...");

        return proceed;
    }

}
