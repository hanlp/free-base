package com.util;


public class ThreadUtil {


    public static void sleep(int millis) {
        try {
//            LockSupport.parkNanos(millis * 1000L * 1000L);
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getName() {
        return Thread.currentThread().getName();
    }
}
