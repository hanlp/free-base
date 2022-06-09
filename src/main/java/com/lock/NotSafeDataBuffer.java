package com.lock;


import com.util.PrintUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class NotSafeDataBuffer<T> implements DataBuffer<T>{

    private static final ReentrantLock lock = new ReentrantLock();

    private BlockingQueue<T> data = new LinkedBlockingQueue<>();

    private AtomicInteger ct = new AtomicInteger(0);
    @Override
    public  T add(T t) {
        data.add(t);
        ct.incrementAndGet();
        checkCt();
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
    public  T fetchOne() {
        if (ct.get() <= 0) {
            PrintUtil.println("仓库为空,不消费...");
            return null;
        }

        T t = data.poll();
        ct.decrementAndGet();
        checkCt();
        return t;
    }

}
