package com.jvm.memory;

import com.sun.glass.ui.Application;

public class JVMStackTest {

    public static void main(String[] args) {
//        overFlow();
        oom();
//        test();
    }


    /**
     * true
     * false
     * true
     */
    private static void test() {
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);

        String str3 = new StringBuilder("ja").append("va1").toString();
        System.out.println(str3.intern() == str3);

        applicationRun();

    }

    private static void applicationRun() {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                String javaName = Application.GetApplication().getName();
                String str4 = new StringBuilder("ja").append("va").toString();
                System.out.println(str4 +  "="+ (str4.intern() == javaName));
            }
        });
        Application.run(thread);

    }


    private static void whileTrue() {
        while (true) {
            System.out.println(11);
        }
    }

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     */
    private static void oom() {
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    whileTrue();
                }
            });
            thread.start();
        }
    }


    private static Integer index = 0;

    /**
     * java.lang.StackOverflowError
     * 	at sun.nio.cs.UTF_8.updatePositions(UTF_8.java:77)
     * 	at sun.nio.cs.UTF_8.access$200(UTF_8.java:57)
     */
    private static void overFlow() {
        index ++;
        System.out.println(index);
        overFlow();
    }

}
