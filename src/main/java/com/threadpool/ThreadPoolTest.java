package com.threadpool;

import com.thread.ThreadCreateTest;
import com.util.ThreadUtil;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {


    public static void main(String[] args) throws Exception {
//        newSingle();
//        newFixed();
//        newCached();
        newScheduled();
//        newFormal();
    }

    private static void newFormal() throws Exception {
        ThreadCreateTest.MyFutureTask task = new ThreadCreateTest.MyFutureTask();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory());
        for (int i = 0; i < 10; i++) {
//            ThreadUtil.sleep(1000);
            Future submit = executor.submit(task);
            System.out.println("submit.size=" + i);
            System.out.println(submit.get());
        }
        boolean b = true;
        while (b) {
            ThreadUtil.sleep(400);
            BlockingQueue<Runnable> queue = executor.getQueue();
            System.out.println("queue.size=" + queue.size());
        }
    }

    private static void newScheduled() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 10; i++) {
            MyRunnable myRunnable = new MyRunnable();
            service.scheduleAtFixedRate(myRunnable, 1, 6, TimeUnit.SECONDS);
        }
        while (true) {
            ThreadUtil.sleep(500);
            System.out.println(service.isShutdown());
        }

    }

    /**
     * 线程总数量无限制Integer.MAX_VALUE
     * 会无限创建线程，可能会导致资源耗尽
     * 线程空闲60s，会进行回收
     */
    private static void newCached() {
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            MyRunnable myRunnable = new MyRunnable();
            service.execute(myRunnable);
        }

        while (true) {
            System.out.println(service.isShutdown());
            ThreadUtil.sleep(400);
            System.out.println(Thread.activeCount());

        }
//        ThreadUtil.sleep(1000);
//        service.shutdown();
    }

    /**
     * 无限大阻塞队列 ！！！！！
     */
    private static void newFixed() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            MyRunnable myRunnable = new MyRunnable();
            service.execute(myRunnable);
        }
        ThreadUtil.sleep(1000);
        service.shutdown();
    }

    /**
     * 无限大阻塞队列 ！！！！！
     */
    private static void newSingle() {
        ExecutorService service = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 10; i++) {
            MyRunnable myRunnable = new MyRunnable();
            service.execute(myRunnable);
        }
        service.shutdown();

    }

    public static class MyRunnable implements Runnable {

        private String name;

        private static AtomicInteger atomicInteger = new AtomicInteger(0);

        public MyRunnable() {
            super();
            int index = atomicInteger.getAndIncrement();
            name = "MyRunnable_" + index;
        }

        public String getName() {
            return name;
        }

        @Override
        public void run() {
//            ThreadUtil.sleep(2000);
            System.out.println(getName() +" done...");
        }
    }
}
