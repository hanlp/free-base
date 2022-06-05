package com.jmm;

import com.alibaba.fastjson.JSON;

public class VolatileTest {
    private static volatile int race = 0;

    private static void inc() {
        race++;
    }

    public static void main(String[] args) {

        doThreadRun();


    }

    private static void doThreadRun() {
        int tct = 10;
        Thread[] thread = new Thread[tct];
        for (int i = 0; i < tct; i++) {
            thread[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        inc();
                    }
                }
            });
            thread[i].start();
        }
        while (Thread.activeCount() > 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            System.out.println(JSON.toJSONString(threadGroup));
            System.out.println("yield..." +Thread.activeCount() );
            Thread.yield();
        }
        System.out.println(race);
    }
}
