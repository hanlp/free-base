package com.spring;

import com.alibaba.fastjson.JSON;
import com.spring.aop.AopTestBean;
import com.spring.customtag.MyUser;
import com.spring.event.MyEvent;
import com.spring.model.HelloBean;
import com.spring.proxy.cglib.MyCglibService;
import com.spring.proxy.cglib.MyMethodInterceptor;
import com.spring.proxy.jdkdynamic.MyInvocationHandler;
import com.spring.proxy.jdkdynamic.MyProxyService;
import com.spring.proxy.jdkdynamic.MyProxyServiceImpl;
import com.util.PrintUtil;
import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloSpring {

    public static void main(String[] args) {
//        registerBeanTest();

//        customtagTest();
//        eventTest();
//        aopTest();
//        proxy4JdkDynamicTest();
        proxy4CglibTest();
    }

    private static void proxy4CglibTest() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyCglibService.class);
        enhancer.setCallback(new MyMethodInterceptor());

        MyCglibService service = (MyCglibService)enhancer.create();
        service.say();
        PrintUtil.println(service);
    }

    private static void proxy4JdkDynamicTest() {
        MyProxyService service = new MyProxyServiceImpl();
        MyInvocationHandler handler = new MyInvocationHandler(service);
        MyProxyService proxy = (MyProxyService)handler.getProxy();
        proxy.say();
    }

    private static void aopTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        AopTestBean aopTestBean = context.getBean("aopTestBean", AopTestBean.class);
        aopTestBean.doExe();
    }

    private static void eventTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        MyEvent myEvent = new MyEvent("hello","bye");
        context.publishEvent(myEvent);
    }


    private static void registerBeanTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(HelloBean.class);
        HelloBean helloBean = context.getBean("helloBean", HelloBean.class);
        helloBean.sayHello();
    }


    private static void customtagTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-customtag.xml");
        MyUser myUser = (MyUser) context.getBean("myUser");
        System.out.println(JSON.toJSONString(myUser));
    }

    @Test
    public void junitTest() {
        PrintUtil.println(11);
    }

}
