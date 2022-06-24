package com.spring.proxy.cglib;

import com.util.PrintUtil;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        PrintUtil.println("MyMethodInterceptor.intercept start：method={}", method);
        Object result = methodProxy.invokeSuper(o, objects);
        PrintUtil.println("MyMethodInterceptor.intercept end：method={}", method);
        return result;
    }
}
