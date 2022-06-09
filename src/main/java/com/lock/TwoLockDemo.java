package com.lock;

import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.util.concurrent.locks.Lock;

public class TwoLockDemo {

    public static void  useTowlockInterruptiblyLock(Lock lock1, Lock lock2) {
        String lock1Name = lock1.toString().replace("java.util.concurrent.locks.", "");
        String lock2Name = lock2.toString().replace("java.util.concurrent.locks.", "");

        PrintUtil.println("开始抢第一把锁，为：{}", lock1Name);
        try {
            lock1.lockInterruptibly();
        } catch (InterruptedException e) {
            PrintUtil.println("被中断，抢第一把锁失败，为：{}", lock1Name);
            return;
        }

        try {
            PrintUtil.println("抢到了第一把锁，为：{}", lock1Name);
            PrintUtil.println("开始抢第二把锁，为：{}", lock2Name);

            try {
                lock2.lockInterruptibly();
            } catch (InterruptedException e) {
                PrintUtil.println("被中断，抢第二把锁失败，为：{}", lock2Name);
                return;
            }

            try {
                PrintUtil.println("抢到了第二把锁：{}", lock2Name);
                PrintUtil.println("do something...");
                ThreadUtil.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock2.unlock();
                PrintUtil.println("释放了第二把锁，为：{}", lock2Name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
            PrintUtil.println("释放了第一把锁，为：{}", lock1Name);
        }
    }
}
