package com.thread;

import com.util.ThreadUtil;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCreateTest {

    public static void main(String[] args) throws Exception {
//        createByThread();
//        createByRunnable();
//        createByFuture();
        createByThreadPool();

    }

    private static ExecutorService pool = Executors.newFixedThreadPool(3);

    /**
     * 通过线程池创建线程
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void createByThreadPool() throws ExecutionException, InterruptedException {
        MyRunner myRunner = new MyRunner();
        pool.execute(myRunner);
        pool.execute(new MyThread("my_thread_pool"));

        MyFutureTask task = new MyFutureTask();
        Future submit = pool.submit(task);
        Object o = submit.get();
        System.out.println(o);


        // 关闭线程池
        pool.shutdown();
    }

    /**
     * 通过
     * @throws Exception
     */
    private static void createByFuture() throws Exception {
        MyFutureTask task = new MyFutureTask();
        FutureTask futureTask = new FutureTask(task);
        Thread thread = new Thread(futureTask);
        thread.start();
        Object o = futureTask.get(1, TimeUnit.SECONDS);
        System.out.println(o);
    }

    private static void createByRunnable() {
        MyRunner myRunner = new MyRunner();

        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(myRunner);
            thread.start();
        }
    }

    private static void createByThread() {
        for (int i = 0; i < 2; i++) {
            Thread thread = new MyThread("MyThread-" + i);
            thread.start();
        }
    }


    public static class MyFutureTask implements Callable {
        private static final AtomicInteger index = new AtomicInteger(1);
        @Override
        public Object call() throws Exception {
            ThreadUtil.sleep(1000);
            return "hello_" + index.getAndIncrement();
        }
    }

    public static class MyRunner implements Runnable {

        private int sum = 10;

        private int init = 0;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "_MyRunner.run...");
            while (init <= sum) {
                System.out.println(Thread.currentThread().getName() + "_" + init++);
            }
        }
    }

    public static class MyThread extends Thread {

        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "_MyThread.run...");
        }
    }


}
