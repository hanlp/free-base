package com.lock;


import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义独占锁
 */
public class SimpleMockLock implements Lock {


    private static final int tct = 10;
    private static int ct = 0;

    private static final SimpleMockLock lock = new SimpleMockLock();

    private static CountDownLatch latch = new CountDownLatch(tct);


    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < tct; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    lock.lock();
                    try {
                        ct++;
                    } finally {
                        lock.unlock();
                    }
                    ThreadUtil.sleep(500);
                    PrintUtil.println("{}, 计算{}次...", ThreadUtil.getName(), j);
                }
                latch.countDown();
                PrintUtil.println("{}, 计算完成...", ThreadUtil.getName());


            }, "线程-" + i).start();

        }
        latch.await();

        PrintUtil.println("计算结果：{}", ct);

    }

    private final SyncMock sync = new SyncMock();

    private static class SyncMock extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
    }
    @Override
    public void lock() {
        sync.tryAcquire(1);

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlock() {
        sync.tryRelease(1);
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }


}
