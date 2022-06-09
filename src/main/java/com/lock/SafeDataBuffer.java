package com.lock;


import com.lock.model.ObjectLock;
import com.util.PrintUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SafeDataBuffer<T> implements DataBuffer<T>{

    private static final ReentrantLock lock = new ReentrantLock();

    private static final int max_queue_size = 10;

    private BlockingQueue<T> data = new LinkedBlockingQueue<>(max_queue_size);

    private final Object not_full = new Object();

    private final Object not_empty = new Object();

    private final Object object_lock = new Object();

    private AtomicInteger ct = new AtomicInteger(0);
    @Override
    public  T add(T t) throws Exception {

        while (ct.get() >= max_queue_size) {
            synchronized (not_full) {
                PrintUtil.println("仓库满了...");
                not_full.wait();

            }
        }
        lock.lock();
        try {
            data.add(t);
            ct.incrementAndGet();
            checkCt();
        } finally {
            lock.unlock();
        }
//        synchronized (object_lock) {
//            data.add(t);
//            ct.incrementAndGet();
//            checkCt();
//        }

        synchronized (not_empty) {
            not_empty.notify();
        }
        return t;
    }

    private void checkCt() {
        if (data.size() != ct.get()) {
            String format = String.format("数量不一致：data.size=%s, ct=%s", data.size(), ct.get());
            throw new RuntimeException(format);
        }
        PrintUtil.println("库存：{}", ct.get());
    }

    @Override
    public  T fetchOne() throws Exception {
        while (ct.get() <= 0) {
            synchronized (not_empty) {
                PrintUtil.println("仓库为空,不消费...");
                not_empty.wait();
            }
        }

        lock.lock();
        T t;
        try {
            t = data.poll();
            ct.decrementAndGet();
            checkCt();
        } finally {
            lock.unlock();
        }

//        synchronized (object_lock) {
//            t = data.poll();
//            ct.decrementAndGet();
//            checkCt();
//        }

        synchronized (not_full) {
            not_full.notify();
        }
        return t;
    }

}
