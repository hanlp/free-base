package com.lock;

import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

    public static void main(String[] args) {
        testLockSupport();
    }


    /**
     * 测试LockSupport 线程的阻塞和唤起
     */
    public static void testLockSupport() {
        Thread thread = new Thread(() -> {
            PrintUtil.println("{}：进入阻塞状态...", ThreadUtil.getName());
            ThreadUtil.sleep(5000);
            LockSupport.park();
            PrintUtil.println("{}：被唤醒了...", ThreadUtil.getName());
        }, "阻塞线程演示Thread");

        thread.start();
        LockSupport.unpark(thread);
        PrintUtil.println("第一次唤醒:{}", thread.getName());
        LockSupport.unpark(thread);
        PrintUtil.println("第二次唤醒:{}", thread.getName());
        ThreadUtil.sleep(6000);
        LockSupport.unpark(thread);
        PrintUtil.println("第三次唤醒:{}", thread.getName());
    }

}
