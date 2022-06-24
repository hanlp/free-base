package com.util;


import com.designmodel.masterworker.MasterWorkerTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    private static AtomicInteger fixedRateThreadNo = new AtomicInteger();
    public static ScheduledExecutorService scheduleAtFixedRate(Runnable runnable, int period, TimeUnit unit) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
//        Thread thread = new Thread(runnable, "pool-fixed-rate" + fixedRateThreadNo.getAndIncrement());
        executorService.scheduleAtFixedRate(runnable, 1, period, unit);
        return executorService;
    }
}
