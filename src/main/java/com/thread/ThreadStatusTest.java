package com.thread;

import com.alibaba.fastjson.JSON;
import com.util.ThreadUtil;

import java.util.concurrent.locks.LockSupport;

public class ThreadStatusTest {


    private static ThreadStatusTest test = new ThreadStatusTest();

    public static void main(String[] args) throws Exception {


//        doWait();
//        doJoin();
        doInterrupt();
    }

    private static void doInterrupt() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (test) {
                        test.wait();
                    }
                } catch (Exception e) {
                    System.out.println("doInterrupt...exception...");
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        System.out.println("main..." + thread.getState());

        for (int i = 0; i < 5; i++) {
            ThreadUtil.sleep(500);
            System.out.println("interrupt...for..." + thread.getState());
        }
        try {
            thread.interrupt();
        } catch (Exception e) {
            System.out.println("interrupt..." + thread.getState());
        }

    }

    private static void doJoin() {
        Thread thread = new Thread(() -> doRun4Join(false));
        thread.start();
        boolean b = true;
        int i = 0;
        while (b) {
            i++;
            ThreadUtil.sleep(1000);
            System.out.println("while... " + thread.getState());
            if (i % 5 ==0) {
//                Thread thread2 = new Thread(() -> doRun4Join(true));
//                thread2.start();
//                thread2.interrupt();
                System.out.println("while...222... " + thread.getState());

            }
        }
    }

    private static void doRun4Join(boolean b) {
        if (b) {
            Thread.currentThread().notify();
            System.out.println("doJoin...222... " + Thread.currentThread().getState());
            return;
        }
        synchronized (test) {
            System.out.println("doJoin... " + Thread.currentThread().getState());
            try {
                Thread.currentThread().join(5000);
                System.out.println("doJoin...333... " + Thread.currentThread().getState());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void doWait() throws Exception {

        Thread thread = new Thread(() -> doRun(false));
        System.out.println("1_" + JSON.toJSONString(thread.getState()));
        thread.start();
        System.out.println("2_" + JSON.toJSONString(thread.getState()));
        boolean b = true;
        int i = 0;
        while (b) {
            i++;
            ThreadUtil.sleep(1000);
            System.out.println("while_" + JSON.toJSONString(thread.getState()));

            if (i % 5 == 0) {
                Thread thread2 = new Thread(() -> doRun(true));
                thread2.start();
                thread2.interrupt();
                System.out.println("thread3.start..." + Thread.activeCount());
            }
        }


    }

    private static void doRun(boolean b) {
        synchronized (test) {
            while (true) {
                try {
                    if (b) {
                        test.notify();
                        System.out.println("notify_" + JSON.toJSONString(Thread.currentThread().getState()));
                        return;
                    }

//                            test.wait(5000);
                    test.wait();
                    System.out.println("02_" + JSON.toJSONString(Thread.currentThread().getState()));

                    if (Thread.currentThread().getState() == Thread.State.WAITING) {
                        test.notify();
                        System.out.println("notify_" + JSON.toJSONString(Thread.currentThread().getState()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
