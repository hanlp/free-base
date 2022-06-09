package com.lock;

import com.google.common.collect.Lists;
import com.util.DateUtil;
import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SemaphoreTest {

    public static void main(String[] args) throws Exception {
        testSemaphoreLock();
    }


    private static final int user_ct = 10;
    private static final int permit_ct = 4;

    /**
     * 共享锁测试
     */
    private static void testSemaphoreLock() throws InterruptedException {


        final CountDownLatch latch = new CountDownLatch(user_ct);
        final Semaphore semaphore = new Semaphore(permit_ct);

        AtomicInteger integer = new AtomicInteger(0);

        Runnable runnable = () -> {
            try {
                semaphore.acquire(1);
                PrintUtil.println("{},{}, 处理中..., 编号={}",ThreadUtil.getName(), DateUtil.getNowTime(), integer.incrementAndGet());
                ThreadUtil.sleep(1000);
                semaphore.release(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch.countDown();
        };



        List<Thread> threads = Lists.newArrayList();
        for (int i = 0; i < user_ct; i++) {
            threads.add(new Thread(runnable, "线程-"+ i));
        }
        threads.forEach(Thread::start);

        latch.await();
        PrintUtil.println("处理完成...");
    }
}
