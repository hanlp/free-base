package com.lock;

import com.google.common.collect.Maps;
import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo {

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();


    static final Map<String, String> dataMap = Maps.newHashMap();



    public static void main(String[] args) throws Exception {
        testReadWriteLock();

    }

    private static Object put(String k, String v) {
        writeLock.lock();
        try {
            PrintUtil.println("{} 抢了writeLock，开始执行write", ThreadUtil.getName());
            ThreadUtil.sleep(1000);
            String put = dataMap.put(k, v);
            return put;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }

        return null;
    }

    private static Object get(String k) {
        readLock.lock();
        try {
            PrintUtil.println("{} 抢了readLock，开始执行read", ThreadUtil.getName());
            ThreadUtil.sleep(1000);
            return dataMap.get(k);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }

        return null;
    }

    /**
     * 1、读线程为共享锁
     * 2、写线程独占锁
     * 3、读&写线程 互斥
     * @throws InterruptedException
     */
    private static void testReadWriteLock() throws InterruptedException {

        Runnable writeRunnable = () -> put("k", "v");
        Runnable readRunnable = () -> get("k");

        for (int i = 0; i < 4; i++) {
            new Thread(readRunnable,"读线程-" + i).start();
        }

        for (int i = 0; i < 3; i++) {
            new Thread(writeRunnable,"写线程-" + i).start();
        }
    }
}
