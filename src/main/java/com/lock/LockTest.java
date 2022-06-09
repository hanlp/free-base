package com.lock;

import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    public static void main(String[] args) throws Exception {

//        threadPlus();

        testDeadLock();

    }


    private static ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();

    private static void testDeadLock() {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        Runnable r1 = () -> TwoLockDemo.useTowlockInterruptiblyLock(lock1, lock2);
        Runnable r2 = () -> TwoLockDemo.useTowlockInterruptiblyLock(lock2, lock1);

        Thread t1 = new Thread(r1, "thread-1");
        Thread t2 = new Thread(r2, "thread-2");
        t1.start();
        t2.start();

        ThreadUtil.sleep(2000);

        PrintUtil.println("等待2秒，开始死锁监控和处理...");
        long[] deadlockedThreads = mxBean.findDeadlockedThreads();
        if (deadlockedThreads.length > 0) {
            PrintUtil.println("发生了死锁，死锁线程信息：");

            for (long pid : deadlockedThreads) {
                ThreadInfo threadInfo = mxBean.getThreadInfo(pid, Integer.MAX_VALUE);
                PrintUtil.println(threadInfo);
            }

        }
        PrintUtil.println("中断一个死锁线程，线程：{}", t1.getName());
        t1.interrupt();
    }

    /**
     * 多线程 int自增
     * @throws Exception
     */
    private static void threadPlus() throws Exception {
        int threadCt = 10;
        int turn = 1000;
        CountDownLatch latch = new CountDownLatch(threadCt);
        NotSafePlus notSafePlus = new NotSafePlus();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < turn; i++) {
                    notSafePlus.plus();
                }
                latch.countDown();
            }
        };
        for (int i = 0; i < threadCt; i++) {
            new Thread(runnable).start();
        }

        latch.await();

        System.out.println("理论值：" + threadCt * turn);
        System.out.println("实际值：" + notSafePlus.getV());
    }

    public static class NotSafePlus {

        private AtomicInteger v =  new AtomicInteger(0);
//        private int v = 0;

        private void plus(){
//            v++;
            v.incrementAndGet();
        }

        public int getV() {
            return v.get();
//            return v;
        }
    }
}
